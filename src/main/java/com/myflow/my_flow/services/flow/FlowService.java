package com.myflow.my_flow.services.flow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myflow.my_flow.dto.commons.flow.FlowDocumentDTO;
import com.myflow.my_flow.dto.responses.flow.ShareFlowResponseDTO;
import com.myflow.my_flow.dto.responses.flow.SharedFlowDocumentResponseDTO;
import com.myflow.my_flow.exceptions.flow.FlowNotFoundException;
import com.myflow.my_flow.models.FlowSnapshot;
import com.myflow.my_flow.repository.FlowSnapshotRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class FlowService {
  private final FlowSnapshotRepository flowSnapshotRepository;
  private final ObjectMapper objectMapper;

  public FlowService(
      FlowSnapshotRepository flowSnapshotRepository,
      ObjectMapper objectMapper
  ) {
    this.flowSnapshotRepository = flowSnapshotRepository;
    this.objectMapper = objectMapper;
  }

  public ShareFlowResponseDTO shareFlow(FlowDocumentDTO dto) {
    Instant expiresAt = Instant.now().plus(Duration.ofDays(90));
    JsonNode document = objectMapper.valueToTree(dto);

    FlowSnapshot snapshot = FlowSnapshot.builder()
        .expiresAt(expiresAt)
        .document(document)
        .build();

    FlowSnapshot savedSnapshot = this.flowSnapshotRepository.save(snapshot);

    return ShareFlowResponseDTO.builder()
        .flowId(savedSnapshot.getId())
        .expiresAt(savedSnapshot.getExpiresAt())
        .build();
  }

  public SharedFlowDocumentResponseDTO getSharedFlow(UUID flowId) {
    FlowSnapshot snapshot = this.flowSnapshotRepository.findById(flowId).orElseThrow(FlowNotFoundException::new);
    Instant newExpiresAt = Instant.now().plus(Duration.ofDays(90));

    snapshot.setExpiresAt(newExpiresAt);

    FlowSnapshot savedSnapshot = this.flowSnapshotRepository.save(snapshot);

    FlowDocumentDTO document = this.objectMapper.convertValue(
        savedSnapshot.getDocument(),
        FlowDocumentDTO.class
    );

    return SharedFlowDocumentResponseDTO.builder()
        .expiresAt(savedSnapshot.getExpiresAt())
        .document(document)
        .flowId(savedSnapshot.getId())
        .build();
  }
}
