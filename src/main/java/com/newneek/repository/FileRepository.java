package com.newneek.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newneek.file.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
