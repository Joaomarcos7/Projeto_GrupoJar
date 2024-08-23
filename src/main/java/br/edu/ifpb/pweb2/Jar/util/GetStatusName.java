package br.edu.ifpb.pweb2.Jar.util;

import br.edu.ifpb.pweb2.Jar.model.StatusOfertaEstagio;

public class GetStatusName {

    public static String get(int statusCode) {
        switch (statusCode) {
            case 1:
                return StatusOfertaEstagio.APROVADO.name();
            case 2:
                return StatusOfertaEstagio.PENDENTE.name();
            case 3:
                return StatusOfertaEstagio.NEGADO.name();
            default:
                return "Desconhecido";
        }
    }
}
