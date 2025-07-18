package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.LoginResponse;
import kadri.rizq_platform.dto.RegistrationRequestDto;
import kadri.rizq_platform.entity.RegistrationRequest;
import kadri.rizq_platform.entity.RequestStatus;
import kadri.rizq_platform.entity.Role;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.RegistrationRequestRepository;
import kadri.rizq_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationRequestServiceImpl implements RegistrationRequestService {

    private final RegistrationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Page<RegistrationRequestDto> getPendingRequests(int page, int size) {
        log.info("Getting pending registration requests - page {}, size {}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return requestRepository.findByStatus(RequestStatus.PENDING, pageable)
                .map(this::mapToDto);
    }

    @Override
    public LoginResponse approveRequest(Long id) {
        RegistrationRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        String generatedUsername = generateUsername(request);
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .serviceType(request.getServiceType())
                .city(request.getCity())
                .username(generatedUsername)
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        log.info("User created from request: {}, username={}, tempPassword={}", id, generatedUsername, rawPassword);

        return new LoginResponse(generatedUsername, rawPassword);
    }


    @Override
    public void rejectRequest(Long id) {
        log.info("Rejecting registration request with ID: {}", id);
        var request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);
    }

    @Override
    public void submitRequest(RegistrationRequestDto dto) {
        log.info("Submitting new registration request for {}", dto.fullName());
        if (requestRepository.existsByPhoneNumberAndStatus(dto.phoneNumber(), RequestStatus.PENDING)) {
            throw new IllegalArgumentException("A pending request already exists with this phone number.");
        }

        RegistrationRequest request = RegistrationRequest.builder()
                .fullName(dto.fullName())
                .phoneNumber(dto.phoneNumber())
                .serviceType(dto.serviceType())
                .city(dto.city())
                .status(RequestStatus.PENDING)
                .build();

        requestRepository.save(request);
    }

    private String generateUsername(RegistrationRequest req) {
        return req.getFullName().replaceAll(" ", "").toLowerCase() + "_" + UUID.randomUUID().toString().substring(0, 4);
    }
    private String generateRandomPassword() {
        int length = 10;
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }

        return password.toString();
    }


    private RegistrationRequestDto mapToDto(RegistrationRequest request) {
        return new RegistrationRequestDto(
                request.getId(),
                request.getFullName(),
                request.getPhoneNumber(),
                request.getServiceType(),
                request.getCity(),
                request.getStatus()
        );
    }
}
