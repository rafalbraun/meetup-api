package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.Comment;
import org.example.model.Group;
import org.example.model.Location;
import org.example.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetupDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title can't exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date and time is required")
    private Instant dateTime;

    //@Min(value = 1, message = "There must be at least 1 attendee")
    private Integer maxAttendees;

    @NotNull(message = "Location is required")
    private LocationDto location;

    @NotNull(message = "Organizer is required")
    private UserDto organizer;

    @NotNull(message = "Group is required")
    private GroupDto group;

    private List<UserDto> attendees = new LinkedList<>();
    private List<CommentDto> comments = new LinkedList<>();

}
