package com.totfd.lms.dto.user.request;

import java.util.List;

public record AssignPackagesRequestDTO(
        List<Long> packageIds
) {}
