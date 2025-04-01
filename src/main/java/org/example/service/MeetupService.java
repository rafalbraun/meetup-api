package org.example.service;

import org.example.dto.UserDto;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.MeetupMapper;
import org.example.mapper.UserMapper;
import org.example.model.Location;
import org.example.model.Meetup;
import org.example.dto.MeetupDto;
import org.example.model.User;
import org.example.repository.GroupRepository;
import org.example.repository.LocationRepository;
import org.example.repository.MeetupRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.exceptions.ExceptionMessages.*;

@Service
public class MeetupService {

    private final LocationRepository locationRepository;
    private final MeetupRepository meetupRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public MeetupService(MeetupRepository meetupRepository, LocationRepository locationRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.meetupRepository = meetupRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<Meetup> getMeetupsById(Long id) throws ResourceNotFoundException {
        Location location = locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, id));
        return meetupRepository.findByLocation(location);
    }

    public MeetupDto createMeetup(MeetupDto dto) throws ResourceNotFoundException {
        Meetup meetup = MeetupMapper.toEntity(dto);

        meetup.setLocation(locationRepository
                .findById(dto.getLocation().getId())
                .orElseThrow(() -> new ResourceNotFoundException(LOCATION_NOT_FOUND, dto.getLocation().getId())));

        meetup.setOrganizer(userRepository
                .findById(dto.getOrganizer().getId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, dto.getOrganizer().getId())));

        meetup.setGroup(groupRepository
                .findById(dto.getGroup().getId())
                .orElseThrow(() -> new ResourceNotFoundException(GROUP_NOT_FOUND, dto.getGroup().getId())));

        meetup = meetupRepository.save(meetup);
        return MeetupMapper.toDto(meetup);
    }

    public MeetupDto updateMeetup(Long id, MeetupDto dto) throws ResourceNotFoundException {
        Meetup meetup = meetupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, id));

        meetup.setTitle(dto.getTitle());
        meetup.setDateTime(dto.getDateTime());
        meetup.setDescription(dto.getDescription());
        meetup.setMaxAttendees(dto.getMaxAttendees());

        Long locId = dto.getLocation().getId();
        Location location = locationRepository.findById(locId)
                .orElseThrow(() -> new ResourceNotFoundException(LOCATION_NOT_FOUND, locId));
        meetup.setLocation(location);

        Long orgId = dto.getOrganizer().getId();
        User organizer = userRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, orgId));
        meetup.setOrganizer(organizer);

        Meetup saved = meetupRepository.save(meetup);
        return MeetupMapper.toDto(saved);
    }

    public void deleteMeetup(Long id) throws ResourceNotFoundException {
        Meetup meetup = meetupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, id));

        meetup.setDeletedAt(Instant.now());
        meetupRepository.save(meetup);
    }

    public List<MeetupDto> getMeetupsByLocationId(Long locationId) throws ResourceNotFoundException {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException(LOCATION_NOT_FOUND, locationId);
        }
        List<Meetup> meetups = meetupRepository.findByLocationId(locationId);
        return meetups.stream()
                .map(MeetupMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getMeetupAttendees(Long id) throws ResourceNotFoundException {
        Meetup meetup = meetupRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(MEETUP_NOT_FOUND, id));
        return meetup.getAttendees().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }


}
