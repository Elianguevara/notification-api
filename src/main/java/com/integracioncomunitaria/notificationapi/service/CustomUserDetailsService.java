package com.integracioncomunitaria.notificationapi.service;

// Importación de las entidades y repositorios necesarios
import com.integracioncomunitaria.notificationapi.entity.User;
import com.integracioncomunitaria.notificationapi.entity.UserProfile;
import com.integracioncomunitaria.notificationapi.entity.RoleType;
import com.integracioncomunitaria.notificationapi.repository.UserProfileRepository;
import com.integracioncomunitaria.notificationapi.repository.UserRepository;

// Importaciones de Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Anotaciones de Spring Framework
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio personalizado que carga los detalles del usuario desde la base de datos,
 * para la autenticación mediante Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;

    // Constructor con inyección de dependencias
    public CustomUserDetailsService(UserRepository userRepo,
                                    UserProfileRepository profileRepo) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
    }

    /**
     * Método principal para cargar los detalles del usuario por email.
     * Es llamado automáticamente por Spring Security durante el login.
     *
     * @param email Email del usuario que intenta autenticarse.
     * @return Objeto UserDetails con los detalles necesarios para la autenticación.
     * @throws UsernameNotFoundException si el usuario no existe.
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Normaliza el email (elimina espacios y lo convierte a minúsculas)
        String normalized = email == null 
            ? "" 
            : email.trim().toLowerCase();

        // 1) Obtiene el usuario de la base de datos junto a su perfil asociado (LEFT JOIN FETCH)
        User user = userRepo.findByEmailWithProfile(normalized)
            .orElseThrow(() ->
                new UsernameNotFoundException("Credenciales inválidas")
            );

        // 2) Verifica si el usuario ya tiene perfil; si no, lo crea automáticamente
        if (user.getProfile() == null) {
            UserProfile p = new UserProfile();
            p.setUser(user);
            p.setEmail(user.getEmail());              // Opcionalmente refleja el email en el perfil
            p.setRoleType(RoleType.cliente);          // Rol predeterminado para nuevos perfiles
            p.setIsAdmin(false);                      // Por defecto no es administrador

            // TODO: Ajusta aquí cualquier otro campo obligatorio (fechas, creador, etc.)
            // Ejemplo: p.setCreatedAt(LocalDateTime.now());

            // Guarda el nuevo perfil en la base de datos
            profileRepo.save(p);

            // Actualiza la referencia del perfil dentro del usuario
            user.setProfile(p);
        }

        // 3) A partir de aquí, user.getProfile() nunca será null.
        // Retorna el usuario completo con perfil cargado para autenticación
        return user;
    }
}
