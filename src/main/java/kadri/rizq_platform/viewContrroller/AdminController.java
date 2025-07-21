package kadri.rizq_platform.viewContrroller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.dto.RegistrationRequestDto;
import kadri.rizq_platform.entity.ListingType;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import kadri.rizq_platform.service.ListingService;
import kadri.rizq_platform.service.RegistrationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final ListingService listingService;
    private final RegistrationRequestService registrationRequestService;

    @GetMapping("/requests")
    public String showPendingRequests(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size, Principal principal , Model model) {
        Page<RegistrationRequestDto> pendingRequests = registrationRequestService.getPendingRequests(page, size);
        model.addAttribute("requests", pendingRequests.getContent());
        model.addAttribute("currentPage", pendingRequests.getNumber());
        model.addAttribute("totalPages", pendingRequests.getTotalPages());
        model.addAttribute("adminName", principal.getName());

        return "admin-requests";
    }

    @PostMapping("/requests/{id}/approve")
    public String approveRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        registrationRequestService.approveRequest(id);
        redirectAttributes.addFlashAttribute("successMessage", "Request approved!");
        return "redirect:/admin/requests";
    }

    @PostMapping("/admin/requests/{id}/reject")
    public String rejectRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        registrationRequestService.rejectRequest(id);
        redirectAttributes.addFlashAttribute("successMessage", "Request rejected.");
        return "redirect:/admin/requests";
    }




}
