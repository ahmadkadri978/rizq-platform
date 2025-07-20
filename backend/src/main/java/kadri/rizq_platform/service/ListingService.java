package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.Listing;
import kadri.rizq_platform.entity.ListingType;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface ListingService {
    Page<ListingDto> getLatestByCity(String city, int page, int size);
    Page<ListingDto> getByCityAndType(String city, ListingType type, int page, int size);
    Page<ListingDto> search(String keyword, String city, int page, int size);
    Page<ListingDto> getMyListings(Long userId, int page, int size);
    ListingDto createListing(ListingDto dto, String username);
    ListingDto getByIdForEditing(Long id, String username);
     ListingDto updateListing(Long id, ListingDto dto, String username);
    void deleteListing(Long id, String username);
}
