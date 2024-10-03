package br.edu.ifpb.pweb2.Jar.util;

import br.edu.ifpb.pweb2.Jar.model.StatusOfertaEstagio;

public class GetStatusName {

    public static String get(int statusCode) {
        return switch (statusCode) {
            case 1 -> StatusOfertaEstagio.APROVADO.name();
            case 2 -> StatusOfertaEstagio.PENDENTE.name();
            case 3 -> StatusOfertaEstagio.NEGADO.name();
            case 4 -> StatusOfertaEstagio.FINALIZADO.name();
            default -> "Desconhecido";
        };
    }
}
