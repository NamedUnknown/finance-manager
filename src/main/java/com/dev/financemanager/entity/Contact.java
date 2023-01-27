package com.dev.financemanager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "contact")
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "email")
    private String email;

    @Column(name = "answer")
    private String answer;

    @Column(name = "created")
    private Date created;

    @Column(name = "updated")
    private Date updated;
}
