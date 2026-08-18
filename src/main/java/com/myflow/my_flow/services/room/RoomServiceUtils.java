package com.myflow.my_flow.services.room;

import com.myflow.my_flow.commons.ParticipantIdentity;
import com.myflow.my_flow.constants.RoomConstants;
import com.myflow.my_flow.constants.RoomDuration;
import com.myflow.my_flow.dto.requests.room.RequestJoinRoomDTO;
import com.myflow.my_flow.exceptions.room.DisplayNameIncorrectLengthException;
import com.myflow.my_flow.exceptions.room.DisplayNameNotFoundException;
import com.myflow.my_flow.models.Room;

import java.util.Optional;

public class RoomServiceUtils {
  public static int getSecondsFromDuration(RoomDuration duration) {
    return switch (duration) {
      case RoomDuration.ONE_HOUR -> 3600;
      case RoomDuration.HALF_HOUR -> 1800;
      case RoomDuration.THREE_HOURS -> 10800;
    };
  }

  public static String resolveName(
      RequestJoinRoomDTO requestJoinRoomDTO,
      ParticipantIdentity identity
  ) {
    if (identity.isAuthenticated()) {
      return identity.displayName();
    }

    String name = requestJoinRoomDTO.getName();

    if (name == null) {
      throw new DisplayNameNotFoundException();
    }

    String trimmedName = name.trim();

    if (trimmedName.length() < 2 || trimmedName.length() > 50) {
      throw new DisplayNameIncorrectLengthException();
    }

    return trimmedName;
  }
}
