// src/main/java/com/integracioncomunitaria/notificationapi/service/RegistrationService.java
package com.integracioncomunitaria.notificationapi.service;

import com.integracioncomunitaria.notificationapi.dto.RegisterRequest;
import com.integracioncomunitaria.notificationapi.entity.*;
import com.integracioncomunitaria.notificationapi.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;
    private final CustomerRepository customerRepo;
    private final ProviderRepository providerRepo;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public void register(RegisterRequest req) {
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // 1) Crear User
        User user = new User();
        user.setName(req.getName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user = userRepo.save(user);

        // 2) Crear UserProfile (siempre isAdmin = false)
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setEmail(user.getEmail());
        profile.setRoleType(req.getRoleType());
        profile.setIsAdmin(false);
        profileRepo.save(profile);

        // 3) Crear Customer si aplica
        if (req.getRoleType() == RoleType.cliente || req.getRoleType() == RoleType.ambos) {
            RegisterRequest.CustomerDTO c = req.getCustomer();
            if (c == null) {
                throw new IllegalArgumentException("Datos de customer obligatorios");
            }
            Customer cust = new Customer();
            cust.setName(c.getName());
            cust.setDateYear(c.getDateYear());
            cust.setDni(c.getDni());
            cust.setPhone(c.getPhone());
            cust.setAddress(c.getAddress());
            cust.setGpsLat(c.getGpsLat());
            cust.setGpsLon(c.getGpsLon());
            cust.setUser(user);
            customerRepo.save(cust);
        }

        // 4) Crear Provider si aplica
        if (req.getRoleType() == RoleType.proveedor || req.getRoleType() == RoleType.ambos) {
            RegisterRequest.ProviderDTO p = req.getProvider();
            if (p == null) {
                throw new IllegalArgumentException("Datos de provider obligatorios");
            }
            Provider prov = new Provider();
            prov.setName(p.getName());
            prov.setAddress(p.getAddress());
            prov.setGpsLat(p.getGpsLat());
            prov.setGpsLong(p.getGpsLong());
            prov.setTypeProviderId(p.getTypeProviderId());
            prov.setGradeProviderId(p.getGradeProviderId());
            prov.setProfessionId(p.getProfessionId());
            prov.setOfferId(p.getOfferId());
            prov.setCategory(new Category(p.getCategoryId())); // proxy por ID
            prov.setUser(user);
            providerRepo.save(prov);
        }
    }
}
