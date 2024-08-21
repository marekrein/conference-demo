package co.reinhold.testworkconference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.reinhold.testworkconference.model.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {}
