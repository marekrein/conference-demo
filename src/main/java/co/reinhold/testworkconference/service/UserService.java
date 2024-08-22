package co.reinhold.testworkconference.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.reinhold.testworkconference.model.ApplicationRole;
import co.reinhold.testworkconference.model.ApplicationUser;
import co.reinhold.testworkconference.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ApplicationUser not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(applicationUser.getUsername())
                .password(applicationUser.getPassword())
                .roles(applicationUser.getAuthorities().stream()
                        .map(ApplicationRole::getAuthority).distinct().toArray(String[]::new))
                .build();
    }

}