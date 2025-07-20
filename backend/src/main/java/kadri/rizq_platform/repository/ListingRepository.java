package kadri.rizq_platform.repository;

import kadri.rizq_platform.entity.ListingType;
import kadri.rizq_platform.entity.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    Page<Listing> findByCity(String city, Pageable pageable);

    Page<Listing> findByCityAndType(String city, ListingType type, Pageable pageable);

    Page<Listing> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Listing> findByCityAndTitleContainingIgnoreCase(String city, String keyword, Pageable pageable);

    Page<Listing> findByOwnerId(Long ownerId, Pageable pageable);


}
