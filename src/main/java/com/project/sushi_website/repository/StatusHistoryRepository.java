package com.project.sushi_website.repository;

import com.project.sushi_website.model.StatusHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StatusHistoryRepository extends CrudRepository<StatusHistory, Integer> {
}