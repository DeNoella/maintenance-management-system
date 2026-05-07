package com.example.demo.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
    Optional<Company> findByName(String name);
    List<Company> findByIsActiveTrue();
}
