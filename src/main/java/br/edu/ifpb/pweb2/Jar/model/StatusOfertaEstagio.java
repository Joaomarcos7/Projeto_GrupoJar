package br.edu.ifpb.pweb2.Jar.model;

import lombok.Getter;

@Getter
public enum StatusOfertaEstagio {
    APROVADO(1),
    PENDENTE(2),
    NEGADO(3);

    private final int status;

    StatusOfertaEstagio(int status) {
        this.status = status;
    }

}
