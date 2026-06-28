package com.myflow.my_flow.dto.responses.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomDTO {
  private String wsToken;
  private String participantId;
  private String displayName;
}
