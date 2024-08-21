package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Oferta;
import br.edu.ifpb.pweb2.Jar.repository.OfertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository ofertaRepository;

    public Oferta buscarPorId(Long id) {
        return ofertaRepository.findById(id).orElse(null);
    }
}
