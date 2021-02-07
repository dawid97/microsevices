package com.javasolution.app.userservice.services;

import com.javasolution.app.userservice.entities.ConfirmationToken;
import com.javasolution.app.userservice.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken saveConfirmationToken(final ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> findConfirmationTokenByToken(final String token) {
        return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
    }

    public void deleteConfirmationToken(final Long id) {
        confirmationTokenRepository.deleteById(id);
    }
}
