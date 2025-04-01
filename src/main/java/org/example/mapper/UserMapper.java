package org.example.mapper;

import org.example.dto.GroupDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.model.Group;
import org.example.model.Meetup;
import org.example.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRoles(user.getRoles());
        dto.setGroups(user.getGroups().stream().map(UserMapper::toGroupDto).collect(Collectors.toList()));
        dto.setOwnedGroups(user.getOwnedGroups().stream().map(UserMapper::toGroupDto).collect(Collectors.toList()));
        dto.setJoinedMeetups(user.getJoinedMeetups().stream().map(UserMapper::toMeetupDto).collect(Collectors.toList()));
        dto.setOrganizedMeetups(user.getOrganizedMeetups().stream().map(UserMapper::toMeetupDto).collect(Collectors.toList()));
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User meetup = new User();
        meetup.setId(dto.getId());
        meetup.setUsername(dto.getUsername());
        meetup.setEmail(dto.getEmail());
        meetup.setBio(dto.getBio());
        meetup.setAvatarUrl(dto.getAvatarUrl());
        meetup.setRoles(dto.getRoles());
        meetup.setGroups(dto.getGroups().stream().map(UserMapper::toGroupEntity).collect(Collectors.toList()));
        meetup.setOwnedGroups(dto.getOwnedGroups().stream().map(UserMapper::toGroupEntity).collect(Collectors.toList()));
        meetup.setJoinedMeetups(dto.getJoinedMeetups().stream().map(UserMapper::toMeetupEntity).collect(Collectors.toList()));
        meetup.setOrganizedMeetups(dto.getOrganizedMeetups().stream().map(UserMapper::toMeetupEntity).collect(Collectors.toList()));
        return meetup;
    }

    public static GroupDto toGroupDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        return dto;
    }

    public static Group toGroupEntity(GroupDto dto) {
        Group group = new Group();
        group.setId(dto.getId());
        return group;
    }

    public static MeetupDto toMeetupDto(Meetup meetup) {
        MeetupDto dto = new MeetupDto();
        dto.setId(meetup.getId());
        return dto;
    }

    public static Meetup toMeetupEntity(MeetupDto dto) {
        Meetup meetup = new Meetup();
        meetup.setId(dto.getId());
        return meetup;
    }

}
