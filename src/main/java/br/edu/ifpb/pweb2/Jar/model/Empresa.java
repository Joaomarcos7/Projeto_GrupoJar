package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String telefoneContato;

    @Column(nullable = false, unique = true)
    private String emailContato;

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
