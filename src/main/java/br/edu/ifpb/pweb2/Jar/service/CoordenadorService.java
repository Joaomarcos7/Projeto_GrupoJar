package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Coordenador;
import br.edu.ifpb.pweb2.Jar.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordenadorService {

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    public void save(Coordenador coordenador) { coordenadorRepository.save(coordenador); }

    public List<Coordenador> findAll() { return coordenadorRepository.findAll(); }

    public Optional<Coordenador> findById(Long id) { return coordenadorRepository.findById(id); }

    public Coordenador findByEmail(String email) { return coordenadorRepository.findByEmail(email); }
}
