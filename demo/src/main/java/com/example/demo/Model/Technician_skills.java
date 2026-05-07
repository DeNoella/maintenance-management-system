package com.example.demo.Model;

@Entity 
@Table(name = "Technician_skills")
public class Technician_skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private User technician; //e.g Electrical etc

    @Column(nullable = false, length = 100)
    private String skillName;

    @Column(columnDefinition = "TEXT")
    private String description;
}
