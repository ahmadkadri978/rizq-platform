package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.Listing;
import kadri.rizq_platform.entity.ListingType;
import kadri.rizq_platform.entity.User;
import kadri.rizq_platform.repository.ListingRepository;
import kadri.rizq_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ListingDto> getLatestByCity(String city, int page, int size) {
        log.info("Getting latest listings for city: {}", city);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return listingRepository.findByCity(city, pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<ListingDto> getByCityAndType(String city, ListingType type, int page, int size) {
        log.info("Getting listings for city: {}, type: {}", city, type);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return listingRepository.findByCityAndType(city, type, pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<ListingDto> search(String keyword, String city, int page, int size) {
        log.info("Searching listings in city '{}' with keyword '{}'", city, keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Listing> result;

        if (city != null && !city.isBlank() && keyword != null && !keyword.isBlank()) {
            result = listingRepository.findByCityAndTitleContainingIgnoreCase(city, keyword, pageable);
        } else if (city != null && !city.isBlank()) {
            result = listingRepository.findByCity(city, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            result = listingRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        } else {
            result = listingRepository.findAll(pageable);
        }

        return result.map(this::mapToDto);
    }


    @Override
    public Page<ListingDto> getMyListings(Long userId, int page, int size) {
        log.info("Getting listings for user ID: {}", userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return listingRepository.findByOwnerId(userId, pageable)
                .map(this::mapToDto);
    }

    @Override
    public ListingDto createListing(ListingDto dto, String username) {
        log.info("Creating new listing: {}", dto.title());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Listing listing = Listing.builder()
                .title(dto.title())
                .description(dto.description())
                .city(dto.city())
                .type(dto.type())
                .owner(user)
                .contactInfo(user.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .build();

        listingRepository.save(listing);

        log.info("Listing created by {}", username);
        return mapToDto(listing);
    }
    @Override
    public ListingDto getByIdForEditing(Long id, String username) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Listing not found"));

        if (!listing.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to edit this listing.");
        }

        return mapToDto(listing);
    }


    @Override
    public ListingDto updateListing(Long id, ListingDto dto, String username) { //user call it to update his service
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        if (!listing.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to update this listing.");
        }

        listing.setTitle(dto.title());
        listing.setDescription(dto.description());
        listing.setCity(dto.city());
        listing.setType(dto.type());


        listingRepository.save(listing);

        log.info("Listing {} updated by user {}", id, username);
        return mapToDto(listing);
    }

    @Override
    public void deleteListing(Long id, String username) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        if (!listing.getOwner().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to delete this listing.");
        }

        listingRepository.delete(listing);
        log.info("Listing {} deleted by user {}", id, username);

    }

    private ListingDto mapToDto(Listing listing) {
        return new ListingDto(
                listing.getId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getCity(),
                listing.getType(),
                listing.getOwner().getFullName(),
                listing.getOwner().getPhoneNumber(),
                listing.getOwner().getEmail(),
                listing.getCreatedAt()
        );
    }
}
