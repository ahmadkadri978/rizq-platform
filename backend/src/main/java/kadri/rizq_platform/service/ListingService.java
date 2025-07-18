package kadri.rizq_platform.service;

import kadri.rizq_platform.dto.ListingDto;
import kadri.rizq_platform.entity.Listing;
import kadri.rizq_platform.entity.ListingType;
import org.springframework.data.domain.Page;

public interface ListingService {
    Page<ListingDto> getLatestByCity(String city, int page, int size);
    Page<ListingDto> getByCityAndType(String city, ListingType type, int page, int size);
    Page<ListingDto> search(String keyword, String city, int page, int size);
    Page<ListingDto> getMyListings(Long userId, int page, int size);
    ListingDto createListing(Listing listing);
}
