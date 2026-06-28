package com.myflow.my_flow.services.room;

import com.myflow.my_flow.constants.RoomConstants;
import com.myflow.my_flow.dto.requests.room.RequestJoinRoomDTO;
import com.myflow.my_flow.dto.responses.room.CreateRoomDTO;
import com.myflow.my_flow.dto.responses.room.JoinRoomDTO;
import com.myflow.my_flow.exceptions.room.RoomExpiredException;
import com.myflow.my_flow.exceptions.room.RoomNotFoundException;
import com.myflow.my_flow.models.Room;
import com.myflow.my_flow.models.User;
import com.myflow.my_flow.repository.RoomRepository;
import com.myflow.my_flow.services.auth.JwtService;
import com.myflow.my_flow.types.RoomDuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class RoomService {
  private final JwtService jwtService;
  private final RoomRepository roomRepository;

  private final Random random = new Random();

  public RoomService(
      RoomRepository roomRepository,
      JwtService jwtService
  ) {
    this.jwtService = jwtService;
    this.roomRepository = roomRepository;
  }

  public CreateRoomDTO createRoom(RoomDuration duration) {
    Instant now = Instant.now();

    Room room = Room.builder()
        .roomId(generateUniqueRoomId())
        .expiresAt(now.plusSeconds(this.getSecondsFromDuration(duration)))
        .lastActivity(now)
        .build();

    roomRepository.save(room);

    return CreateRoomDTO.builder().roomId(room.getRoomId()).lastActivity(room.getLastActivity()).build();
  }

  public JoinRoomDTO joinRoom(String roomId, RequestJoinRoomDTO requestJoinRoomDTO) throws RuntimeException {
    Room room = this.roomRepository.findByRoomId(roomId).orElse(null);

    if (room == null) {
      throw new RoomNotFoundException();
    }

    if (room.getExpiresAt().isBefore(Instant.now())) {
      throw new RoomExpiredException();
    }

    room.setLastActivity(Instant.now());
    this.roomRepository.save(room);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    JoinRoomDTO dto = new JoinRoomDTO();
    if (
        auth != null &&
        auth.isAuthenticated() &&
        auth.getPrincipal() instanceof User user
    ) {
      dto.setDisplayName(user.getName());
      dto.setParticipantId(user.getId().toString());
    } else {
      dto.setDisplayName(requestJoinRoomDTO.getName());
      dto.setParticipantId(UUID.randomUUID().toString());
    }

    Duration tokenDuration = Duration.between(Instant.now(), room.getExpiresAt());
    String wsToken = this.jwtService.generateWsToken(
        room.getRoomId(),
        dto.getParticipantId(),
        dto.getDisplayName(),
        tokenDuration
    );

    return dto.setWsToken(wsToken);
  }

  // TODO: Find a better way to generate Unique RoomID
  private String generateUniqueRoomId() {
    String code;
    Optional<Room> existingRoom;
    do {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < RoomConstants.ROOM_CODE_LENGTH; i++) {
        sb.append(RoomConstants.CHARACTERS.charAt(random.nextInt(RoomConstants.CHARACTERS.length())));
      }
      code = sb.toString();
      existingRoom = roomRepository.findByRoomId(code);
    } while (existingRoom.isPresent());

    return code;
  }

  private int getSecondsFromDuration(RoomDuration duration) {
    return switch (duration) {
      case RoomDuration.ONE_HOUR -> 3600;
      case RoomDuration.HALF_HOUR -> 1800;
      case RoomDuration.THREE_HOUR -> 10800;
    };
  }
}
