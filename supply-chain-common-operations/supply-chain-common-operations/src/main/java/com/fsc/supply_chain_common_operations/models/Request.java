package com.fsc.supply_chain_common_operations.models;

import com.fsc.supply_chain_common_operations.enums.RequestPriority;
import com.fsc.supply_chain_common_operations.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "requests")
@Entity
public class Request extends GlobalRecord {
    @Column(name = "request_number", nullable = false, unique = true)
    private String requestNumber;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RequestStatus requestStatus = RequestStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestPriority priority = RequestPriority.MEDIUM;

    private Instant submissionDate;

    private LocalDate requiredDate;


    @Column(length = 1000)
    private String rejectionReason;
}
