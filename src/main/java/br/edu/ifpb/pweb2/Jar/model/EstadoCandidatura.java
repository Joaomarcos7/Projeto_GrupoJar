package br.edu.ifpb.pweb2.Jar.model;

import lombok.Getter;

@Getter
public enum EstadoCandidatura {
    PENDENTE("Pendente"),
    ACEITA("Aceita"),
    REJEITADA("Rejeitada"),
    CANCELADA("Cancelada");

    private final String description;

    EstadoCandidatura(String description) {
        this.description = description;
    }
}
