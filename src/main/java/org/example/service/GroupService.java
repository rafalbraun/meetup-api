package org.example.service;

import org.example.dto.MeetupDto;
import org.example.dto.GroupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.GroupMapper;
import org.example.mapper.MeetupMapper;
import org.example.mapper.UserMapper;
import org.example.model.Group;
import org.example.model.Meetup;
import org.example.model.User;
import org.example.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.exceptions.ExceptionMessages.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(GroupMapper::toDto).collect(Collectors.toList());
    }

    public GroupDto getGroup(Long id) throws ResourceNotFoundException {
        Group group = groupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, id));
        return GroupMapper.toDto(group);
    }

    public GroupDto createGroup(GroupDto dto) {
        Group group = GroupMapper.toEntity(dto);
        group = groupRepository.save(group);
        return GroupMapper.toDto(group);
    }

    public GroupDto updateGroup(Long id, GroupDto dto) throws ResourceNotFoundException {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, id));
        group.setName(dto.getName());
        Group saved = groupRepository.save(group);
        return GroupMapper.toDto(saved);
    }

    public void deleteGroup(Long id) throws ResourceNotFoundException {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, id));
        groupRepository.delete(group);
    }

    public List<UserDto> getGroupMembers(Long id) throws ResourceNotFoundException {
        Group group = groupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, id));
        List<User> members = group.getMembers();
        return members.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public List<MeetupDto> getGroupMeetups(Long id) throws ResourceNotFoundException {
        Group group = groupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, id));
        List<Meetup> meetups = group.getMeetups();
        return meetups.stream().map(MeetupMapper::toDto).collect(Collectors.toList());
    }

}
