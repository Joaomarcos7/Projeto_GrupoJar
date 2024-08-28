package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @PostMapping("/candidatar/{alunoId}/oferta/{ofertaId}")
    public ModelAndView candidatar(@PathVariable Long alunoId, @PathVariable Long ofertaId,
                                   ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        Optional<Aluno> alunoOptional = alunoService.findById(alunoId);
        Optional<OfertaEstagio> ofertaEstagioOptional = ofertaEstagioService.findById(ofertaId);

        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();
            if (ofertaEstagioOptional.isPresent()) {
                OfertaEstagio ofertaEstagio = ofertaEstagioOptional.get();

                Candidatura candidatura = new Candidatura();
                candidatura.setAluno(aluno);
                candidatura.setOfertaEstagio(ofertaEstagio);
                candidatura.setDataCandidatura(LocalDate.now());
                candidatura.setEstado(EstadoCandidatura.PENDENTE);
                ofertaEstagio.getCandidaturas().add(candidatura);

                candidaturaService.save(candidatura);

                redirectAttributes.addFlashAttribute("mensagem", "Candidatura realizada com sucesso!");
                modelAndView.setViewName("redirect:/alunos/" + aluno.getId() + "/candidaturas");

            } else {
                modelAndView.addObject("mensagem", "Oferta invalida");
                modelAndView.setViewName("redirect:/alunos/" + aluno.getId() + "/candidaturas");
            }
        } else {
            modelAndView.addObject("mensagem", "Aluno nao encontrado");
            modelAndView.setViewName("redirect:/alunos/login");
        }

       return modelAndView;
    }

}
