package org.example.service;

import org.example.dto.GroupDto;
import org.example.dto.LocationDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.ValidationException;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.example.controllers.Utils.*;

import org.example.model.Group;
import org.example.model.Meetup;
import org.example.repository.GroupRepository;
import org.example.repository.MeetupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    MeetupService meetupService;

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    MeetupRepository meetupRepository;

    @BeforeEach
    void setup() {}

    @Test
    void shouldCreateMeetup() throws ValidationException, ResourceNotFoundException {
        UserDto userDto = new UserDto();
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto = userService.registerNewUser(userDto);

        LocationDto locationDto = new LocationDto();
        locationDto.setAddress(LOCATION_ADDRESS);
        locationDto.setCity(LOCATION_CITY);
        locationDto = locationService.createLocation(locationDto);

        GroupDto groupDto = new GroupDto();
        groupDto.setName(GROUP_NAME);
        groupDto.setOwner(userDto);
        groupDto = groupService.createGroup(groupDto);

        List<UserDto> attendees = List.of(userDto);
        MeetupDto meetupDto = new MeetupDto();
        meetupDto.setTitle(MEETUP_TITLE_1);
        meetupDto.setDateTime(Instant.now());
        meetupDto.setLocation(locationDto);
        meetupDto.setOrganizer(userDto);
        meetupDto.setGroup(groupDto);
        meetupDto.setAttendees(attendees);
        meetupDto = meetupService.createMeetup(meetupDto);

        assertEquals(1, meetupDto.getAttendees().size());
        assertEquals(groupDto, meetupService.getMeetup(meetupDto.getId()).getGroup());
        assertEquals(List.of(meetupDto), groupService.getGroup(groupDto.getId()).getMeetups());
    }



}
