package kadri.rizq_platform.dto;

import kadri.rizq_platform.entity.ListingType;

import java.time.LocalDateTime;

public record ListingDto(
        Long id,
        String title,
        String description,
        String city,
        ListingType type,
        String contactInfo,
        String ownerName,
        LocalDateTime createdAt
) {}
