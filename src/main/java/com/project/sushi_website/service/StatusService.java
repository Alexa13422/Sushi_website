package com.project.sushi_website.service;

import com.project.sushi_website.model.Status;
import com.project.sushi_website.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status findByName(String statusName) {
        return statusRepository.findByStatus(statusName).orElseThrow(() ->
                new IllegalArgumentException("Status not found: " + statusName));
    }
}
