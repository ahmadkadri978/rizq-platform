package kadri.rizq_platform.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.Listing;
import kadri.rizq_platform.entity.ListingType;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import kadri.rizq_platform.service.ListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            @RequestParam(defaultValue = "5") int size,
            Principal principal,
            Model model) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // نمرر null فقط في أول مرة عند عدم وجود city ولا type
        boolean isFirstVisit = (city == null && type == null && page == 0);

        if (isFirstVisit) {
            city = user.getCity(); // نستخدم مدينة المستخدم لعرض بيانات أولية
        } else if (city != null && city.isBlank()) {
            city = null; // إذا اختار "All"، نعطل الفلترة بالمدينة
        }

        Page<ListingDto> listings = listingService.search(null, city, page, size);

        model.addAttribute("user", user);
        model.addAttribute("listings", listings.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listings.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("city", city); // لازم نرجع قيمة city للسلكت
        model.addAttribute("type", type);
        model.addAttribute("cities", List.of("Damascus", "Aleppo", "Homs", "Latakia", "Hama"));

        return "dashboard";
    }
    @GetMapping("/listings/new")
    public String showAddListingForm(Model model) {
        model.addAttribute("listingDto", new ListingDto(
                null,      // id
                "",        // title
                "",        // description
                "",        // city
                null,      // type
                "",        // ownerName
                "",
                null       // createdAt
        ));


        model.addAttribute("cities", List.of("Damascus", "Aleppo", "Homs", "Latakia", "Hama"));
        return "add-listing";
    }

    @PostMapping("/listings/new")
    public String handleAddListing(
            @Valid @ModelAttribute("listingDto") ListingDto dto,
            BindingResult result,
            Principal principal,
            Model model,
            HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute("cities", List.of("Damascus", "Aleppo", "Homs", "Latakia", "Hama"));
            return "add-listing";
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        listingService.createListing(dto , user.getUsername());
        session.setAttribute("successMessage", "Listing created successfully!");

        return "redirect:/my-listings";
    }




}
