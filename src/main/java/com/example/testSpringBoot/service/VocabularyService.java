package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Vocabulary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VocabularyService {

    List<Vocabulary> findAll();

    void importFromXml(MultipartFile file) throws Exception;

    String exportToXml() throws Exception;
}