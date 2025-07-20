package kadri.rizq_platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "service_listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String city;

    @Enumerated(EnumType.STRING)
    private ListingType type; // SERVICE / JOB

    private String contactInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
    @CreatedDate
    private LocalDateTime createdAt;
}
