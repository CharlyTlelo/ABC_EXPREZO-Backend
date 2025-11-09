package com.abc.exprezo.repos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Document("repositorios")
public class Repositorio {
    @Id private String id;

    @NotBlank private String proyecto;          // p.ej., "Frontend Angular"
    @Indexed(unique = true) @NotBlank
    private String repositorio;                 // p.ej., "ABC_EXPREZO-Frontend"

    private String url;                         // si viene vacío -> se pone default

    @CreatedDate      private OffsetDateTime createdAt;
    @LastModifiedDate private OffsetDateTime updatedAt;

    // getters/setters...

    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getProyecto() { return proyecto; } public void setProyecto(String proyecto) { this.proyecto = proyecto; }
    public String getRepositorio() { return repositorio; } public void setRepositorio(String repositorio) { this.repositorio = repositorio; }
    public String getUrl() { return url; } public void setUrl(String url) { this.url = url; }
    public OffsetDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}