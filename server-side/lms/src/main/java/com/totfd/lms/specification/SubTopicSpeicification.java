package com.totfd.lms.specification;

import com.totfd.lms.entity.SubTopic;
import com.totfd.lms.entity.Topic;
import org.springframework.data.jpa.domain.Specification;

public class SubTopicSpeicification {
    public static Specification<SubTopic> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }
}
