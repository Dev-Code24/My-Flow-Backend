package com.myflow.my_flow.controllers;

import com.myflow.my_flow.constants.Messages;
import com.myflow.my_flow.dto.requests.flow.ShareFlowDocumentRequestDTO;
import com.myflow.my_flow.dto.responses.BasicResponseDTO;
import com.myflow.my_flow.dto.responses.flow.ShareFlowResponseDTO;
import com.myflow.my_flow.dto.responses.flow.SharedFlowDocumentResponseDTO;
import com.myflow.my_flow.services.flow.FlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/share")
@Controller
public class FlowController {
  private final FlowService flowService;

  public FlowController(FlowService flowService) {
    this.flowService = flowService;
  }

  @PostMapping
  public ResponseEntity<BasicResponseDTO<ShareFlowResponseDTO>> shareFlow(
      @RequestBody ShareFlowDocumentRequestDTO requestBody
      ) {
    ShareFlowResponseDTO dto = this.flowService.shareFlow(requestBody.document());

    return ResponseEntity.ok().body(
        BasicResponseDTO.<ShareFlowResponseDTO>builder()
            .message(Messages.SUCCESS.getValue())
            .data(dto)
            .build()
    );
  }

  @GetMapping("/{flowId}")
  public ResponseEntity<BasicResponseDTO<SharedFlowDocumentResponseDTO>> getFlow(
      @PathVariable UUID flowId
  ) {
    SharedFlowDocumentResponseDTO dto = this.flowService.getSharedFlow(flowId);

    return ResponseEntity.ok().body(
        BasicResponseDTO.<SharedFlowDocumentResponseDTO>builder()
            .data(dto)
            .message(Messages.SUCCESS.getValue())
            .build()
    );
  }
}
