package com.totfd.lms.specification;

import com.totfd.lms.entity.LearningPackage;
import com.totfd.lms.entity.Topic;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class TopicSpecification {

    public static Specification<Topic> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

}
