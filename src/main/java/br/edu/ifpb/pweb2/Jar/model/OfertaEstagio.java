package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OfertaEstagio implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String atividadePrincipal;

    @Column(nullable = false)
    private Integer ch;

    private BigDecimal valorPago;

    private Double valeTransporte;

    @Column(nullable = false)
    private String preRequisitos;

    @ElementCollection(targetClass = Habilidade.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "habilidades_necessarias", joinColumns = @JoinColumn(name = "oferta_id"))
    @Column(name = "habilidade_necessaria")
    private Set<Habilidade> habilidadesNecessarias = new HashSet<>();

    @ElementCollection(targetClass = Habilidade.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "habilidades_desejaveis", joinColumns = @JoinColumn(name = "oferta_id"))
    @Column(name = "habilidade_desejavel")
    private Set<Habilidade> habilidadesDesejaveis = new HashSet<>();

    @Column(nullable = false)
    private LocalDate dataPublicacao;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOferta u;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "ofertaEstagio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Candidatura> candidaturas = new HashSet<>();

}
