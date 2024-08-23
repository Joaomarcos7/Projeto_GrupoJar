package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import org.springframework.web.bind.annotation.*;

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
