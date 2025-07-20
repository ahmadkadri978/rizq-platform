package kadri.rizq_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kadri.rizq_platform.entity.ListingType;

import java.time.LocalDateTime;

public record ListingDto(

        Long id,

        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
        String description,

        @NotBlank(message = "City is required")
        String city,

        @NotNull(message = "Listing type is required")
        ListingType type, // SERVICE / JOB

        String ownerName,

        LocalDateTime createdAt
) {}

