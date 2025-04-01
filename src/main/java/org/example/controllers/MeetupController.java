package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.MeetupMapper;
import org.example.model.Meetup;
import org.example.repository.MeetupRepository;
import org.example.service.MeetupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.Constants.*;
import static org.example.exceptions.ExceptionMessages.MEETUP_NOT_FOUND;

@RestController
public class MeetupController {

    private final MeetupService meetupService;
    private final MeetupRepository meetupRepository;

    public MeetupController(MeetupService meetupService, MeetupRepository meetupRepository) {
        this.meetupService = meetupService;
        this.meetupRepository = meetupRepository;
    }

    @GetMapping(GET_MEETUPS)
    public ResponseEntity<List<MeetupDto>> getMeetups() {
        List<MeetupDto> meetups = meetupRepository.findAll().stream().map(MeetupMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(meetups);
    }

    @PostMapping(CREATE_MEETUP)
    public ResponseEntity<MeetupDto> createMeetup(@Valid @RequestBody MeetupDto request) throws ResourceNotFoundException {
        MeetupDto saved = meetupService.createMeetup(request);
        URI meetupUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(meetupUri).body(saved);
    }

    @PutMapping(UPDATE_MEETUP)
    public ResponseEntity<MeetupDto> updateMeetup(@PathVariable Long id, @RequestBody MeetupDto requestDto) throws ResourceNotFoundException {
        MeetupDto updatedMeetup = meetupService.updateMeetup(id, requestDto);
        return ResponseEntity.ok(updatedMeetup);
    }

    @DeleteMapping(DELETE_MEETUP)
    public ResponseEntity<Void> deleteMeetup(@PathVariable Long id) throws ResourceNotFoundException {
        meetupService.deleteMeetup(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping(GET_MEETUP)
    public ResponseEntity<Meetup> getMeetup(@PathVariable Long id) throws ResourceNotFoundException {
        Meetup meetup = meetupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND));
        return ResponseEntity.ok(meetup);
    }

    //------------------------------------------------------

    @GetMapping(ALL_MEETUPS_IN_LOCATION)
    public ResponseEntity<List<MeetupDto>> getMeetupsByLocation(@PathVariable Long locationId) throws ResourceNotFoundException {
        List<MeetupDto> meetups = meetupService.getMeetupsByLocationId(locationId);
        return ResponseEntity.ok(meetups);
    }

    @GetMapping(ALL_MEETUP_ATTENDEES)
    public ResponseEntity<List<UserDto>> getMeetupAttendees(@PathVariable Long id) throws ResourceNotFoundException {
        List<UserDto> attendees = meetupService.getMeetupAttendees(id);
        return ResponseEntity.ok(attendees);
    }

}
