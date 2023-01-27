package com.dev.financemanager.service.contact;

import com.dev.financemanager.dao.ContactRepository;
import com.dev.financemanager.entity.Contact;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private ContactRepository contactRepository;

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> findByEmailOrderByCreatedDesc(String email) {
        return contactRepository.findByEmailOrderByCreatedDesc(email);
    }

    @Override
    public List<Contact> findTop5ByEmailAndAnswerIsNull(String email) {
        return contactRepository.findTop5ByEmailAndAnswerIsNull(email);
    }

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }
}
