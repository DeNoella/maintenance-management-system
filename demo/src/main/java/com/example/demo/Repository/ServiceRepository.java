package com.example.demo.Repository;

@Repository
public interface ServiceRepository extends JpaRepository2<Service, Long>{
    List<Service> findByIsActiveTrue();
}
