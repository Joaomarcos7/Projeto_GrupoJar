package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String telefoneContato;

    @Column(nullable = false)
    private String pessoaContato;

    @Column(nullable = false)
    private String atividadePrincipal;

    @Column(nullable = false)
    private URL url;

    @Column(nullable = false)
    private byte[] documentoComprovacaoEndereco;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OfertaEstagio> ofertaEstagios = new HashSet<>();

}
