package org.example.controllers;

import org.example.Constants;
import org.example.dto.GroupDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.Constants.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(Constants.UPDATE_USER)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto requestDto) throws ResourceNotFoundException {
        UserDto updatedUser = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping(Constants.GET_USER_BY_USERNAME)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserbyUsername(username).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


//    @PutMapping
//    public ResponseEntity<UserDto> blockUser(@PathVariable Long id) {
//        // TODO
//        ResponseEntity.ok();
//    }

    @GetMapping(ATTEND_MEETUP)
    public ResponseEntity<MeetupDto> attendMeetup(@PathVariable Long id) throws ResourceNotFoundException {
        MeetupDto meetupDto = userService.attendMeetup(id);
        return ResponseEntity.ok(meetupDto);
    }

    @GetMapping(UNATTEND_MEETUP)
    public ResponseEntity<MeetupDto> unattendMeetup(@PathVariable Long id) throws ResourceNotFoundException {
        MeetupDto meetupDto = userService.unattendMeetup(id);
        return ResponseEntity.ok(meetupDto);
    }

    @GetMapping(JOIN_GROUP)
    public ResponseEntity<GroupDto> joinGroup(@PathVariable Long id) throws ResourceNotFoundException {
        GroupDto groupDto = userService.joinGroup(id);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping(LEAVE_GROUP)
    public ResponseEntity<GroupDto> leaveGroup(@PathVariable Long id) throws ResourceNotFoundException {
        GroupDto groupDto = userService.leaveGroup(id);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping(JOINED_MEETUPS)
    public ResponseEntity<List<MeetupDto>> userJoinedMeetups() throws ResourceNotFoundException {
        List<MeetupDto> meetups = userService.getUserJoinedMeetups();
        return ResponseEntity.ok(meetups);
    }

    @GetMapping(JOINED_GROUPS)
    public ResponseEntity<List<GroupDto>> userJoinedGroups() throws ResourceNotFoundException {
        List<GroupDto> groups = userService.getUserJoinedGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping(ORGANIZED_MEETUPS)
    public ResponseEntity<List<MeetupDto>> userOrganizedMeetups() throws ResourceNotFoundException {
        List<MeetupDto> meetups = userService.getUserOrganizedMeetups();
        return ResponseEntity.ok(meetups);
    }

    @GetMapping(OWNED_GROUPS)
    public ResponseEntity<List<GroupDto>> userOwnedGroups() throws ResourceNotFoundException {
        List<GroupDto> groups = userService.getUserOwnedGroups();
        return ResponseEntity.ok(groups);
    }

}
