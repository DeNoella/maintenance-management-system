package com.example.demo.Repository;

@Repository
public interface TechnicianSkillRepository extends JpaRepository<TechnicianSkill,Long> {

    List<TechnicianSkill> findByTechnicianId(Long technicianId);
    void deleteByTechnicianId(Long technicianId);
}
