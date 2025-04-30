package org.example.mapper;

import org.example.dto.GroupDto;
import org.example.dto.MeetupDto;
import org.example.dto.UserDto;
import org.example.model.Group;
import org.example.model.Meetup;
import org.example.model.User;

import java.util.stream.Collectors;

public class GroupMapper {

    public static GroupDto toDto(Group group) {
        if (group == null) return null;
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setOwner(GroupMapper.toUserDto(group.getOwner()));
        dto.setMembers(group.getMembers().stream().map(GroupMapper::toUserDto).collect(Collectors.toList()));
        dto.setMeetups(group.getMeetups().stream().map(MeetupMapper::toDto).collect(Collectors.toList()));
        return dto;
    }

    public static Group toEntity(GroupDto dto) {
        if (dto == null) return null;
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        group.setOwner(GroupMapper.toUserEntity(dto.getOwner()));
        group.setMembers(dto.getMembers().stream().map(GroupMapper::toUserEntity).collect(Collectors.toList()));
        group.setMeetups(dto.getMeetups().stream().map(MeetupMapper::toEntity).collect(Collectors.toList()));
        return group;
    }

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public static User toUserEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        return user;
    }

    public static MeetupDto toMeetupDto(Meetup meetup) {
        if (meetup == null) {
            return null;
        }
        MeetupDto dto = new MeetupDto();
        dto.setId(meetup.getId());
        return dto;
    }

    public static Meetup toMeetupEntity(MeetupDto dto) {
        if (dto == null) {
            return null;
        }
        Meetup meetup = new Meetup();
        meetup.setId(dto.getId());
        return meetup;
    }


}
