package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private HttpSession httpSession;

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
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              ModelAndView modelAndView) {

        Aluno aluno = alunoService.findByEmail(email);

        if (aluno != null) {
            if (aluno.getSenha().equals(password)) {
                httpSession.setAttribute("alunoLogado", aluno);
                modelAndView.setViewName("redirect:/alunos/menu");
            } else {
                modelAndView.addObject("error", "Senha incorreta.");
                modelAndView.setViewName("alunos/login");
            }
        } else {
            modelAndView.addObject("error", "Email não encontrado.");
            modelAndView.setViewName("alunos/login");
        }
        return modelAndView;
    }

    @GetMapping("/cadastro")
    public ModelAndView exibirFormularioDeCadastro(ModelAndView modelAndView) {
        modelAndView.addObject("aluno", new Aluno());
        modelAndView.setViewName("alunos/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarAluno(@Validated @ModelAttribute("aluno") Aluno aluno,
                                       BindingResult result,
                                       ModelAndView modelAndView,
                                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            modelAndView.setViewName("alunos/form");
            return modelAndView;
        }

        alunoService.save(aluno);

        redirectAttributes.addFlashAttribute("mensagem", "Bem vindo(a)!");

        httpSession.setAttribute("alunoLogado", aluno);

        modelAndView.setViewName("redirect:/alunos/menu");
        return modelAndView;
    }

    @GetMapping("/menu")
    public ModelAndView exibirMenu(ModelAndView modelAndView) {
        Aluno alunoLogado = (Aluno) httpSession.getAttribute("alunoLogado");

        if (alunoLogado != null) {
            modelAndView.addObject("aluno", alunoLogado);
            modelAndView.setViewName("alunos/menu");
        } else {
            modelAndView.setViewName("alunos/login");
        }
        return modelAndView;
    }

    @GetMapping("/ofertas")
    public ModelAndView listarOfertasDisponiveis(@RequestParam(required = false) Double minValue,
                                                 @RequestParam(required = false) Double maxValue,
                                                 @RequestParam(required = false) List<String> habilidades,
                                                 ModelAndView modelAndView) {
        Aluno alunoLogado = (Aluno) httpSession.getAttribute("alunoLogado");

        if (alunoLogado != null) {
            List<OfertaEstagio> ofertasDisponiveis = ofertaEstagioService.buscarOfertasDisponiveis();

            // Filtrando as ofertas com base nos parâmetros opcionais
            if (minValue != null) {
                ofertasDisponiveis = ofertasDisponiveis.stream()
                        .filter(oferta -> oferta.getValorPago().doubleValue() >= minValue)
                        .collect(Collectors.toList());
            }
            if (maxValue != null) {
                ofertasDisponiveis = ofertasDisponiveis.stream()
                        .filter(oferta -> oferta.getValorPago().doubleValue() <= maxValue)
                        .collect(Collectors.toList());
            }
            if (habilidades != null && !habilidades.isEmpty()) {
                ofertasDisponiveis = ofertasDisponiveis.stream()
                        .filter(oferta -> oferta.getHabilidadesNecessarias().stream()
                                .anyMatch(habilidade -> habilidades.contains(habilidade.getDescription())))
                        .collect(Collectors.toList());
            }

            // Transformando as ofertas em DTOs e verificando candidaturas
            List<OfertaEstagioDTO> ofertasDTO = ofertasDisponiveis.stream()
                    .map(oferta -> {
                        OfertaEstagioDTO dto = new OfertaEstagioDTO(oferta);
                        boolean jaCandidatou = candidaturaService.existsByAlunoIdAndOfertaId(alunoLogado.getId(), oferta.getId());
                        dto.setJaCandidatou(jaCandidatou);
                        return dto;
                    })
                    .toList();

            modelAndView.addObject("aluno", alunoLogado);
            modelAndView.addObject("ofertas", ofertasDTO);
            modelAndView.setViewName("alunos/ofertas");
        } else {
            modelAndView.setViewName("alunos/login");
        }

        return modelAndView;
    }

    @GetMapping("/candidaturas")
    public ModelAndView listarCandidaturas(ModelAndView modelAndView) {
        Aluno alunoLogado = (Aluno) httpSession.getAttribute("alunoLogado");

        if (alunoLogado != null) {
            List<Candidatura> candidaturas = candidaturaService.buscarPorAluno(alunoLogado);

            modelAndView.addObject("candidaturas", candidaturas);
            modelAndView.addObject("aluno", alunoLogado);
            modelAndView.setViewName("alunos/candidaturas");
        } else {
            modelAndView.setViewName("alunos/login");
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

    @GetMapping("/logout")
    public String logout() {
        httpSession.invalidate();
        return "redirect:/";
    }

}