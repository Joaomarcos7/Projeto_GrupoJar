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
public class Empresa extends Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O nome da empresa é obrigatório.")
    @Column(nullable = false)
    private String nome;

    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter 14 dígitos.")
    @Column(nullable = false, unique = true)
    private String cnpj;

    @NotBlank(message = "O endereço é obrigatório.")
    @Column(nullable = false)
    private String endereco;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX.")
    @Column(nullable = false)
    private String telefoneContato;

    @NotBlank(message = "O nome da pessoa de contato é obrigatório.")
    @Column(nullable = false)
    private String pessoaContato;

    @NotBlank(message = "A atividade principal é obrigatória.")
    @Column(nullable = false)
    private String atividadePrincipal;

    @NotNull(message = "A URL é obrigatória.")
    @Column(nullable = false)
    private URL url;

    @NotNull(message = "O documento de comprovação de endereço é obrigatório.")
    @Column(nullable = false)
    private byte[] documentoComprovacaoEndereco;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OfertaEstagio> ofertaEstagios = new HashSet<>();

}
