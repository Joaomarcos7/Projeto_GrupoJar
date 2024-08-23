package br.edu.ifpb.pweb2.Jar.model;

public enum StatusOfertaEstagio {
    APROVADO(1),
    PENDENTE(2),
    NEGADO(3);

    private final int status;

    StatusOfertaEstagio(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
