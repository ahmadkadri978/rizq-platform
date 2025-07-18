package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.LoginResponse;
import kadri.rizq_platform.dto.RegistrationRequestDto;
import org.springframework.data.domain.Page;

public interface RegistrationRequestService {
    Page<RegistrationRequestDto> getPendingRequests(int page, int size);
    void rejectRequest(Long id);
    void submitRequest(RegistrationRequestDto dto);
    LoginResponse approveRequest(Long id);


}

