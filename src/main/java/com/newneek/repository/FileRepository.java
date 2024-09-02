package com.newneek.repository;

import com.newneek.entity.FileEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
