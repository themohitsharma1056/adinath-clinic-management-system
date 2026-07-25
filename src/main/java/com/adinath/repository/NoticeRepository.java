package com.adinath.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adinath.entity.Notice;

public interface NoticeRepository
        extends JpaRepository<Notice, Long> {

    Optional<Notice> findByActiveTrue();

}