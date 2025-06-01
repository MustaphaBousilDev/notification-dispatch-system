package com.auth.app.entities;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.app.entities.enums.NotificationPriority;
import com.auth.app.entities.enums.NotificationStatus;
import com.auth.app.entities.enums.RecipientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(name = "idx_notification_type", columnList = "notification_type_id"),
        @Index(name = "idx_recipient", columnList = "recipient_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_priority", columnList = "priority"),
        @Index(name = "idx_scheduled_at", columnList = "scheduledAt"),
        @Index(name = "idx_batch_execution", columnList = "batch_execution_id"),
        @Index(name = "idx_external_id", columnList = "externalId"),
        @Index(name = "idx_status_created", columnList = {"status", "createdAt"}),
        @Index(name = "idx_recipient_status", columnList = {"recipient_id", "status"}),
        @Index(name = "idx_type_priority", columnList = {"notification_type_id", "priority"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"notificationType", "template", "batchExecution", "recipient", "deliveryLogs"})

public class Notification  extends BaseEntity{
    @Size(max = 500, message = "Subject cannot exceed 500 characters")
    @Column(name = "subject", length = 500)
    private String subject;
    
    @NotBlank(message = "Content is required")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "JSON")
    private Map<String, Object> metadata;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "priority", nullable = false)
    private NotificationPriority priority = NotificationPriority.MEDIUM;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false)
    private NotificationStatus status = NotificationStatus.PENDING;
    
    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;
    
    @Builder.Default
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;
    
    @Builder.Default
    @Column(name = "max_retries", nullable = false)
    private Integer maxRetries = 3;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Size(max = 255, message = "External ID cannot exceed 255 characters")
    @Column(name = "external_id", length = 255)
    private String externalId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id", nullable = false)
    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private NotificationTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_execution_id")
    private BatchExecution batchExecution;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    @NotNull(message = "Recipient is required")
    private Recipient recipient;
    
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificationDeliveryLog> deliveryLogs = new ArrayList<>();

}
