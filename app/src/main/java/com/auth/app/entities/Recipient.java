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
    name = "recipients",
    indexes = {
        @Index(name = "idx_recipient_type", columnList = "recipientType"),
        @Index(name = "idx_identifier", columnList = "identifier"),
        @Index(name = "idx_is_active", columnList = "isActive")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_recipient_identifier", columnNames = {"recipientType", "identifier"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"notifications", "preferences"})
public class Recipient extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    @NotBlank(message = "Recipient type is required")
    private RecipientType recipientType;

    @NotBlank(message = "Identifier is required")
    @Size(max = 255, message = "Identifier cannot exceed 255 characters")
    @Column(name = "identifier", nullable = false, length = 255)
    private String identifier;
    
    @Size(max = 255, message = "Display name cannot exceed 255 characters")
    @Column(name = "display_name", length = 255)
    private String displayName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contact_info", columnDefinition = "JSON")
    @JsonIgnore // Sensitive information
    private Map<String, Object> contactInfo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "preferences", columnDefinition = "JSON")
    private Map<String, Object> preferences;

    @Builder.Default
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    @Column(name = "timezone", length = 50)
    private String timezone = "UTC";

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();
    
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificationPreference> notificationPreferences = new ArrayList<>();


}
