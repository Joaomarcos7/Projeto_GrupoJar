package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @Autowired
    private CandidaturaService candidaturaService;

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("alunos/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        ModelAndView model) {

        Aluno aluno = alunoService.findByUsername(username);

        if (aluno != null) {
            if (aluno.getSenha().equals(password)) {
                model.setViewName("redirect:/alunos/" + aluno.getId() + "/menu");
            } else {
                model.addObject("error", "Senha incorreta.");
                model.setViewName("alunos/login");
            }
        } else {
            model.addObject("error", "Nome de usuário não encontrado.");
            model.setViewName("alunos/login");
        }
        return model;
    }

    @GetMapping("/cadastro")
    public ModelAndView exibirFormularioDeCadastro(ModelAndView modelAndView) {
        modelAndView.addObject("aluno", new Aluno());
        modelAndView.setViewName("alunos/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarAluno(@Validated @ModelAttribute("aluno") Aluno aluno,
                                       BindingResult result, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            modelAndView.setViewName("alunos/form");
            return modelAndView;
        }
        alunoService.save(aluno);
        redirectAttributes.addFlashAttribute("mensagem", "Bem vindo(a)!");
        modelAndView.addObject("aluno", aluno);
        modelAndView.setViewName("redirect:/alunos/" + aluno.getId() + "/menu");
        return modelAndView;
    }

    @GetMapping("/{id}/menu")
    public ModelAndView exibirMenu(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Optional<Aluno> alunoOptional = alunoService.findById(id);
        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();
            modelAndView.addObject("aluno", aluno);
            modelAndView.setViewName("alunos/menu");
        } else {
            modelAndView.setViewName("redirect:/alunos/form");
        }
        return modelAndView;
    }

    @GetMapping("/{id}/ofertas")
    public ModelAndView listarOfertasDisponiveis(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Optional<Aluno> alunoOptional = alunoService.findById(id);

        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();

            List<OfertaEstagio> ofertasDisponiveis = ofertaEstagioService.buscarOfertasDisponiveis();
            List<OfertaEstagioDTO> ofertasDTO = ofertasDisponiveis.stream()
                    .map(oferta -> {
                        OfertaEstagioDTO dto = new OfertaEstagioDTO(oferta);
                        boolean jaCandidatou = candidaturaService.existsByAlunoIdAndOfertaId(aluno.getId(), oferta.getId());
                        dto.setJaCandidatou(jaCandidatou);
                        return dto;
                    })
                    .toList();

            modelAndView.addObject("aluno", aluno);
            modelAndView.addObject("ofertas", ofertasDTO);
            modelAndView.setViewName("alunos/ofertas");
        } else {
            modelAndView.setViewName("redirect:/alunos/login");
        }
        return modelAndView;

    }

    @GetMapping("/{id}/candidaturas")
    public ModelAndView listarCandidaturas(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Optional<Aluno> alunoOptional = alunoService.findById(id);
        if (alunoOptional.isPresent()) {
            Aluno aluno = alunoOptional.get();

            List<Candidatura> candidaturas = candidaturaService.buscarPorAluno(aluno);

            modelAndView.addObject("candidaturas", candidaturas);
            modelAndView.addObject("aluno", aluno);
            modelAndView.setViewName("alunos/candidaturas");
        } else {
            modelAndView.setViewName("redirect:/alunos/login");
        }

        return modelAndView;
    }

    @GetMapping()
    public ModelAndView listarAlunos(ModelAndView modelAndView) {
        List<Aluno> alunos = alunoService.findAll();
        modelAndView.addObject("alunos", alunos);
        modelAndView.setViewName("alunos/list");
        return modelAndView;
    }
}
