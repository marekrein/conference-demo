package co.reinhold.testworkconference.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.reinhold.testworkconference.model.ApplicationUser;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByUsername(String username);

}
