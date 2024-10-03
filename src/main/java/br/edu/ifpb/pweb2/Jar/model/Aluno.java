package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Aluno extends User {

    @NotBlank(message = "Campo obrigatório")
    @Column(nullable = false)
    private String nomeCompleto;


    @ElementCollection(targetClass = Habilidade.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "aluno_habilidades", joinColumns = @JoinColumn(name = "aluno_id"))
    @Column(name = "habilidade", nullable = false)
    private Set<Habilidade> habilidades;
}
