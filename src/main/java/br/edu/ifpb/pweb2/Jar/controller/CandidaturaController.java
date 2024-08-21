package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.Oferta;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    /*@Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private OfertaService ofertaService;

    @PostMapping("/{alunoId}/oferta/{ofertaId}")
    public ResponseEntity<String> candidatarAluno(@PathVariable Long alunoId, @PathVariable Long ofertaId) {
        Aluno aluno = alunoService.fiPorId(alunoId);
        Oferta oferta = ofertaService.buscarPorId(ofertaId);

        if (aluno == null || oferta == null) {
            return ResponseEntity.badRequest().body("Aluno ou Oferta não encontrados.");
        }

        Candidatura candidatura = new Candidatura();
        candidatura.setAluno(aluno);
        candidatura.setOferta(oferta);
        candidatura.setDataCandidatura(LocalDate.now());
        candidatura.setEstado(EstadoCandidatura.PENDENTE); // Estado inicial

        candidaturaService.salvar(candidatura);

        return ResponseEntity.ok("Candidatura realizada com sucesso.");
    }*/
}
