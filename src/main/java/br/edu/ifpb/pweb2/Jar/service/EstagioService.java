package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Estagio;
import br.edu.ifpb.pweb2.Jar.repository.EstagioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstagioService {

    @Autowired
    private EstagioRepository estagioRepository;

    public List<Estagio> findAll() {
        return estagioRepository.findAll();
    }

    public Estagio save (Estagio estagio) {
        return estagioRepository.save(estagio);
    }
}
