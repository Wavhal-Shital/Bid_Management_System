package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}
