package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.UserDto;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<UserDto> findByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(this::mapToDto);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getServiceType(),
                user.getCity(),
                user.getUsername(),
                user.getRole()
        );
    }
}

