package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Empresa extends User{

    @NotBlank(message = "Campo obrigatório")
    @Column(nullable = false)
    private String nome;

    @Size(min = 14, message = "CNPJ deve ter no mínimo 14 caracteres")
    @Column(nullable = false, unique = true)
    private String cnpj;

    @NotBlank(message = "Campo obrigatório")
    @Column(nullable = false)
    private String endereco;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX.")
    @Column(nullable = false)
    private String telefoneContato;

    @NotBlank(message = "Campo obrigatório")
    @Column(nullable = false)
    private String pessoaContato;

    @NotBlank(message = "Campo obrigatório")
    @Column(nullable = false)
    private String atividadePrincipal;

    @NotNull(message = "Campo obrigatório")
    @Column(nullable = false)
    private URL url;

    @NotNull(message = "Campo obrigatório")
    @Column(nullable = false)
    private byte[] documentoComprovacaoEndereco;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<OfertaEstagio> ofertaEstagios = new HashSet<>();

}
