package com.example.testSpringBoot.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "vocabularyList")
@Getter
@Setter
public class VocabularyListWrapper {

    @XmlElement(name = "word")
    private List<Vocabulary> vocabularies;

    public VocabularyListWrapper() {}

    public VocabularyListWrapper(List<Vocabulary> vocabularies) {
        this.vocabularies = vocabularies;
    }
}