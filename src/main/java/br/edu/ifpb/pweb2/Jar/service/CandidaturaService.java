package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.CandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
