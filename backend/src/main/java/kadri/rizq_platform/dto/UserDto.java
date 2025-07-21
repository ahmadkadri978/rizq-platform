package kadri.rizq_platform.dto;

import kadri.rizq_platform.entity.Role;

public record UserDto(
        Long id,
        String fullName,
        String phoneNumber,
        String email,
        String serviceType,
        String city,
        String username,
        Role role
) {}
