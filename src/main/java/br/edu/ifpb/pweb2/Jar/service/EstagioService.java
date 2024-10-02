package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.Estagio;
import br.edu.ifpb.pweb2.Jar.repository.EstagioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class EstagioService {
    @Autowired
    private EstagioRepository repository;

    public Estagio save(Estagio estagio) {
        return repository.save(estagio);
    }

    public Estagio findByAluno(Aluno aluno){ return repository.findByAluno(aluno);}

    public List<Estagio> findAll() {
        return repository.findAll();
    }

    public Page<Estagio> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Estagio> findById(Long id) {
        return repository.findById(id);
    }

    public Estagio findByEmpresa(Empresa empresa) { return repository.findByEmpresa(empresa); }

}
