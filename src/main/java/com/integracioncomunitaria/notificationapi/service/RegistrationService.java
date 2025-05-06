package com.integracioncomunitaria.notificationapi.service;

// Importaciones necesarias para entidades y repositorios
import com.integracioncomunitaria.notificationapi.dto.RegisterRequest;
import com.integracioncomunitaria.notificationapi.entity.*;
import com.integracioncomunitaria.notificationapi.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que maneja el proceso de registro de nuevos usuarios en el sistema.
 */
@Service
public class RegistrationService {

    // Valores predeterminados para crear nuevos usuarios/proveedores
    private static final String DEFAULT_ADDRESS             = "Sin especificar";
    private static final Integer DEFAULT_TYPE_PROVIDER_ID   = 1;
    private static final Integer DEFAULT_GRADE_PROVIDER_ID  = 1;
    private static final Integer DEFAULT_PROFESSION_ID      = 1;
    private static final Long    DEFAULT_OFFER_ID           = 1L;
    private static final Integer DEFAULT_CATEGORY_ID        = 1;

    // Repositorios para interactuar con la base de datos
    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;
    private final CustomerRepository customerRepo;
    private final ProviderRepository providerRepo;
    private final PasswordEncoder passwordEncoder;

    // Constructor con inyección de dependencias
    public RegistrationService(UserRepository userRepo,
                               UserProfileRepository profileRepo,
                               CustomerRepository customerRepo,
                               ProviderRepository providerRepo,
                               PasswordEncoder passwordEncoder) {
        this.userRepo        = userRepo;
        this.profileRepo     = profileRepo;
        this.customerRepo    = customerRepo;
        this.providerRepo    = providerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método principal para registrar un nuevo usuario.
     * Este método es transaccional para asegurar integridad en caso de error.
     *
     * @param req DTO con los datos necesarios para el registro.
     */
    @Transactional
    public void register(RegisterRequest req) {

        // 0) Verifica que el email proporcionado no esté ya registrado
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // 1) Creación del usuario básico (tabla "user")
        User user = new User();
        user.setName(req.getName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getEmail());  // Usa email como username predeterminado
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // Contraseña encriptada
        user.setEnabled(true);  // Usuario habilitado inmediatamente después del registro
        user = userRepo.save(user); // Guarda usuario en base de datos y obtiene ID generado

        // 2) Creación del perfil de usuario (tabla "user_profile")
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setEmail(user.getEmail());
        profile.setRoleType(req.getRoleType()); // Asigna rol según solicitud (cliente/proveedor/ambos)
        profile.setIsAdmin(false);  // Por defecto, el usuario no es administrador
        profileRepo.save(profile);

        // 3) Si el rol incluye cliente, crea el registro en tabla "customer"
        RoleType role = req.getRoleType();
        if (role == RoleType.cliente || role == RoleType.ambos) {
            Customer cust = new Customer();
            cust.setUser(user);
            cust.setName(req.getName() + " " + req.getLastName());
            cust.setEmail(req.getEmail());
            cust.setAddress(DEFAULT_ADDRESS);  // Dirección por defecto
            customerRepo.save(cust);
        }

        // 4) Si el rol incluye proveedor, crea el registro en tabla "provider"
        if (role == RoleType.proveedor || role == RoleType.ambos) {
            Provider prov = new Provider();
            prov.setUser(user);
            prov.setName(req.getName() + " " + req.getLastName());
            prov.setAddress(DEFAULT_ADDRESS);
            prov.setTypeProviderId(DEFAULT_TYPE_PROVIDER_ID);     // Tipo por defecto
            prov.setGradeProviderId(DEFAULT_GRADE_PROVIDER_ID);   // Grado por defecto
            prov.setProfessionId(DEFAULT_PROFESSION_ID);          // Profesión por defecto
            prov.setOfferId(DEFAULT_OFFER_ID);                    // Oferta predeterminada
            prov.setCategoryId(DEFAULT_CATEGORY_ID);              // Categoría por defecto
            providerRepo.save(prov);
        }
    }
}
