package com.auth.app.entities;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.app.entities.enums.DeliveryStatus;
import com.auth.app.entities.enums.NotificationPriority;
import com.auth.app.entities.enums.NotificationStatus;
import com.auth.app.entities.enums.RecipientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(
    name = "notification_delivery_log",
    indexes = {
        @Index(name = "idx_notification_id", columnList = "notification_id"),
        @Index(name = "idx_delivery_status", columnList = "deliveryStatus"),
        @Index(name = "idx_attempt_at", columnList = "attemptAt")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "notification")
public class NotificationDeliveryLog extends BaseEntity{
    @Column(name = "attempt_number", nullable = false)
    @NotNull(message = "Attempt number is required")
    private Integer attemptNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    @NotNull(message = "Delivery status is required")
    private DeliveryStatus deliveryStatus;
    
    @Size(max = 10, message = "Response code cannot exceed 10 characters")
    @Column(name = "response_code", length = 10)
    private String responseCode;

    @Column(name = "response_message", columnDefinition = "TEXT")
    private String responseMessage;
    
    @Size(max = 50, message = "Delivery channel cannot exceed 50 characters")
    @Column(name = "delivery_channel", length = 50)
    private String deliveryChannel;
    
    @Size(max = 255, message = "External reference cannot exceed 255 characters")
    @Column(name = "external_reference", length = 255)
    private String externalReference;
    
    @Builder.Default
    @Column(name = "attempt_at", nullable = false)
    private LocalDateTime attemptAt = LocalDateTime.now();
    
    @Column(name = "response_time_ms")
    private Integer responseTimeMs;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    @NotNull(message = "Notification is required")
    private Notification notification;
}
