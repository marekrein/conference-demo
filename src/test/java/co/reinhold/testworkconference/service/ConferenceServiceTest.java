package co.reinhold.testworkconference.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.reinhold.testworkconference.dto.ConferenceCreateRequest;
import co.reinhold.testworkconference.dto.ConferenceDto;
import co.reinhold.testworkconference.mapper.ConferenceMapper;
import co.reinhold.testworkconference.model.Conference;
import co.reinhold.testworkconference.model.Participant;
import co.reinhold.testworkconference.repository.ConferenceRepository;

@ExtendWith(MockitoExtension.class)
class ConferenceServiceTest {

    @InjectMocks private ConferenceService subject;
    @Mock private ConferenceMapper conferenceMapper;
    @Mock private ParticipantService participantService;
    @Mock private ConferenceRepository conferenceRepository;

    private Conference conference;
    private ConferenceDto conferenceDto;
    private ConferenceCreateRequest conferenceCreateRequest;

    @BeforeEach
    void setUp() {
        conferenceCreateRequest = new ConferenceCreateRequest(
                "Tech Conference", 100, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        conference = Conference.builder()
                .name("Tech Conference")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .roomCapacity(100)
                .build();
        conferenceDto = new ConferenceDto("Tech Conference", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
    }

    @Test
    void shouldCreateConference() {
        when(conferenceMapper.toEntity(conferenceCreateRequest)).thenReturn(conference);
        when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);
        when(conferenceMapper.toDto(any(Conference.class))).thenReturn(conferenceDto);

        ConferenceDto result = subject.createConference(conferenceCreateRequest);

        assertThat(result)
                .isNotNull()
                .extracting("name", "startDate", "endDate")
                .containsExactly(conferenceDto.name(), conferenceDto.startDate(), conferenceDto.endDate());

        verify(conferenceMapper).toEntity(conferenceCreateRequest);
        verify(conferenceRepository).save(conference);
        verify(conferenceMapper).toDto(conference);
    }

    @Test
    void shouldCancelConference() {
        Long conferenceId = 1L;

        var conference = new Conference()
                .setId(conferenceId)
                .setActive(true);

        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
        when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);

        subject.cancelConference(conferenceId);

        verify(conferenceRepository, times(1)).findById(conferenceId);
        verify(conferenceRepository, times(1)).save(conference);
        assertThat(conference.isActive()).isFalse();
    }

    @Test
    void shouldReturnNotRoomAvailable() {
        Long conferenceId = 1L;
        Set<Participant> participants = Set.of(new Participant().setPersonalCode("12345678901"), new Participant().setPersonalCode("09871234567"));
        var conference = new Conference()
                .setId(conferenceId)
                .setRoomCapacity(1)
                .setParticipants(participants);

        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));

        boolean isAvailable = subject.isRoomAvailable(conferenceId);

        verify(conferenceRepository, times(1)).findById(conferenceId);
        assertThat(isAvailable).isFalse();
    }

    @Test
    void shouldReturnRoomAvailable() {
        Long conferenceId = 1L;
        Set<Participant> participants = Set.of(new Participant().setPersonalCode("12345678901"));
        var conference = new Conference()
                .setId(conferenceId)
                .setRoomCapacity(2)
                .setParticipants(participants);

        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));

        boolean isAvailable = subject.isRoomAvailable(conferenceId);

        verify(conferenceRepository, times(1)).findById(conferenceId);
        assertThat(isAvailable).isTrue();
    }

    @Test
    void shouldAddParticipantToConference() {
        Long conferenceId = 1L;
        Long participantId = 2L;

        var conference = new Conference().setId(conferenceId);
        var participant = new Participant().setId(participantId);

        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
        when(participantService.findParticipantById(participantId)).thenReturn(participant);

        subject.addParticipantToConference(conferenceId, participantId);

        verify(conferenceRepository, times(1)).findById(conferenceId);
        verify(participantService, times(1)).findParticipantById(participantId);

        assertThat(conference.getParticipants()).contains(participant);
    }

    @Test
    void shouldRemoveParticipantFromConference() {
        Long conferenceId = 1L;
        Long participantId = 2L;

        var conference = new Conference().setId(conferenceId);
        var participant = new Participant().setId(participantId);

        conference.addParticipant(participant);

        when(conferenceRepository.findById(conferenceId)).thenReturn(Optional.of(conference));
        when(participantService.findParticipantById(participantId)).thenReturn(participant);

        subject.removeParticipantFromConference(conferenceId, participantId);

        verify(conferenceRepository, times(1)).findById(conferenceId);
        verify(participantService, times(1)).findParticipantById(participantId);

        assertThat(conference.getParticipants()).doesNotContain(participant);
    }

}