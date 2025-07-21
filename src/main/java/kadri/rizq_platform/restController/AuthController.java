package kadri.rizq_platform.restController;

import jakarta.validation.Valid;
import kadri.rizq_platform.dto.JwtResponse;
import kadri.rizq_platform.dto.LoginRequest;
import kadri.rizq_platform.jwt.JwtUtil;
import kadri.rizq_platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import kadri.rizq_platform.repository.UserRepository;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login") //Tested ✅
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.username());
        return ResponseEntity.ok(authService.login(request));
    }


}
