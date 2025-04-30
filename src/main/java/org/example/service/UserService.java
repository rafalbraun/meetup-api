package org.example.service;

import org.example.dto.GroupDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.ValidationException;
import org.example.mapper.GroupMapper;
import org.example.mapper.MeetupMapper;
import org.example.mapper.UserMapper;
import org.example.model.Group;
import org.example.model.Meetup;
import org.example.model.User;
import org.example.repository.GroupRepository;
import org.example.repository.MeetupRepository;
import org.example.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.exceptions.ExceptionMessages.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MeetupRepository meetupRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, MeetupRepository meetupRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.meetupRepository = meetupRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserbyId(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserbyUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<GroupDto> getGroupsOwnedByUser(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, id));
        return user.getOwnedGroups()
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getGroupsThatUserIsMemberOf(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, id));
        return user.getGroups()
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto registerNewUser(UserDto dto) throws ValidationException {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ValidationException(USERNAME_EXiSTS, dto.getUsername());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException(EMAIL_EXISTS, dto.getEmail());
        }
        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // hash!
        user.setRoles(List.of("ROLE_USER"));
        user = userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public MeetupDto attendMeetup(Long meetupId) throws ResourceNotFoundException {
        Long userId = getCurrentUserId();
        User user = getUserbyId(getCurrentUserId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, userId));
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, meetupId));
        meetup.getAttendees().add(user);
        Meetup saved = meetupRepository.save(meetup);
        return MeetupMapper.toDto(saved);
    }

    public MeetupDto unattendMeetup(Long meetupId) throws ResourceNotFoundException {
        Long userId = getCurrentUserId();
        User user = getUserbyId(getCurrentUserId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, userId));
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, meetupId));
        meetup.getAttendees().remove(user);
        Meetup saved = meetupRepository.save(meetup);
        return MeetupMapper.toDto(saved);
    }

    public GroupDto joinGroup(Long groupId) throws ResourceNotFoundException {
        Long userId = getCurrentUserId();
        User user = getUserbyId(getCurrentUserId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, userId));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, groupId));
        group.getMembers().add(user);
        Group saved = groupRepository.save(group);
        return GroupMapper.toDto(saved);
    }

    public GroupDto leaveGroup(Long groupId) throws ResourceNotFoundException {
        Long userId = getCurrentUserId();
        User user = getUserbyId(getCurrentUserId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, userId));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, groupId));
        group.getMembers().remove(user);
        Group saved = groupRepository.save(group);
        return GroupMapper.toDto(saved);
    }

    public User getCurrentUser() throws ResourceNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USERNAME_NOT_FOUND, username));
    }

    public Long getCurrentUserId() throws ResourceNotFoundException {
        return getCurrentUser().getId();
    }

    public List<MeetupDto> getUserJoinedMeetups() throws ResourceNotFoundException {
        return meetupRepository.findByAttendeesContaining(getCurrentUser())
                .stream()
                .map(MeetupMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getUserJoinedGroups() throws ResourceNotFoundException {
        return groupRepository.findByMembersContaining(getCurrentUser())
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<MeetupDto> getUserOrganizedMeetups() throws ResourceNotFoundException {
        return meetupRepository.findByOrganizer(getCurrentUser())
                .stream()
                .map(MeetupMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getUserOwnedGroups() throws ResourceNotFoundException {
        return groupRepository.findByOwner(getCurrentUser())
                .stream()
                .map(GroupMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long id, UserDto requestDto) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, id));
        user.setBio(requestDto.getBio());
        user.setAvatarUrl(requestDto.getAvatarUrl());
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

}
