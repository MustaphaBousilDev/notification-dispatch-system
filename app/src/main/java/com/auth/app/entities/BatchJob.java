package com.auth.app.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.app.entities.enums.RecipientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(
    name = "batch_jobs",
    indexes = {
        @Index(name = "idx_job_name", columnList = "jobName"),
        @Index(name = "idx_is_active", columnList = "isActive")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "executions")
public class BatchJob extends BaseEntity {
    @NotBlank(message = "Job name is required")
    @Size(max = 100, message = "Job name cannot exceed 100 characters")
    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;
    
    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "job_parameters", columnDefinition = "JSON")
    private Map<String, Object> jobParameters;
    
    @Size(max = 100, message = "Cron expression cannot exceed 100 characters")
    @Column(name = "cron_expression", length = 100)
    private String cronExpression;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    // Relationships
    @OneToMany(mappedBy = "batchJob", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BatchExecution> executions = new ArrayList<>();

}
