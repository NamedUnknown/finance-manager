package com.dev.financemanager.service.contact;

import com.dev.financemanager.entity.Contact;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactService {
    List<Contact> findAll();

    List<Contact> findByEmailOrderByCreatedDesc(String email);

    List<Contact> findTop5ByEmailAndAnswerIsNull(String email);

    Contact save(Contact contact);

    void delete(Contact contact);
}
