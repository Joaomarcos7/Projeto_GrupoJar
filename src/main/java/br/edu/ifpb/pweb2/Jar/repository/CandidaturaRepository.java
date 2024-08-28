package br.edu.ifpb.pweb2.Jar.repository;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    List<Candidatura> findByAluno(Aluno aluno);
    List<Candidatura> findByOfertaEstagio(OfertaEstagio ofertaEstagio);
    List<Candidatura> findByEstado(EstadoCandidatura estado);

    boolean existsByAlunoIdAndOfertaEstagioId(Long alunoId, Long ofertaId);
}
