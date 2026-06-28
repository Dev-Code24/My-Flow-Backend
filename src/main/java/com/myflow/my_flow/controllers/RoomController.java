package com.myflow.my_flow.controllers;

import com.myflow.my_flow.constants.Messages;
import com.myflow.my_flow.dto.requests.room.RequestCreateRoomDTO;
import com.myflow.my_flow.dto.requests.room.RequestJoinRoomDTO;
import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.dto.responses.room.CreateRoomDTO;
import com.myflow.my_flow.dto.responses.room.JoinRoomDTO;
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
  public ResponseEntity<BasicResponseDTO<CreateRoomDTO>> createRoom(
      @Valid @RequestBody RequestCreateRoomDTO requestCreateRoomDto
      ) {
    CreateRoomDTO newRoom = this.roomService.createRoom(requestCreateRoomDto.getDuration());

    return ResponseEntity.status(HttpStatus.CREATED).body(
        BasicResponseDTO.<CreateRoomDTO>builder()
            .data(newRoom)
            .message(Messages.SUCCESS.getValue())
            .build()
    );
  }

  @PostMapping("/{roomId}/join")
  public ResponseEntity<BasicResponseDTO<JoinRoomDTO>> joinRoom(
      @PathVariable String roomId,  @Valid @RequestBody RequestJoinRoomDTO requestJoinRoomDTO
  ) {
    JoinRoomDTO dto = this.roomService.joinRoom(roomId, requestJoinRoomDTO);
    System.out.println(
        BasicResponseDTO.<JoinRoomDTO>builder()
            .message(Messages.SUCCESS.getValue())
            .data(dto)
            .build()
    );

    return ResponseEntity.ok().body(
        BasicResponseDTO.<JoinRoomDTO>builder()
            .message(Messages.SUCCESS.getValue())
            .data(dto)
            .build()
    );
  }
}
