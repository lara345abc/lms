package com.totfd.lms.repository;

import com.totfd.lms.entity.LearningPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageRepository extends JpaRepository<LearningPackage, Long>, JpaSpecificationExecutor<LearningPackage> {
    @Query("SELECT p.id FROM LearningPackage p")
    Page<Long> findAllPackageIds(Pageable pageable);

    @Query("SELECT DISTINCT p FROM LearningPackage p LEFT JOIN FETCH p.skills WHERE p.id IN :ids")
    List<LearningPackage> findAllWithSkillsByIds(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"skills"})
    @Query("SELECT p FROM LearningPackage p")
    List<LearningPackage> packgeWithAllSkills();

}
