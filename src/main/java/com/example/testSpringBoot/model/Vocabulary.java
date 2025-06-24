package com.example.testSpringBoot.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient; // <-- Thêm import này
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "vocabulary")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement
    @Column(nullable = false)
    private String term;

    @XmlElement
    @Column(columnDefinition = "TEXT", nullable = false)
    private String definition;

    @XmlElement
    private String ipa;

    @XmlElement
    @Column(columnDefinition = "TEXT")
    private String example;

    @XmlTransient // <-- THÊM ANNOTATION NÀY ĐỂ JAXB BỎ QUA TRƯỜNG NÀY
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}