package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
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

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private int vagas;

    @ElementCollection(targetClass = Habilidade.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "habilidades_necessarias", joinColumns = @JoinColumn(name = "oferta_id", nullable = false))
    @Column(name = "habilidade_necessaria")
    private Set<Habilidade> habilidadesNecessarias = new HashSet<>();

    @ElementCollection(targetClass = Habilidade.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "habilidades_desejaveis", joinColumns = @JoinColumn(name = "oferta_id", nullable = false))
    @Column(name = "habilidade_desejavel")
    private Set<Habilidade> habilidadesDesejaveis = new HashSet<>();

    @Column(nullable = false)
    private LocalDate dataPublicacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "ofertaEstagio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Candidatura> candidaturas = new HashSet<>();

    public String getDataPublicacaoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataPublicacao.format(formatter);
    }

    public String getFormatadoValorPago() {
        return valorPago != null ? String.format("R$ %.2f", this.valorPago) : "N/A";
    }

    public String getFormatadoValeTransporte() {
        return valeTransporte != null ? String.format("R$ %.2f", this.valeTransporte) : "N/A";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OfertaEstagio ofertaEstagio = (OfertaEstagio) obj;
        return Objects.equals(id, ofertaEstagio.id);
    }

}
