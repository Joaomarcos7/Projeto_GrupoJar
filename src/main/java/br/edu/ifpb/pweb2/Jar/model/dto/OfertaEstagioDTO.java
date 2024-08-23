package br.edu.ifpb.pweb2.Jar.model.dto;

import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.Habilidade;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.util.GetStatusName;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Data
public class OfertaEstagioDTO {
    private Long id; // exemplo de atributo
    private String statusName;

    private String titulo;

    private String descricao;
    private String atividadePrincipal;

    private Integer ch;

    private BigDecimal valorPago;

    private Double valeTransporte;

    private String preRequisitos;


    private Set<Habilidade> habilidadesNecessarias = new HashSet<>();


    private Set<Habilidade> habilidadesDesejaveis = new HashSet<>();

    private LocalDate dataPublicacao;

    private LocalDate dataValidade;

    private Empresa empresa;

    private Set<Candidatura> candidaturas = new HashSet<>();

    public String getDataPublicacaoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataPublicacao.format(formatter);
    }

    public String getDataValidadeFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataValidade.format(formatter);
    }

    public String getFormatadoValorPago() {
        return valorPago != null ? String.format("R$ %.2f", this.valorPago) : "N/A";
    }

    public String getFormatadoValeTransporte() {
        return valeTransporte != null ? String.format("R$ %.2f", this.valeTransporte) : "N/A";
    }
    // Construtores, getters e setters

    public OfertaEstagioDTO(OfertaEstagio oferta) {
        this.id = oferta.getId();
        this.statusName = GetStatusName.get(oferta.getStatus());
        this.titulo = oferta.getTitulo();
        this.descricao = oferta.getDescricao();
        this.atividadePrincipal = oferta.getAtividadePrincipal();
        this.ch = oferta.getCh();
        this.valorPago = oferta.getValorPago();
        this.valeTransporte = oferta.getValeTransporte();
        this.preRequisitos = oferta.getPreRequisitos();
        this.habilidadesNecessarias = oferta.getHabilidadesNecessarias();
        this.habilidadesDesejaveis = oferta.getHabilidadesDesejaveis();
        this.dataPublicacao = oferta.getDataPublicacao();
        this.dataValidade = oferta.getDataValidade();
        this.empresa = oferta.getEmpresa();
        this.candidaturas = oferta.getCandidaturas();
    }
}
