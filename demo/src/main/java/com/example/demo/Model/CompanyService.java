package com.example.demo.Model;


@Entity
@Table(name = "company_services", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "service_id}))
public class CompanyService {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

}
