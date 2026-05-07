package com.example.demo.Model;

@Entity
@Table(name ="companies")
public class Company {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable =false, unique =true, length = 150)
    private String name;

    @Column(name = "rob_certificates", length = 100)
    private String robCertificate;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "cretaed_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
