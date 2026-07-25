package com.adinath.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.adinath.entity.Notice;
import com.adinath.repository.NoticeRepository;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(
            NoticeRepository noticeRepository) {

        this.noticeRepository = noticeRepository;

    }

    public Notice save(Notice notice) {

        List<Notice> list =
                noticeRepository.findAll();

        list.forEach(n -> {

            n.setActive(false);

            noticeRepository.save(n);

        });

        notice.setActive(true);

        notice.setUpdatedAt(LocalDateTime.now());

        return noticeRepository.save(notice);

    }

    public Notice getActiveNotice() {

        return noticeRepository
                .findByActiveTrue()
                .orElse(null);

    }

    public List<Notice> getAll() {

        return noticeRepository.findAll();

    }

    public void delete(Long id) {

        noticeRepository.deleteById(id);

    }

}