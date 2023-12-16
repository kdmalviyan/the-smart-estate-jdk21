package com.sfd.thesmartestate.users.services;

import com.sfd.thesmartestate.users.entities.Employee;
import com.sfd.thesmartestate.users.entities.LoginDetails;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author kuldeep
 */

@Service
@Data
@Slf4j
public class LoginDetailsService implements UserDetailsService {
    private final LoginDetailsRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found"));
    }

    public LoginDetails findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    public LoginDetails update(LoginDetails loginDetails) {
        return repository.save(loginDetails);
    }

    public LoginDetails findLoggedInUser() {
        try {
            return (LoginDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error("Unable to find logged in user");
        }
        return null;
    }
}
