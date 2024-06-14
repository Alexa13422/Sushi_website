package com.project.sushi_website.service;

import com.project.sushi_website.model.StatusHistory;
import com.project.sushi_website.repository.StatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusHistoryService {
    private final StatusHistoryRepository statusHistoryRepository;

    public StatusHistoryService(StatusHistoryRepository statusHistoryRepository) {
        this.statusHistoryRepository = statusHistoryRepository;
    }

    public void save(StatusHistory statusHistory) {
        statusHistoryRepository.save(statusHistory);
    }
}
