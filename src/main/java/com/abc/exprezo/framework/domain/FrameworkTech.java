package com.abc.exprezo.framework.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Document("frameworks")
public class FrameworkTech {
    @Id private String id;

    @Indexed(unique = true) @NotBlank
    private String tecnologia;

    @NotBlank private String categoria;
    @NotBlank private String versionActual;
    @NotBlank private String proximaVersion;

    @CreatedDate      private OffsetDateTime createdAt;
    @LastModifiedDate private OffsetDateTime updatedAt;

    // getters/setters…
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getTecnologia() { return tecnologia; } public void setTecnologia(String tecnologia) { this.tecnologia = tecnologia; }
    public String getCategoria() { return categoria; } public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getVersionActual() { return versionActual; } public void setVersionActual(String versionActual) { this.versionActual = versionActual; }
    public String getProximaVersion() { return proximaVersion; } public void setProximaVersion(String proximaVersion) { this.proximaVersion = proximaVersion; }
    public OffsetDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

}
