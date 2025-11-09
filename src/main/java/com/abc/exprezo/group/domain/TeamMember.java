package com.abc.exprezo.group.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

@Document("team_members")
public class TeamMember {
    @Id private String id;

    @Indexed(unique = true) @NotBlank @Size(max=80)
    private String code;

    @NotBlank @Size(max=150)
    private String fullName;

    @NotBlank @Size(max=120)
    private String title;

    private String category;     // DEV, QA, MANAGEMENT...
    private String status;       // ACTIVE, INACTIVE
    private String area;         // E-commerce
    //private String colorHex;     // #148d0a
    private String avatarUrl;
    private Integer displayOrder;

    @CreatedDate      private OffsetDateTime createdAt;
    @LastModifiedDate private OffsetDateTime updatedAt;

    // Getters y setters (généralos con tu IDE)
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getCode() { return code; } public void setCode(String code) { this.code = code; }
    public String getFullName() { return fullName; } public void setFullName(String fullName) { this.fullName = fullName; }
    public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; } public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getArea() { return area; } public void setArea(String area) { this.area = area; }
    //public String getColorHex() { return colorHex; } public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public String getAvatarUrl() { return avatarUrl; } public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Integer getDisplayOrder() { return displayOrder; } public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public OffsetDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
