package com.integracioncomunitaria.notificationapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integracioncomunitaria.notificationapi.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    // Busca el Customer cuyo user.idUser == el parámetro
    Optional<Customer> findByUser_IdUser(Integer userId);
}