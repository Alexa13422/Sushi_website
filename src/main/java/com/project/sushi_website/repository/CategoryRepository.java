package com.project.sushi_website.repository;

import com.project.sushi_website.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category,Integer> {
}
