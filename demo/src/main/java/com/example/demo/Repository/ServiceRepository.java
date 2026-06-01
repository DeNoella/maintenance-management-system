package com.example.demo.Repository;

import com.example.demo.Model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceType, Long> {
    List<ServiceType> findByIsActiveTrue();
}