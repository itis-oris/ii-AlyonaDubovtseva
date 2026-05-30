package ru.kpfu.itis.subscribio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.subscribio.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
            select o from Order o
            join fetch o.user
            order by o.createdAt desc
            """)
    List<Order> findAllWithUsersOrderByCreatedAtDesc();

    @Query("""
        select o from Order o
        join fetch o.user
        left join fetch o.items
        where o.id = :id
        """)
    Optional<Order> findByIdWithUserAndItems(@Param("id") Long id);
}