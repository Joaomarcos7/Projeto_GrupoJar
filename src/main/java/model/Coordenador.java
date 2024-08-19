package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
// import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Coordenador")
public class Coordenador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;


    // @OneToMany(mappedBy = "coordenador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private List<Oferta> acompanharOfertas;

    // @OneToMany(mappedBy = "coordenador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private List<Estagio> acompanharEstagio;
}
