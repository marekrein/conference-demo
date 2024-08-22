package co.reinhold.testworkconference.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.reinhold.testworkconference.dto.ConferenceCreateRequest;
import co.reinhold.testworkconference.dto.ConferenceDto;
import co.reinhold.testworkconference.exception.ConferenceNotFoundException;
import co.reinhold.testworkconference.mapper.ConferenceMapper;
import co.reinhold.testworkconference.model.Conference;
import co.reinhold.testworkconference.model.Participant;
import co.reinhold.testworkconference.repository.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConferenceService {

    private final ConferenceMapper conferenceMapper;
    private final ParticipantService participantService;
    private final ConferenceRepository conferenceRepository;

    @Transactional
    public ConferenceDto createConference(ConferenceCreateRequest conferenceCreate) {
        log.info("Create conference: {}", conferenceCreate);
        Conference newConference = conferenceMapper.toEntity(conferenceCreate);
        Conference savedConference = conferenceRepository.save(newConference);
        log.info("Conference saved - id: {}", savedConference.getId());
        return conferenceMapper.toDto(savedConference);
    }

    @Transactional
    public void cancelConference(Long conferenceId) {
        log.info("Delete conference: {}", conferenceId);
        Conference conference = getConference(conferenceId);
        conference.setActive(false);
        Conference updatedConference = conferenceRepository.save(conference);
        log.info("Conference deleted: {}", updatedConference.getId());
    }

    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Long conferenceId) {
        Conference conference = getConference(conferenceId);
        return conference.isRoomAvailable();
    }

    @Transactional
    public void addParticipantToConference(Long conferenceId, Long participantId) {
        log.info("Add participant: {} to conference: {}", participantId, conferenceId);
        Conference conference = getConference(conferenceId);
        Participant participant = getParticipant(participantId);
        conference.addParticipant(participant);
        log.info("Participant: {} added to conference: {}", participantId, conferenceId);
    }

    @Transactional
    public void removeParticipantFromConference(Long conferenceId, Long participantId) {
        log.info("Remove participant: {} from conference: {}", participantId, conferenceId);
        Conference conference = getConference(conferenceId);
        Participant participant = getParticipant(participantId);
        conference.removeParticipant(participant);
        log.info("Participant: {} removed from conference: {}", participantId, conferenceId);
    }

    private Participant getParticipant(Long participantId) {
        return participantService.findParticipantById(participantId);
    }

    private Conference getConference(Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with ID: %d".formatted(conferenceId)));
    }

}
