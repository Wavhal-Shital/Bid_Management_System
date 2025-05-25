package com.example.demo.config;

import java.io.FileInputStream;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

@Configuration

public class GoogleOAuthConfig {
     @Value("${google.credentials.file.path}")
    private String credentialsFilePath;

    @Value("${google.tokens.directory.path}")
    private String tokensDirectoryPath;

    public Gmail getGmailService() throws Exception {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(credentialsFilePath))
            .createScoped(List.of(GmailScopes.GMAIL_READONLY, DriveScopes.DRIVE_FILE));
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Bid Management System")
                .build();
    }

    public Drive getDriveService() throws Exception {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(credentialsFilePath))
            .createScoped(List.of(DriveScopes.DRIVE_FILE));
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Bid Management System")
                .build();
    }

}
