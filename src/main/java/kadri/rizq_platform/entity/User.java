package kadri.rizq_platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)

    private String email;
    @Column(nullable = false)
    private String serviceType;
    @Column(nullable = false)
    private String city;

    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN / USER
}

