package org.example.service;

import org.example.dto.LocationDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.example.controllers.Utils.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    MeetupService meetupService;

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldCreateMeetup() throws ValidationException, ResourceNotFoundException {
        UserDto userDto = new UserDto();
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        UserDto savedUser = userService.registerNewUser(userDto);

        LocationDto locationDto = new LocationDto();
        locationDto.setAddress(LOCATION_ADDRESS);
        locationDto.setCity(LOCATION_CITY);
        LocationDto savedLocationDto = locationService.createLocation(locationDto);

        List<UserDto> attendees = List.of(userDto);
        MeetupDto meetupDto = new MeetupDto();
        meetupDto.setTitle(MEETUP_TITLE);
        meetupDto.setDateTime(Instant.now());
        meetupDto.setLocation(savedLocationDto);
        meetupDto.setOrganizer(savedUser);
        meetupDto.setAttendees(attendees);
        MeetupDto savedMeetup = meetupService.createMeetup(meetupDto);
        assertEquals(1, savedMeetup.getAttendees().size());
    }



}
