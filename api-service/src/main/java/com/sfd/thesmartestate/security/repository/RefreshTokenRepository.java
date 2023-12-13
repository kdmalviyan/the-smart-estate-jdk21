package com.sfd.thesmartestate.security.repository;

import com.sfd.thesmartestate.security.entities.RefreshToken;
import com.sfd.thesmartestate.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

    RefreshToken findByUser(User user);
}