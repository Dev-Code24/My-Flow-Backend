package com.myflow.my_flow.services.room;

import com.myflow.my_flow.constants.RoomConstants;
import com.myflow.my_flow.dto.responses.room.RoomDTO;
import com.myflow.my_flow.exceptions.room.RoomExpiredException;
import com.myflow.my_flow.exceptions.room.RoomNotFoundException;
import com.myflow.my_flow.models.Room;
import com.myflow.my_flow.repository.RoomRepository;
import com.myflow.my_flow.types.RoomDuration;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class RoomService {
  private final RoomRepository roomRepository;

  private final Random random = new Random();

  public RoomService(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public RoomDTO createRoom(RoomDuration duration) {
    Instant now = Instant.now();

    Room room = Room.builder()
        .roomId(generateUniqueRoomId())
        .expiresAt(now.plusSeconds(this.getSecondsFromDuration(duration)))
        .lastActivity(now)
        .build();

    roomRepository.save(room);

    return RoomDTO.builder().roomId(room.getRoomId()).lastActivity(room.getLastActivity()).build();
  }

  public void joinRoom(String roomId) {
    Room room = this.roomRepository.findByRoomId(roomId).orElse(null);

    if (room == null) {
      throw new RoomNotFoundException();
    }

    if (room.getExpiresAt().isBefore(Instant.now())) {
      throw new RoomExpiredException();
    }

    room.setLastActivity(Instant.now());

    this.roomRepository.save(room);
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
