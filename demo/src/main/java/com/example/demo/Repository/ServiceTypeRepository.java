package com.example.demo.Repository;

import com.example.mms.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository2<Service, Long>{
    List<Service> findByIsActiveTrue();
}
