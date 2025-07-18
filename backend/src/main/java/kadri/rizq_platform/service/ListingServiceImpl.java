package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.Listing;
import kadri.rizq_platform.entity.ListingType;
import kadri.rizq_platform.repository.ListingRepository;
import kadri.rizq_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        return listingRepository.findByCityAndTitleContainingIgnoreCase(city, keyword, pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<ListingDto> getMyListings(Long userId, int page, int size) {
        log.info("Getting listings for user ID: {}", userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return listingRepository.findByOwnerId(userId, pageable)
                .map(this::mapToDto);
    }

    @Override
    public ListingDto createListing(Listing listing) {
        log.info("Creating new listing: {}", listing.getTitle());
        return mapToDto(listingRepository.save(listing));
    }

    private ListingDto mapToDto(Listing listing) {
        return new ListingDto(
                listing.getId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getCity(),
                listing.getType(),
                listing.getContactInfo(),
                listing.getOwner().getFullName(),
                listing.getCreatedAt()
        );
    }
}
