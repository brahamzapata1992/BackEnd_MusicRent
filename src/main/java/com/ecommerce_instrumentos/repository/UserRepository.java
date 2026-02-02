package com.ecommerce_instrumentos.repository;
import com.ecommerce_instrumentos.entity.User;
import com.ecommerce_instrumentos.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);

    List<User> findByRole(UserRole userRole);
}
