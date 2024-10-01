package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.OfertaEstagioRepository;
import br.edu.ifpb.pweb2.Jar.util.GetStatusName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<OfertaEstagio> findAllPaged(Pageable page) {
        return ofertaEstagioRepository.findAll(page);
    }

    public Optional<OfertaEstagio> findById(Long id) { return ofertaEstagioRepository.findById(id); }

    public Page<OfertaEstagio> findByEmpresa(Long empresaId, Pageable page) {
        return ofertaEstagioRepository.findByEmpresaId(empresaId, page);
    }

    public Page<OfertaEstagio> buscarOfertasDisponiveisPaginado(Pageable page) {
        return ofertaEstagioRepository.findByStatus(1, page);
    }

    public String convertStatus(int statusCode) {
        return GetStatusName.get(statusCode);
    }
}
