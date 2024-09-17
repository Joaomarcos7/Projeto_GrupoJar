package br.edu.ifpb.pweb2.Jar.model.dto;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Habilidade;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AlunoDTO {
    private Long id;
    private String nomeCompleto;
    private String username;
    private String email;
    private Set<Habilidade> habilidades = new HashSet<>();

    public AlunoDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nomeCompleto = aluno.getNomeCompleto();
        this.username = aluno.getUsername();
        this.email = aluno.getEmail();
        this.habilidades = aluno.getHabilidades(); // Copia as habilidades diretamente
    }

    // Método para formatar as habilidades em uma lista de strings
    public String getFormatadoHabilidades() {
        return habilidades.stream()
            .map(Habilidade::name)
            .collect(Collectors.joining(", "));
    }
}
