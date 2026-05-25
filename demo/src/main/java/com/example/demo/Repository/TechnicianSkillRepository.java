package com.example.demo.Repository;

import com.example.mms.model.TechnicianSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianSkillRepository extends JpaRepository<TechnicianSkill,Long> {
    List<TechnicianSkill> findByTechnicianId(Long technicianId);
    void deleteByTechnicianId(Long technicianId);
}
