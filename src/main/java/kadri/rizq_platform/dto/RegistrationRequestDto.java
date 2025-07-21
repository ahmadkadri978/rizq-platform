package kadri.rizq_platform.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kadri.rizq_platform.entity.RequestStatus;


public record RegistrationRequestDto(
        Long id,
        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{10,12}$", message = "Invalid phone number format")
        String phoneNumber,

        @Email(message = "Invalid email")
        @NotBlank(message = "Email is required")
        String email,


        @NotBlank(message = "Service type is required")
        String serviceType,

        @NotBlank(message = "City is required")
        String city,
        RequestStatus status
) {}
