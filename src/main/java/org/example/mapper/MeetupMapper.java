package org.example.mapper;

import org.example.dto.GroupDto;
import org.example.dto.UserDto;
import org.example.dto.MeetupDto;
import org.example.model.Group;
import org.example.model.Meetup;
import org.example.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MeetupMapper {

    public MeetupMapper() {}

    public static MeetupDto toDto(Meetup meetup) {
        MeetupDto dto = new MeetupDto();
        dto.setId(meetup.getId());
        dto.setTitle(meetup.getTitle());
        dto.setDescription(meetup.getDescription());
        dto.setDateTime(meetup.getDateTime());
        dto.setMaxAttendees(meetup.getMaxAttendees());
        dto.setLocation(LocationMapper.toDto(meetup.getLocation()));
        dto.setOrganizer(toUserDto(meetup.getOrganizer()));
        dto.setGroup(toGroupDto(meetup.getGroup()));
        dto.setAttendees(meetup.getAttendees().stream().map(MeetupMapper::toUserDto).collect(Collectors.toList()));
        //dto.setComments(meetup.getComments().stream().map(CommentMapper::toDto).collect(Collectors.toList()));
        return dto;
    }

    public static Meetup toEntity(MeetupDto dto) {
        Meetup meetup = new Meetup();
        meetup.setTitle(dto.getTitle());
        meetup.setDescription(dto.getDescription());
        meetup.setDateTime(dto.getDateTime());
        meetup.setMaxAttendees(dto.getMaxAttendees());
        meetup.setLocation(LocationMapper.toEntity(dto.getLocation()));
        meetup.setOrganizer(UserMapper.toEntity(dto.getOrganizer()));
        meetup.setAttendees(dto.getAttendees().stream().map(MeetupMapper::toUserEntity).collect(Collectors.toList()));
        //meetup.setComments(dto.getComments().stream().map(CommentMapper::toEntity).collect(Collectors.toList()));
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

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public static User toUserEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        return user;
    }

}