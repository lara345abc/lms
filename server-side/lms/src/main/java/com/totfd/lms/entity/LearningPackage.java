package com.totfd.lms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    @OneToMany(mappedBy = "learningPackage", fetch = FetchType.LAZY)
    private List<Skill> skills;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder.Default
    @OneToMany(mappedBy = "learningPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLearningPackage> assignedUsers = new ArrayList<>();
}
