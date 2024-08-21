package br.edu.ifpb.pweb2.Jar.model;

import lombok.Getter;

@Getter
public enum StatusOferta {
    ABERTA("Aberta"),
    FECHADA("Fechada"),
    CANCELADA("Cancelada");

    private final String description;

    StatusOferta(String description) {
        this.description = description;
    }
}
