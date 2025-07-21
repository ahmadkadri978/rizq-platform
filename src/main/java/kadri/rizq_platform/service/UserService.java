package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.UserDto;
import kadri.rizq_platform.entity.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserService {

    Optional<UserDto> findByUsername(String username);
}
