package com.integracioncomunitaria.notificationapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integracioncomunitaria.notificationapi.entity.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Integer> {
    Optional<Provider> findByUser_IdUser(Integer userId);
}
