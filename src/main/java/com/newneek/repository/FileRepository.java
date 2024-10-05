package com.newneek.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newneek.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
