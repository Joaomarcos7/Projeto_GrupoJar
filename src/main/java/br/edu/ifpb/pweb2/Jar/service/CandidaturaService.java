package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.CandidaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public void save(Candidatura candidatura) {
        candidaturaRepository.save(candidatura);
    }

    public Optional<Candidatura> findById(Long id) { return candidaturaRepository.findById(id); }
    
    public List<Candidatura> buscarPorAluno(Aluno aluno) { return candidaturaRepository.findByAluno(aluno); }

    public List<Candidatura> buscarPorOferta(OfertaEstagio oferta) {
        return candidaturaRepository.findByOfertaEstagio(oferta);
    }
    public boolean existsByAlunoIdAndOfertaId(Long alunoId, Long ofertaId) {
        return candidaturaRepository.existsByAlunoIdAndOfertaEstagioId(alunoId, ofertaId);
    }
    
    public Page<Candidatura> buscarPorAlunosNaoSelecionadosPaginado(Pageable page) {
        return candidaturaRepository.findByEstado(EstadoCandidatura.PENDENTE, page);
    }




    
}
