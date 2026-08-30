package com.myflow.my_flow.services.room;

import com.myflow.my_flow.commons.ParticipantIdentity;
import com.myflow.my_flow.constants.RoomConstants;
import com.myflow.my_flow.constants.RoomRole;
import com.myflow.my_flow.dto.requests.room.RequestJoinRoomDTO;
import com.myflow.my_flow.dto.responses.room.CreateRoomDTO;
import com.myflow.my_flow.dto.responses.room.JoinRoomDTO;
import com.myflow.my_flow.exceptions.room.RoomExpiredException;
import com.myflow.my_flow.exceptions.room.RoomNotFoundException;
import com.myflow.my_flow.models.Room;
import com.myflow.my_flow.models.User;
import com.myflow.my_flow.repository.RoomRepository;
import com.myflow.my_flow.services.auth.JwtService;
import com.myflow.my_flow.constants.RoomDuration;
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

  public CreateRoomDTO createRoom(
      RoomDuration duration,
      ParticipantIdentity identity
  ) {
    Instant now = Instant.now();
    Room room = this.roomRepository.findByCreatorId(identity.id()).orElse(null);

    if (room == null) {
      room = Room.builder()
          .roomId(generateUniqueRoomId())
          .creatorId(identity.id())
          .expiresAt(
              now.plusSeconds(
                  RoomServiceUtils.getSecondsFromDuration(duration)
              )
          )
          .lastActivity(now)
          .build();
    } else if (now.isAfter(room.getExpiresAt())) {
      room.setRoomId(generateUniqueRoomId());
      room.setExpiresAt(
          now.plusSeconds(
              RoomServiceUtils.getSecondsFromDuration(duration)
          )
      );
      room.setLastActivity(now);
    } else {
      room.setLastActivity(now);
    }

    room = roomRepository.save(room);

    return CreateRoomDTO.builder()
        .roomId(room.getRoomId())
        .lastActivity(room.getLastActivity())
        .build();
  }

  public JoinRoomDTO joinRoom(
      String roomId,
      RequestJoinRoomDTO requestJoinRoomDTO,
      ParticipantIdentity identity
  ) throws RuntimeException {
    Room room = this.roomRepository.findByRoomId(roomId).orElse(null);
    String displayName = RoomServiceUtils.resolveName(requestJoinRoomDTO, identity);

    if (room == null) {
      throw new RoomNotFoundException();
    }

    if (room.getExpiresAt().isBefore(Instant.now())) {
      throw new RoomExpiredException();
    }

    room.setLastActivity(Instant.now());
    this.roomRepository.save(room);

    JoinRoomDTO dto = new JoinRoomDTO();
    Duration tokenDuration = Duration.between(Instant.now(), room.getExpiresAt());
    String participantId = identity.id().toString();
    String wsToken = this.jwtService.generateWsToken(
        room.getRoomId(),
        participantId,
        displayName,
        tokenDuration
    );
    RoomRole role = room.getCreatorId().equals(identity.id()) ? RoomRole.CREATOR : RoomRole.JOINER;

    dto.setParticipantId(identity.id().toString());
    dto.setDisplayName(displayName);
    dto.setWsToken(wsToken);
    dto.setRole(role);

    return dto;
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
}
