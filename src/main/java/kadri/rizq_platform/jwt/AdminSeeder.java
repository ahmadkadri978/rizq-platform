package kadri.rizq_platform.jwt;

import kadri.rizq_platform.entity.Role;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .fullName("rezq platform")
                    .username("admin")
                    .password(passwordEncoder.encode("adminpass"))
                    .phoneNumber("0999999999")
                    .email("rezqplatform@gmail.com")
                    .serviceType("System")
                    .city("Damascus")
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin user created with password: adminpass");
        }
    }
}
