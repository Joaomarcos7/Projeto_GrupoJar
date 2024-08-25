package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.OfertaEstagioRepository;
import br.edu.ifpb.pweb2.Jar.util.GetStatusName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfertaEstagioService {

    @Autowired
    private OfertaEstagioRepository ofertaEstagioRepository;

    public OfertaEstagio save(OfertaEstagio oferta) {
        return ofertaEstagioRepository.save(oferta);
    }

    public void delete(OfertaEstagio oferta) {
        ofertaEstagioRepository.delete(oferta);
    }

    public List<OfertaEstagio> findAll() {
        return ofertaEstagioRepository.findAll();
    }

    public Optional<OfertaEstagio> findById(Long id) { return ofertaEstagioRepository.findById(id); }

    public List<OfertaEstagio> buscarOfertasDisponiveis() {
        return ofertaEstagioRepository.findByStatus(1);
    }

    public String convertStatus(int statusCode) {
        return GetStatusName.get(statusCode);
    }
}
