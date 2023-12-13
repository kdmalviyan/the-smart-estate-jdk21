package com.sfd.thesmartestate.bookings;

import com.sfd.thesmartestate.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCreatedBy(User user);
}
