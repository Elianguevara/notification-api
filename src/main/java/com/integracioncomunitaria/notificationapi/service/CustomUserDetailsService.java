package com.integracioncomunitaria.notificationapi.service;


import com.integracioncomunitaria.notificationapi.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * busca al usuario por email en lugar de por username.
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado con email: " + email)
            );
    }
}
