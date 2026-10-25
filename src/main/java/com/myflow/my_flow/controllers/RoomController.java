package com.myflow.my_flow.controllers;

import com.myflow.my_flow.commons.ParticipantIdentity;
import com.myflow.my_flow.constants.Messages;
import com.myflow.my_flow.constants.RoomDuration;
import com.myflow.my_flow.dto.requests.room.RequestCreateRoomDTO;
import com.myflow.my_flow.dto.requests.room.RequestJoinRoomDTO;
import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.dto.responses.room.CreateRoomDTO;
import com.myflow.my_flow.dto.responses.room.JoinRoomDTO;
import com.myflow.my_flow.services.identity.ParticipantIdentityService;
import com.myflow.my_flow.services.room.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rooms")
@RestController
public class RoomController {
  private final RoomService roomService;
  private final ParticipantIdentityService participantIdentityService;

  public RoomController(
      RoomService roomService,
      ParticipantIdentityService participantIdentityService
  ) {
    this.roomService = roomService;
    this.participantIdentityService = participantIdentityService;
  }

  @PostMapping("/create")
  public ResponseEntity<BasicResponseDTO<CreateRoomDTO>> createRoom(
      @Valid @RequestBody RequestCreateRoomDTO requestCreateRoomDto,
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    ParticipantIdentity guestUserId = this.participantIdentityService.resolveIdentity(request, response);
    RoomDuration roomDuration = requestCreateRoomDto.getDuration();
    CreateRoomDTO newRoom = this.roomService.createRoom(roomDuration, guestUserId);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        BasicResponseDTO.<CreateRoomDTO>builder()
            .data(newRoom)
            .message(Messages.SUCCESS.getValue())
            .build()
    );
  }

  @PostMapping("/{roomId}/join")
  public ResponseEntity<BasicResponseDTO<JoinRoomDTO>> joinRoom(
      @PathVariable String roomId,
      @Valid @RequestBody RequestJoinRoomDTO requestJoinRoomDTO,
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    ParticipantIdentity guestUserId = this.participantIdentityService.resolveIdentity(request, response);
    JoinRoomDTO dto = this.roomService.joinRoom(roomId, requestJoinRoomDTO, guestUserId);

    return ResponseEntity.ok().body(
        BasicResponseDTO.<JoinRoomDTO>builder()
            .message(Messages.SUCCESS.getValue())
            .data(dto)
            .build()
    );
  }
}
