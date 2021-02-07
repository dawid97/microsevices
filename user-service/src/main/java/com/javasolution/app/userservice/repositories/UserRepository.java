package com.javasolution.app.userservice.repositories;

import com.javasolution.app.userservice.entities.User;
import com.javasolution.app.userservice.entities.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(final String email);
    List<User> findAllByUserRole(final UserRole userRole);
    Optional<User> findBySsn(final String ssn);
}
