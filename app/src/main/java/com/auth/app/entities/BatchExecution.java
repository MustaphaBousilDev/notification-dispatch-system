package com.auth.app.entities;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.app.entities.enums.BatchExecutionStatus;
import com.auth.app.entities.enums.RecipientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(
    name = "batch_executions",
    indexes = {
        @Index(name = "idx_batch_job_id", columnList = "batch_job_id"),
        @Index(name = "idx_execution_id", columnList = "executionId"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_start_time", columnList = "startTime"),
        @Index(name = "idx_status_start", columnList = "status,startTime")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"batchJob", "notifications"})
public class BatchExecution extends BaseEntity {
    @NotBlank(message = "Execution ID is required")
    @Size(max = 100, message = "Execution ID cannot exceed 100 characters")
    @Column(name = "execution_id", nullable = false, unique = true, length = 100)
    private String executionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotBlank(message = "Status is required")
    private BatchExecutionStatus status;

    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Size(max = 20, message = "Exit code cannot exceed 20 characters")
    @Column(name = "exit_code", length = 20)
    private String exitCode;
    
    @Column(name = "exit_message", columnDefinition = "TEXT")
    private String exitMessage;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "job_parameters", columnDefinition = "JSON")
    private Map<String, Object> jobParameters;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "execution_context", columnDefinition = "JSON")
    private Map<String, Object> executionContext;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_job_id", nullable = false)
    @NotNull(message = "Batch job is required")
    private BatchJob batchJob;
    
    @OneToMany(mappedBy = "batchExecution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

}
