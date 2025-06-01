package com.auth.app.entities;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.auth.app.entities.enums.ConfigDataType;
import com.auth.app.entities.enums.DeliveryStatus;
import com.auth.app.entities.enums.NotificationFrequency;
import com.auth.app.entities.enums.NotificationPriority;
import com.auth.app.entities.enums.NotificationStatus;
import com.auth.app.entities.enums.RecipientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
@Table(
    name = "system_configuration",
    indexes = {
        @Index(name = "idx_config_key", columnList = "configKey")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfiguration  extends BaseEntity{
    @NotBlank(message = "Config key is required")
    @Size(max = 100, message = "Config key cannot exceed 100 characters")
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;
    
    @NotBlank(message = "Config value is required")
    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    @JsonIgnore // Potentially sensitive configuration
    private String configValue;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "data_type", nullable = false)
    private ConfigDataType dataType = ConfigDataType.STRING;
    
    @Builder.Default
    @Column(name = "is_encrypted", nullable = false)
    private Boolean isEncrypted = false;
}
