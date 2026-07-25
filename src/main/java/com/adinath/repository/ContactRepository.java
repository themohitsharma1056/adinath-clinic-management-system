package com.adinath.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adinath.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}