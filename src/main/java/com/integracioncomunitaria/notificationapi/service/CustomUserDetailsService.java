package com.integracioncomunitaria.notificationapi.service;


import com.integracioncomunitaria.notificationapi.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * busca al usuario por email en lugar de por username.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
            String normalizedEmail = email == null 
            ? "" 
            : email.trim().toLowerCase();
        return repo.findByEmailWithProfile(normalizedEmail)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado con email: " + normalizedEmail)
            );
    }
}
