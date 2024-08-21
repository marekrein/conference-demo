package co.reinhold.testworkconference.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.reinhold.testworkconference.dto.ConferenceCreateRequest;
import co.reinhold.testworkconference.dto.ConferenceDto;
import co.reinhold.testworkconference.service.ConferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public @Valid ConferenceDto createConference(@Valid @RequestBody ConferenceCreateRequest conferenceCreate) {
        return conferenceService.createConference(conferenceCreate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)//todo
    public void cancelConference(@PathVariable("id") Long conferenceId) {
        conferenceService.cancelConference(conferenceId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/check-availability")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public boolean checkRoomAvailability(@PathVariable("id") Long conferenceId) {
        return conferenceService.isRoomAvailable(conferenceId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{conferenceId}/participants/{participantId}")
    public ResponseEntity<String> addParticipant(@PathVariable Long conferenceId, @PathVariable Long participantId) {
        conferenceService.addParticipantToConference(conferenceId, participantId);
        return ResponseEntity.ok("Participant added successfully");//todo
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{conferenceId}/participants/{participantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)//todo
    public ResponseEntity<String> removeParticipant(@PathVariable Long conferenceId, @PathVariable Long participantId) {
        conferenceService.removeParticipantFromConference(conferenceId, participantId);
        return ResponseEntity.ok("Participant removed successfully");
    }

}
