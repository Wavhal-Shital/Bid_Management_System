package com.example.demo.service;

// import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.config.GoogleOAuthConfig;
import com.example.demo.model.Attachment;
import com.example.demo.model.Email;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.EmailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

public class EmailArchivingService {
    @Autowired
    private GoogleOAuthConfig googleOAuthConfig;
    @Autowired 
    private EmailRepository emailRepository;
    @Autowired
     private AttachmentRepository attachmentRepository;

    @Scheduled(fixedDelay = 300000) // every 5 min
    public void fetchAndArchiveEmails() throws Exception {
        Gmail gmail = googleOAuthConfig.getGmailService();
        ListMessagesResponse response = gmail.users().messages().list("me").setQ("is:inbox").execute();
        for (Message msg : response.getMessages()) {
            Message fullMsg = gmail.users().messages().get("me", msg.getId()).setFormat("full").execute();
            if (emailRepository.existsByMessageId(fullMsg.getId())) continue; // skip duplicates

            Email email = new Email();
            email.setMessageId(fullMsg.getId());
            email.setThreadId(fullMsg.getThreadId());
            email.setSubject(extractHeader(fullMsg, "Subject"));
            email.setSender(extractHeader(fullMsg, "From"));
            email.setRecipients(extractHeader(fullMsg, "To"));
            email.setCc(extractHeader(fullMsg, "Cc"));
            email.setBcc(extractHeader(fullMsg, "Bcc"));
            email.setReceivedTime(Instant.ofEpochMilli(fullMsg.getInternalDate()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            email.setBody(new String(Base64.getDecoder().decode(fullMsg.getPayload().getBody().getData())));
            email.setHeaders(new ObjectMapper().writeValueAsString(fullMsg.getPayload().getHeaders()));
            emailRepository.save(email);

            processAttachments(fullMsg, email);
        }
    }

    private void processAttachments(Message message, Email email) throws Exception {
        Drive drive = googleOAuthConfig.getDriveService();
        if (message.getPayload().getParts() != null) {
            for (MessagePart part : message.getPayload().getParts()) {
                if (part.getFilename() != null && part.getBody() != null && part.getBody().getAttachmentId() != null) {
                    Attachment attachment = new Attachment();
                    attachment.setEmail(email);
                    attachment.setFileName(part.getFilename());
                    attachment.setMimeType(part.getMimeType());

                    ByteArrayContent content = new ByteArrayContent(part.getMimeType(), Base64.getDecoder().decode(part.getBody().getData()));
                    File fileMetadata = new File();
                    fileMetadata.setName(part.getFilename());
                    File uploadedFile = drive.files().create(fileMetadata, content).setFields("id, webViewLink").execute();

                    attachment.setDriveLink(uploadedFile.getWebViewLink());
                    attachmentRepository.save(attachment);
                }
            }
        }
    }

    private String extractHeader(Message message, String name) {
        return message.getPayload().getHeaders().stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("");
    }

}
