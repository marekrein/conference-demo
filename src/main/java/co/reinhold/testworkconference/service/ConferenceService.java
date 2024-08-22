package co.reinhold.testworkconference.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.reinhold.testworkconference.dto.ConferenceCreateRequest;
import co.reinhold.testworkconference.dto.ConferenceDto;
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
        log.info("Delete conference - id: {}", conferenceId);
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new IllegalArgumentException("Conference not found"));
        conference.setActive(false);
        Conference updatedConference = conferenceRepository.save(conference);
        log.info("Conference deleted - id: {} | isActive: {}", updatedConference.getId(), updatedConference.isActive());
    }

    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(() -> new IllegalArgumentException("Conference not found"));
        return conference.isRoomAvailable();
    }

    @Transactional
    public void addParticipantToConference(Long conferenceId, Long participantId) {
        Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(() -> new IllegalArgumentException("Conference not found"));
        Participant participant = participantService.findParticipantById(participantId);
        conference.addParticipant(participant);
    }

    @Transactional
    public void removeParticipantFromConference(Long conferenceId, Long participantId) {
        Conference conference = conferenceRepository.findById(conferenceId).orElseThrow(() -> new IllegalArgumentException("Conference not found"));
        Participant participant = participantService.findParticipantById(participantId);
        conference.removeParticipant(participant);
    }

}
