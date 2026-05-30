package ru.kpfu.itis.subscribio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.subscribio.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}