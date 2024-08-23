package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.OfertaEstagioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfertaEstagioService {

    @Autowired
    private OfertaEstagioRepository ofertaEstagioRepository;

    public OfertaEstagio save(OfertaEstagio oferta) {
        return ofertaEstagioRepository.save(oferta);
    }

    public List<OfertaEstagio> findAll() {
        return ofertaEstagioRepository.findAll();
    }

    public OfertaEstagio buscarPorId(Long id) {
        return ofertaEstagioRepository.findById(id).orElse(null);
    }
}
