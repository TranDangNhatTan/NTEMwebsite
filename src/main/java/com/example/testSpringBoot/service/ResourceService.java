package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Resource;
import com.example.testSpringBoot.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id);
    }

    public void save(Resource resource) {
        resourceRepository.save(resource);
    }
}