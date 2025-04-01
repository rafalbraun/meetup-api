# Meetup Management API

## Overview
This API provides endpoints for managing meetups, user attendance, and group memberships.

## Business Context
This API facilitates the organization and management of meetups within user-created groups. It allows users to join, leave, and participate in events while maintaining structured access control.

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

