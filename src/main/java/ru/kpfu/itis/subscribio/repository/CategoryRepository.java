package ru.kpfu.itis.subscribio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.subscribio.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}