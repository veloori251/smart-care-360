package org.sc.auth.sc.auth.dto;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String email,
        List<String> roles,
        boolean enabled
) {
}
