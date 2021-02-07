package com.javasolution.app.userservice.repositories;

import com.javasolution.app.userservice.entities.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(final String token);
    ConfirmationToken findConfirmationTokenByUserId(Long id);
}
