package com.dev.financemanager.controller;

import com.dev.financemanager.dto.response.ErrorResponse;
import com.dev.financemanager.dto.response.SuccessfulResponse;
import com.dev.financemanager.entity.AppUser;
import com.dev.financemanager.entity.Contact;
import com.dev.financemanager.service.contact.ContactService;
import com.dev.financemanager.utils.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final AuthUtils authUtils;

    @GetMapping("/{type}")
    public List<Contact> getContactBasedOnPath(@PathVariable String type) {
        AppUser user = authUtils.getUserFromSecurityContext();
        List<Contact> contact;
        if (type.equals("limited")) {
            contact = contactService.findTop5ByEmailAndAnswerIsNull(user.getEmail());
        } else {
            contact = contactService.findByEmailOrderByCreatedDesc(user.getEmail());
        }
        if (contact == null || contact.isEmpty()) return new ArrayList<>();
        return contact;
    }

    @PostMapping
    public ResponseEntity<Object> saveContact(@RequestBody Contact contact) {
        try {
            contact.setId(0L);
            Contact savedContact = contactService.save(contact);
            return ResponseEntity.ok().body(new SuccessfulResponse<Contact>(HttpStatus.OK.value(), "Contact created", savedContact));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Contact could not be saved"));
        }
    }

}
