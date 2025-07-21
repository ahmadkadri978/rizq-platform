package kadri.rizq_platform.viewContrroller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import kadri.rizq_platform.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyListingController {

    private final ListingService listingService;
    private final UserRepository userRepository;

    @GetMapping("/my-listings")
    public String myListings(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<ListingDto> listings = listingService.getMyListings(user.getId(), page, size);

        model.addAttribute("listings", listings.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listings.getTotalPages());

        return "my-listings";
    }
    @GetMapping("/listings/edit/{id}")
    public String showEditForm(@PathVariable Long id, Principal principal, Model model) {
        ListingDto dto = listingService.getByIdForEditing(id, principal.getName());
        model.addAttribute("listingDto", dto);
        model.addAttribute("cities", List.of("Damascus", "Aleppo", "Homs", "Latakia", "Hama"));
        return "edit-listing";
    }

    @PostMapping("/listings/edit/{id}")
    public String updateListing(@PathVariable Long id,
                                @Valid @ModelAttribute ListingDto listingDto,
                                BindingResult result,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit-listing";
        }

        listingService.updateListing(id, listingDto, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Listing updated successfully!");

        return "redirect:/my-listings";
    }
    @PostMapping("/listings/delete/{id}")
    public String deleteListing(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        listingService.deleteListing(id, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Listing deleted successfully!");
        return "redirect:/my-listings";
    }





}
