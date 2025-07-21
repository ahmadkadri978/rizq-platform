package kadri.rizq_platform.restController;

import jakarta.validation.Valid;
import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.Listing;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.UserRepository;
import kadri.rizq_platform.service.ListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ListingController {

    private final ListingService listingService;
    private final UserRepository userRepository;

    @PostMapping("/listings") //Tested ✅
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ListingDto> createListing(@Valid @RequestBody ListingDto dto,
                                                    Principal principal) {

        ListingDto saved = listingService.createListing(dto, principal.getName());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/listings") //Tested ✅
    public ResponseEntity<Page<ListingDto>> getByCity(@RequestParam String city,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(listingService.getLatestByCity(city, page, size));
    }

    @GetMapping("/my-listings") //Tested ✅
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ListingDto>> getMyListings(Principal principal,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        User user = userRepository.findByUsername(principal.getName()) // we have to call findMyListings
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(listingService.getMyListings(user.getId(), page, size));
    }
    @GetMapping("/listings/search") //Tested ✅
    public ResponseEntity<Page<ListingDto>> searchListings(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(listingService.search(keyword, city, page, size));
    }


    @PutMapping("/listings/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ListingDto> update(@PathVariable Long id,
                                             @Valid @RequestBody ListingDto dto,
                                             Principal principal) {

        return ResponseEntity.ok(listingService.updateListing(id, dto, principal.getName()));
    }

    @DeleteMapping("/listings/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> delete(@PathVariable Long id, Principal principal) {
        listingService.deleteListing(id, principal.getName());
        return ResponseEntity.ok("Listing deleted successfully");
    }
}
