package com.myflow.my_flow.dto.requests.room;

import com.myflow.my_flow.constants.RoomDuration;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateRoomDTO {
    @NotNull(message = "Duration is required")
    private RoomDuration duration;
}
