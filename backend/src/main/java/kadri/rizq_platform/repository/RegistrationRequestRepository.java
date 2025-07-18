package kadri.rizq_platform.repository;

import kadri.rizq_platform.entity.RegistrationRequest;
import kadri.rizq_platform.entity.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RegistrationRequestRepository  extends JpaRepository<RegistrationRequest, Long> {
    Page<RegistrationRequest> findByStatus(RequestStatus status, Pageable pageable);
    boolean existsByPhoneNumberAndStatus(String phoneNumber, RequestStatus status);

}
