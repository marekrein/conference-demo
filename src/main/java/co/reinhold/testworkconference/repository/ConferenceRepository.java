package co.reinhold.testworkconference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.reinhold.testworkconference.model.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {}
