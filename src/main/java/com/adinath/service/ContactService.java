package com.adinath.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.adinath.entity.Contact;
import com.adinath.repository.ContactRepository;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(
            ContactRepository contactRepository) {

        this.contactRepository = contactRepository;

    }

    public Contact save(Contact contact) {

        contact.setSubmittedAt(LocalDateTime.now());

        return contactRepository.save(contact);

    }

    public List<Contact> getAll() {

        return contactRepository.findAll();

    }

    public Contact getById(Long id) {

        return contactRepository.findById(id).orElse(null);

    }

    public void delete(Long id) {

        contactRepository.deleteById(id);

    }
    public long getCount() {

        return contactRepository.count();

    }

}
