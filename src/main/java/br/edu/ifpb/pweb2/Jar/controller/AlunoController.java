package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.*;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
import br.edu.ifpb.pweb2.Jar.model.pagination.NavPage;
import br.edu.ifpb.pweb2.Jar.model.pagination.NavePageBuilder;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.EstagioService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
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

    @Autowired
    private EstagioService estagioService;


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
            if (true) {
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

    @GetMapping("/estagio")
    public ModelAndView exibirEstagio(ModelAndView modelAndView) {
        Aluno alunoLogado = (Aluno) httpSession.getAttribute("alunoLogado");
        Estagio estagio  = this.estagioService.findByAluno(alunoLogado);
        System.out.println(estagio);
        modelAndView.addObject("estagio", estagio);
        modelAndView.setViewName("alunos/estagio");
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
    public ModelAndView exibirMenu(ModelAndView modelAndView, @AuthenticationPrincipal UserDetails userDetails) {
            Aluno aluno = alunoService.findByUsername(userDetails.getUsername());
            httpSession.setAttribute("alunoLogado",aluno);
            modelAndView.addObject("aluno", aluno);
            modelAndView.setViewName("alunos/menu");
            return modelAndView;
    }

    @GetMapping("/ofertas")
    public ModelAndView listarOfertasDisponiveis(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "6") int size,
                                                 @RequestParam(required = false) Double minValue,
                                                 @RequestParam(required = false) Double maxValue,
                                                 @RequestParam(required = false) List<String> habilidades,
                                                 ModelAndView modelAndView) {
        Aluno alunoLogado = (Aluno) httpSession.getAttribute("alunoLogado");

        if (alunoLogado != null) {
            Pageable paging = PageRequest.of(page - 1, size);

            Page<OfertaEstagio> ofertasDisponiveisPage = ofertaEstagioService.buscarOfertasDisponiveisPaginado(paging);
            List<OfertaEstagio> ofertasDisponiveis = ofertasDisponiveisPage.getContent();

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

            NavPage navPage = NavePageBuilder.newNavPage(ofertasDisponiveisPage.getNumber() + 1,
                    ofertasDisponiveisPage.getTotalElements(), ofertasDisponiveisPage.getTotalPages(), size);

            modelAndView.addObject("aluno", alunoLogado);
            modelAndView.addObject("ofertas", ofertasDTO);
            modelAndView.addObject("navPage", navPage);
            modelAndView.setViewName("alunos/ofertas");
        } else {
            modelAndView.setViewName("alunos/login");
        }

        return modelAndView;
    }

    @GetMapping("/candidaturas")
    public ModelAndView listarCandidaturas(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "6") int size,
                                           ModelAndView modelAndView) {
        Aluno alunoLogado = (Aluno) httpSession.getAttribute("alunoLogado");

        if (alunoLogado != null) {
            Pageable paging = PageRequest.of(page - 1, size);

            Page<Candidatura> candidaturasPage = candidaturaService.buscarPorAlunoPaginado(alunoLogado, paging);

            NavPage navPage = NavePageBuilder.newNavPage(candidaturasPage.getNumber() + 1,
                    candidaturasPage.getTotalElements(), candidaturasPage.getTotalPages(), size);

            modelAndView.addObject("candidaturas", candidaturasPage.getContent());
            modelAndView.addObject("aluno", alunoLogado);
            modelAndView.addObject("navPage", navPage);
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