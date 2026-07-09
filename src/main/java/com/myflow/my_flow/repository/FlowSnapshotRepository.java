package com.myflow.my_flow.repository;

import com.myflow.my_flow.models.FlowSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FlowSnapshotRepository extends JpaRepository<FlowSnapshot, UUID> { }
