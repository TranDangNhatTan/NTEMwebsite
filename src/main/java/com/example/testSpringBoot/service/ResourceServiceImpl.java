package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Resource;
import com.example.testSpringBoot.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl extends ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public void save(Resource resource) {
        resourceRepository.save(resource);
    }

//    @Override
    public Resource findById(Integer id) {
        return resourceRepository.findById(Long.valueOf(id)).orElse(null);
    }
}