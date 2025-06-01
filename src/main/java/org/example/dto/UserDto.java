package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Group;
import org.example.model.Meetup;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username can only contain letters, digits, and underscores"
    )
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    private String password;

    //@NotBlank(message = "Email cannot be blank")
    //@Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$",
            message = "Email format is invalid"
    )
    private String email;

    private String bio;

    private String avatarUrl;

    private List<String> roles;
    
    private List<MeetupDto> organizedMeetups = new LinkedList<>();

    private List<MeetupDto> joinedMeetups = new LinkedList<>();

    private List<GroupDto> ownedGroups = new LinkedList<>();

    private List<GroupDto> groups = new LinkedList<>();

}
