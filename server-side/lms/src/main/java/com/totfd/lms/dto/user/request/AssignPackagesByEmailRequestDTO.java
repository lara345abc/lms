package com.totfd.lms.dto.user.request;

import java.util.List;

public record AssignPackagesByEmailRequestDTO(
        String email,
        List<Long> packageIds
) {}

