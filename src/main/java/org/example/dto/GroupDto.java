package org.example.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class GroupDto {
    private Long id;
    private String name;
    private Long ownerId;
    private UserDto owner;
    private List<UserDto> members = new LinkedList<>();
    private List<MeetupDto> meetups = new LinkedList<>();
}
