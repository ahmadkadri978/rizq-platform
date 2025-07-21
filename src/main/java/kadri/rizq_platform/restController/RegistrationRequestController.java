package kadri.rizq_platform.restController;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import kadri.rizq_platform.dto.LoginResponse;
import kadri.rizq_platform.dto.RegistrationRequestDto;
import kadri.rizq_platform.service.RegistrationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RegistrationRequestController {

    private final RegistrationRequestService registrationRequestService;

    //  إرسال طلب تسجيل (مفتوح فقط للزوار)
    @PostMapping("/register-request") //Tested ✅
    @PermitAll
    public ResponseEntity<String> submitRequest(@Valid @RequestBody RegistrationRequestDto dto) {
        log.info("Received new registration request: {}", dto.fullName());
        registrationRequestService.submitRequest(dto);
        return ResponseEntity.ok("Registration request submitted successfully.");
    }

    //  عرض الطلبات المعلقة (Admin فقط)
    @GetMapping("/admin/requests") //Tested ✅
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RegistrationRequestDto>> getPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Admin fetching pending registration requests");
        Page<RegistrationRequestDto> requests = registrationRequestService.getPendingRequests(page, size);
        return ResponseEntity.ok(requests);
    }

    //  قبول طلب
    @PostMapping("/admin/requests/{id}/approve") //Tested ✅
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoginResponse> approveRequest(@PathVariable Long id) {
        log.info("Approving registration request with ID {}", id);
        LoginResponse response = registrationRequestService.approveRequest(id);
        return ResponseEntity.ok(response);
    }

    //  رفض طلب
    @PostMapping("/admin/requests/{id}/reject") //Tested ✅
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id) {
        log.info("Rejecting registration request with ID {}", id);
        registrationRequestService.rejectRequest(id);
        return ResponseEntity.ok("Request rejected.");
    }
}


