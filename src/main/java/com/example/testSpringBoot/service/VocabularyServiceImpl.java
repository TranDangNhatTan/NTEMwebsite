package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Vocabulary;
import com.example.testSpringBoot.model.VocabularyListWrapper;
import com.example.testSpringBoot.repository.VocabularyRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Override
    public List<Vocabulary> findAll() {
        return vocabularyRepository.findAll();
    }

    @Override
    @Transactional
    public void importFromXml(MultipartFile file) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        VocabularyHandler handler = new VocabularyHandler();
        saxParser.parse(file.getInputStream(), handler);

        List<Vocabulary> vocabularyList = handler.getVocabularyList();
        if (vocabularyList != null && !vocabularyList.isEmpty()) {
            vocabularyRepository.saveAll(vocabularyList);
        }
    }

    @Override
    public String exportToXml() throws Exception {
        List<Vocabulary> vocabularies = vocabularyRepository.findAll();
        VocabularyListWrapper wrapper = new VocabularyListWrapper(vocabularies);

        JAXBContext context = JAXBContext.newInstance(VocabularyListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(wrapper, sw);

        return sw.toString();
    }

    // Lớp nội xử lý logic đọc file XML bằng SAX Parser
    private static class VocabularyHandler extends DefaultHandler {
        private List<Vocabulary> vocabularyList;
        private Vocabulary currentVocabulary;
        private StringBuilder elementValue;

        public List<Vocabulary> getVocabularyList() {
            return vocabularyList;
        }

        @Override
        public void startDocument() {
            vocabularyList = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            elementValue = new StringBuilder();
            if (qName.equalsIgnoreCase("word")) {
                currentVocabulary = new Vocabulary();
                currentVocabulary.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            elementValue.append(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (currentVocabulary != null) {
                switch (qName.toLowerCase()) {
                    case "term":
                        currentVocabulary.setTerm(elementValue.toString().trim());
                        break;
                    case "definition":
                        currentVocabulary.setDefinition(elementValue.toString().trim());
                        break;
                    case "ipa":
                        currentVocabulary.setIpa(elementValue.toString().trim());
                        break;
                    case "example":
                        currentVocabulary.setExample(elementValue.toString().trim());
                        break;
                    case "word":
                        vocabularyList.add(currentVocabulary);
                        break;
                }
            }
        }
    }
}