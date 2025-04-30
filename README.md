# Meetup Management API

## Overview
This API provides endpoints for managing meetups, user attendance, and group memberships.

## Business Context
This API facilitates the organization and management of meetups within user-created groups. It allows users to join, leave, and participate in events while maintaining structured access control.


| Endpoint                           | Business Context                                                                                                                                                                                            |
|------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| create group                       | As a user with account I want to be able to create new group. When I create group I become its owner.                                                                                                       |
| create meetup                      | As a user with account I want to be able to create new meetup within the given group for all members of the group. Only group members can see and join meetup. When I create meetup I become its organizer. |
| update group                       | As an owner of the group I want to be able to modify its name.                                                                                                                                              |
| update meetup                      | As an organizer of the meetup I want to be able to update its title.                                                                                                                                        |
| delete meetup                      | As an organizer of the meetup I want to be able to delete it.                                                                                                                                               |
| delete group                       | As an owner of the group I want to be able to delete it.                                                                                                                                                    |
| join group                         | As a user with account I want to be able to join group.                                                                                                                                                     |
| leave group                        | As a user with account I want to be able to leave group.                                                                                                                                                    |
| attend meetup                      | As a user with account and member of a group I want to be able to attend meetup.                                                                                                                            |
| unattend meetup                    | As a user with account and member of a group and user signed to attend a meeting I want to be able to unattend meetup.                                                                                      |
| get all groups                     | As a user I want to be able to see all groups.                                                                                                                                                              |
| get all meetups                    | As a user I want to be able to see all meetups.                                                                                                                                                             |
| get all meetups in given location  | As a user I want to be able to see all meetups in a given location.                                                                                                                                         |
| get all meetups within given group | As a user I want to be able to see all meetups within a given group.                                                                                                                                        |
| get all group members              | As a user I want to be able to see all group members.                                                                                                                                                       |
| get all meetup attendees           | As a user I want to be able to see all meetup attendees.                                                                                                                                                    |
| get all user's groups              | As a user with account I want to be able to see all my groups (groups I am member of).                                                                                                                      |
| get all user's meetups             | As a user with account I want to be able to see all my meetups (groups I have attended and I'm going to attend).                                                                                            |

### Technicals


| Endpoint                           | Controller         | Endpoint Name        | Test Name                                                           |
|------------------------------------|--------------------|----------------------|---------------------------------------------------------------------|
| create group                       | GroupController    | createGroup          | shouldCreateGroupEndpoint                                           |
| update group                       | GroupController    | updateGroup          | shouldUpdateGroupEndpoint                                           |
| delete group                       | GroupController    | deleteGroup          | shouldRemoveGroupEndpoint                                           |
| get all groups                     | GroupController    | getGroups            |                                                                     |
| get all meetups in given location  | GroupController    | getMeetupsByLocation |                                                                     |
| get all meetups within given group | GroupController    | getGroupMeetups      |                                                                     |
| get all group members              | GroupController    | getGroupMembers      |                                                                     |
| create meetup                      | MeetupController   | createMeetup         | shouldOrganizeMeetingInTheGroupEndpoint, shouldCreateMeetupEndpoint |
| update meetup                      | MeetupController   | updateMeetup         | shouldUpdateMeetupEndpoint                                          |
| delete meetup                      | MeetupController   | deleteMeetup         |                                                                     |
| get all meetups                    | MeetupController   | getMeetups           | shouldRemoveMeetupEndpoint                                          |
| get meetup                         | MeetupController   | getMeetup            | shouldGetMeetupsEndpoint                                            |
| get all meetup attendees           | MeetupController   | getMeetupAttendees   |                                                                     |
| attend meetup                      | UserController     | attendMeetup         | shouldAttendMeetupEndpoint                                          |
| unattend meetup                    | UserController     | unattendMeetup       | shouldAttendMeetupEndpoint                                          |
| join group                         | UserController     | joinGroup            | shouldJoinGroupEndpoint                                             |
| leave group                        | UserController     | leaveGroup           | shouldJoinGroupEndpoint                                             |
| get all user's owned groups        | UserController     | userJoinedGroups     |                                                                     |
| get all user's joined groups       | UserController     | userOwnedGroups      |                                                                     |
| get all user's organized meetups   | UserController     | userOrganizedMeetups |                                                                     |
| get all user's attended meetups    | UserController     | userJoinedMeetups    |                                                                     |
| create location                    | LocationController | createLocation       | shouldCreateLocationEndpoint                                        |

### Key Features:
- **Meetup Management**
  - Users can create and organize meetups within specific groups.
  - Meetups are associated with locations to help attendees find events easily.
  - Users can join or leave meetups dynamically.

- **Group Management**
  - Users can create and own groups where meetups take place.
  - Groups have members, and users can join or leave them at will.

- **User Participation**
  - Users can attend meetups and see a list of attendees.
  - Users can track meetups they have joined or organized.

- **Authentication & Access Control**
  - All endpoints are secured with authentication via Bearer Tokens.
  - Access control ensures only authorized users can manage groups and meetups.

- **Error Handling & API Consistency**
  - Standardized JSON responses for errors.
  - Meaningful HTTP status codes for success and failure cases.

## Endpoints

### Meetup Endpoints
- **Get all meetups in a user's groups**
  ```http
  GET /api/meetups/{userId}/groups/
  ```

- **Get all meetups at a specific location**
  ```http
  GET /api/locations/{locationId}/meetups
  ```

- **Get all attendees of a meetup**
  ```http
  GET /api/meetup/{id}/attendees
  ```

- **Attend a meetup**
  ```http
  GET /attend/{id}
  ```

- **Unattend a meetup**
  ```http
  GET /unattend/{id}
  ```

- **Get meetups a user has joined**
  ```http
  GET /api/meetups/joined
  ```

- **Get meetups a user has organized**
  ```http
  GET /api/meetups/organized
  ```

### Group Endpoints
- **Join a group**
  ```http
  GET /join/{id}
  ```

- **Leave a group**
  ```http
  GET /leave/{id}
  ```

- **Get groups a user has joined**
  ```http
  GET /api/groups/joined
  ```

- **Get groups a user owns**
  ```http
  GET /api/groups/owned
  ```

- **Get members of a group**
  ```http
  GET /api/members/{groupId}
  ```

## Authentication
All endpoints require authentication using Bearer Tokens.

## Response Format
All responses follow JSON format with appropriate HTTP status codes.

## Error Handling
Errors are returned as JSON objects with `message` and `statusCode` fields.

