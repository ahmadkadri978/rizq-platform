package kadri.rizq_platform.controller;


import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.ListingType;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import kadri.rizq_platform.service.ListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final ListingService listingService;

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) ListingType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal,
            Model model) {

        String username = principal.getName();
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // إذا لم يحدد المدينة، نستخدم مدينة المستخدم كخيار افتراضي
        if (city == null || city.isBlank()) {
            city = user.getCity();
        }

        Page<ListingDto> listings = listingService.search(null, city, page, size);

        model.addAttribute("user", user);
        model.addAttribute("listings", listings.getContent());
        model.addAttribute("city", city);
        model.addAttribute("type", type);
        model.addAttribute("cities", List.of("Damascus", "Aleppo", "Homs", "Latakia", "Hama")); // قائمة قابلة للتوسيع

        return "dashboard";
    }
}
