package br.edu.ifpb.pweb2.Jar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Estagio {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable=false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name="empresa_id",nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Aluno> aluno;

    @ManyToOne
    @JoinColumn(name="oferta_id",nullable = false)
    private OfertaEstagio oferta;



    public String getDataInicioFormatada() {
        return formatarData(dataInicio);
    }

    public String getDataFimFormatada() {
        return formatarData(dataFim);
    }

    public String getNomeAlunos() {
        StringBuilder response = new StringBuilder();

        for (Aluno aluno : this.aluno) {
            response.append(aluno.getNomeCompleto()).append(" (").append(aluno.getEmail()).append(")").append("; ");
        }

        // Remove o último ponto e vírgula e espaço, se houver
        if (response.length() > 0) {
            response.setLength(response.length() - 2); // Remove o último "; "
        }

        return response.toString();
    }
    // Método auxiliar para formatar a data
    private String formatarData(LocalDate data) {
        if (data != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return data.format(formatter);
        }
        return null;
    }



}
