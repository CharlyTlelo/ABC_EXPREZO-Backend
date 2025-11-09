package com.abc.exprezo.requirements.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("requirements")
public class Requirement {

    @Id
    private String id;

    // OJO: folio ya NO es único
    private String folio;

    private String title;
    private String description;
    private RequirementStatus status;

    // archivo
    private String originalFilename;
    private String contentType;
    private long sizeBytes;
    private String storagePath;

    // decisión de revisión (por documento)
    private String reviewDecision; // "aprobado" | "rechazado" | null
    private String reviewReason;   // comentario opcional

    @CreatedDate  private Instant createdAt;
    @LastModifiedDate private Instant updatedAt;



    public Requirement() {}

    // --- getters y setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public RequirementStatus getStatus() { return status; }
    public void setStatus(RequirementStatus status) { this.status = status; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long sizeBytes) { this.sizeBytes = sizeBytes; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getReviewDecision() {
        return reviewDecision;
    }
    public void setReviewDecision(String reviewDecision) {
        this.reviewDecision = reviewDecision;
    }

    public String getReviewReason() {
        return reviewReason;
    }
    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }
}
