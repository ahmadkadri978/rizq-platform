package kadri.rizq_platform.restController;

import jakarta.validation.Valid;
import kadri.rizq_platform.dto.JwtResponse;
import kadri.rizq_platform.dto.LoginRequest;
import kadri.rizq_platform.jwt.JwtUtil;
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

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login") //Tested ✅
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.username());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");

            String token = jwtUtil.generateToken(userDetails.getUsername(), role);
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
    }

}
