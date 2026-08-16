package com.myflow.my_flow.models;

import com.myflow.my_flow.models.abstracts.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Accessors(chain = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends AbstractEntity {
  @Column(nullable = false, unique = true)
  private String roomId;

  @Column(nullable = false, unique = true)
  private UUID creatorId;

  @Column(nullable = false)
  private Instant expiresAt;

  @Column(nullable = false)
  private Instant lastActivity;
}