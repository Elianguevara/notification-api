package com.integracioncomunitaria.notificationapi.service;

import com.integracioncomunitaria.notificationapi.entity.*;
import com.integracioncomunitaria.notificationapi.entity.User;
import com.integracioncomunitaria.notificationapi.repository.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;

    public CustomUserDetailsService(UserRepository userRepo,
                                    UserProfileRepository profileRepo) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        String normalized = email == null 
            ? "" 
            : email.trim().toLowerCase();

        // 1) Traemos el usuario (LEFT JOIN FETCH u.profile)
        User user = userRepo.findByEmailWithProfile(normalized)
            .orElseThrow(() ->
                new UsernameNotFoundException("Credenciales inválidas")
            );

        // 2) Si no tiene perfil, lo creamos con valores por defecto
        if (user.getProfile() == null) {
            UserProfile p = new UserProfile();
            p.setUser(user);
            p.setEmail(user.getEmail());          // si quieres reflejar el mail
            p.setRoleType(RoleType.cliente);      // rol default
            p.setIsAdmin(false);
            // …ajusta otros campos obligatorios (fechas, who-created…)

            profileRepo.save(p);

            // refrescamos el perfil en la entidad User
            user.setProfile(p);
        }

        // 3) Ahora user.getProfile() nunca es null; devolvemos el UserDetails
        return user;
    }
}
