package ru.kpfu.itis.subscribio.service;

import ru.kpfu.itis.subscribio.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
}