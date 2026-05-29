package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "technician_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnicianSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private User technician;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;   // e.g. Electrical, HVAC, Plumbing

    @Column(columnDefinition = "TEXT")
    private String description;
}
