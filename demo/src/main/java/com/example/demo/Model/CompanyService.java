package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company_services",
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "service_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceType service;
}
