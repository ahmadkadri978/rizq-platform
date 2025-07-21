package kadri.rizq_platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "registration_requests")
public class RegistrationRequest {

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
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;
}
