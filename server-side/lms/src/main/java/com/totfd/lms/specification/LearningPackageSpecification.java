package com.totfd.lms.specification;

import com.totfd.lms.entity.LearningPackage;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class LearningPackageSpecification {

    public static Specification<LearningPackage> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<LearningPackage> hasMinPrice(BigDecimal minPrice){
        return  ((root, query, criteriaBuilder) -> (minPrice != null) ? criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice):null);
    }

    public static Specification<LearningPackage> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> (maxPrice != null) ? cb.lessThanOrEqualTo(root.get("price"), maxPrice) : null;
    }

}
