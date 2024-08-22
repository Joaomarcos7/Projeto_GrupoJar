package br.edu.ifpb.pweb2.Jar.model;

import lombok.Getter;

@Getter
public enum Habilidade {
    PROGRAMACAO_PYTHON("Programador Python"),
    DESIGN_CSS("Design CSS"),
    ESPECIALISTA_UX("Especialista UX"),
    PROGRAMADOR_TESTES("Programador testes"),
    DESENVOLVIMENTO_WEB("Desenvolvimento web"),
    BANCO_DE_DADOS("Banco de Dados"),
    INTELIGENCIA_ARTIFICIAL("Inteligencia Artificial"),
    MACHINE_LEARNING("Machine Learning");

    private final String description;

    Habilidade(String description) {
        this.description = description;
    }
}
