package com.auth.app.entities;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name="notification_templates",
    indexes = {
        @Index(name="idx_template_code", columnList = "templateCode"),
        @Index(name="idx_notification_type", columnList = "notification_type_id"),
        @Index(name="idx_language", columnList = "languageCode")
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "notificationType")
public class NotificationTemplate extends BaseEntity {

    @NotBlank(message="templateName is required")
    @Size(max=50, message="Template name must not exceed 50 character")
    @Column(name="template_name", nullable = false, length = 50)
    private String templateName;

    @NotBlank(message="templateCose is required")
    @Size(max=50, message="templateCode must not exceed 50 character")
    @Column(name="template_code", unique = true,  nullable = false, length = 50)
    private String templateCode;

    @Column(name="subject_template", columnDefinition = "TEXT")
    private String subjectTemplate;

    @NotBlank(message="bodyTemplate is required")
    @Column(name="body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name="variables", columnDefinition = "JSON")
    private Map<String, Object> variables;

    @Builder.Default
    @Size(max = 5, message="Language code cannot exceed 5 characters")
    @Column(name="language_code", length=5)
    private String languageCode ="en";

    @Builder.Default
    @Column(name="is_active", nullable = false)
    private Boolean isActive = true;

    //RelationShip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notification_type_id", nullable = false)
    @NotBlank(message="Notification type is required")
    private NotificationType notificationType;

}
