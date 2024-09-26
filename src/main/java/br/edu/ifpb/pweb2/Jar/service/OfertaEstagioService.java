package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.Estagio;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.OfertaEstagioRepository;
import br.edu.ifpb.pweb2.Jar.util.GetStatusName;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OfertaEstagioService {

    @Autowired
    private OfertaEstagioRepository ofertaEstagioRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EstagioService  estagioService;

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

    public void converterParaEstagio(Long ofertaId, List<Long> alunosIds, Long empresaId, LocalDate dataInicio, LocalDate dataTermino, BigDecimal valor) {
        OfertaEstagio oferta = ofertaEstagioRepository.findById(ofertaId).orElseThrow(() -> new EntityNotFoundException("Oferta não encontrada"));
        Empresa empresa = empresaService.findById(ofertaId).orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        List<Aluno> alunosSelecionados = alunoService.findAllByIds(alunosIds);

        for (Aluno aluno : alunosSelecionados) {
            Estagio estagio = new Estagio();
            estagio.setAluno(aluno);
            estagio.setDataInicio(dataInicio);
            estagio.setDataTermino(dataTermino);
            estagio.setValor(valor);
            estagio.setEmpresa(empresa);

            estagioService.save(estagio);
        }

        oferta.setFoiConvertidaEmEstagio(true);
        ofertaEstagioRepository.save(oferta);
    }
}
