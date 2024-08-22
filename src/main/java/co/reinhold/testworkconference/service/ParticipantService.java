package co.reinhold.testworkconference.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.reinhold.testworkconference.exception.ParticipantNotFoundException;
import co.reinhold.testworkconference.model.Participant;
import co.reinhold.testworkconference.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public Participant findParticipantById(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found with ID: %d".formatted(participantId)));
    }

}
