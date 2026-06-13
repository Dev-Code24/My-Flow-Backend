package com.myflow.my_flow.controllers;

import com.myflow.my_flow.constants.Messages;
import com.myflow.my_flow.dto.requests.room.RequestCreateRoomDTO;
import com.myflow.my_flow.dto.requests.room.RequestJoinRoomDTO;
import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.dto.responses.room.RoomDTO;
import com.myflow.my_flow.services.room.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/rooms")
@RestController
public class RoomController {
  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("/create")
  public ResponseEntity<BasicResponseDTO<RoomDTO>> createRoom(
      @Valid @RequestBody RequestCreateRoomDTO requestCreateRoomDto
      ) {
    RoomDTO newRoom = this.roomService.createRoom(requestCreateRoomDto.getDuration());

    return ResponseEntity.status(HttpStatus.CREATED).body(
        BasicResponseDTO.<RoomDTO>builder()
            .data(newRoom)
            .message(Messages.SUCCESS.getValue())
            .build()
    );
  }

  @PostMapping("/{roomId}/join")
  public ResponseEntity<BasicResponseDTO<Void>> joinRoom(
      @PathVariable String roomId,
      @Valid @RequestBody RequestJoinRoomDTO requestJoinRoomDTO
  ) {
    this.roomService.joinRoom(roomId);
    return ResponseEntity.ok().body(
        BasicResponseDTO.<Void>builder()
            .message(Messages.SUCCESS.getValue())
            .build()
    );
  }
}
