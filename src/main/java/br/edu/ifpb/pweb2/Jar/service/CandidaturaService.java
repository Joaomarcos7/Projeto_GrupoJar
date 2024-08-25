package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.CandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public void save(Candidatura candidatura) {
        candidaturaRepository.save(candidatura);
    }
    
    public List<Candidatura> buscarPorAluno(Aluno aluno) { return candidaturaRepository.findByAluno(aluno); }
    

    public boolean existsByAlunoIdAndOfertaId(Long alunoId, Long ofertaId) {
        return candidaturaRepository.existsByAlunoIdAndOfertaEstagioId(alunoId, ofertaId);
    }
    
    public List<Aluno> buscarAlunosNaoSelecionados() {
        return candidaturaRepository.findByEstado(EstadoCandidatura.PENDENTE)
            .stream()
            .map(Candidatura::getAluno)
            .collect(Collectors.toList());
    }


    
}
