package org.example.controllers;

import org.example.dto.GroupDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.service.GroupService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.example.Constants.*;

@RestController
public class GroupController {

    private final UserService userService;
    private final GroupService groupService;

    public GroupController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping(GET_GROUPS)
    public ResponseEntity<List<GroupDto>> getGroups() {
        List<GroupDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping(GET_GROUP)
    public ResponseEntity<GroupDto> getGroup(Long id) throws ResourceNotFoundException {
        GroupDto group = groupService.getGroup(id);
        return ResponseEntity.ok().body(group);
    }

    @PostMapping(CREATE_GROUP)
    public ResponseEntity<GroupDto> createGroup(GroupDto request) throws ResourceNotFoundException {
        GroupDto saved = groupService.createGroup(request);
        URI groupUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(groupUri).body(saved);
    }

    @PutMapping(UPDATE_GROUP)
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Long id, @RequestBody GroupDto requestDto) throws ResourceNotFoundException {
        GroupDto updatedGroup = groupService.updateGroup(id, requestDto);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping(DELETE_GROUP)
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) throws ResourceNotFoundException {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    //------------------------------------------------------

    @GetMapping(GROUP_MEMBERS)
    public ResponseEntity<List<UserDto>> getGroupMembers(@PathVariable Long groupId) throws ResourceNotFoundException {
        List<UserDto> members = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok().body(members);
    }

    @GetMapping(MEETUPS_IN_THE_GROUP)
    public ResponseEntity<List<MeetupDto>> getGroupMeetups(@PathVariable Long groupId) throws ResourceNotFoundException {
        List<MeetupDto> meetups = groupService.getGroupMeetups(groupId);
        return ResponseEntity.ok().body(meetups);
    }

}
