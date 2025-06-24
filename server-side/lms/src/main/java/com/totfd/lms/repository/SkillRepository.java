package com.totfd.lms.repository;

import com.totfd.lms.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Modifying
    @Query("DELETE FROM Skill s WHERE s.learningPackage.id = :packageId")
    void deleteByLearningPackageId(@Param("packageId") Long packageId);

    @Query("""
    SELECT s FROM Skill s
    LEFT JOIN FETCH s.topics t
    LEFT JOIN FETCH t.subTopics st
    LEFT JOIN FETCH st.videos v
    LEFT JOIN FETCH st.studyMaterials sm
    WHERE s.id = :id
""")
    Optional<Skill> findByIdWithTopicsAndSubtopicsAndVideosAndMaterials(@Param("id") Long id);




}
