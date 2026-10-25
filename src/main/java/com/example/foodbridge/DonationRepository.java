package com.foodbridge.repository;
import com.foodbridge.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DonationRepository extends JpaRepository<Donation,Long> {
    List<Donation> findByRestaurantOrderByCreatedAtDesc(AppUser restaurant);
    List<Donation> findByStatusOrderByExpiryAtAsc(DonationStatus status);
    List<Donation> findByNgoOrderByCreatedAtDesc(AppUser ngo);
    long countByStatus(DonationStatus status);
}
