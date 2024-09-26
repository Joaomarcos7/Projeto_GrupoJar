package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public List<Aluno> findAllByIds(List<Long> ids) {
        return alunoRepository.findAllById(ids);
    }

    public Optional<Aluno> findById(Long id) { return alunoRepository.findById(id); }

    public Aluno findByUsername(String username) { return alunoRepository.findByUsername(username); }

}
