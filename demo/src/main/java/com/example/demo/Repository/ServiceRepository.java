package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Provider.Service;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>{
    List<Service> findByIsActiveTrue();
}
