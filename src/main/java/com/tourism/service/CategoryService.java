package com.tourism.service;

import com.tourism.model.domain.Category;
import com.tourism.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService{

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }


    public Category save(Category category) {
        return categoryRepository.save(category);
    }


    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
