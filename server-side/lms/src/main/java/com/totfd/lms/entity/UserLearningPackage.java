package com.totfd.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_learning_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLearningPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private LearningPackage learningPackage;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @PrePersist
    public void prePersist() {
        assignedAt = LocalDateTime.now();
    }
}
