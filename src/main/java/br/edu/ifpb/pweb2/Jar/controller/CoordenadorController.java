package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.*;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.CoordenadorService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/coordenadores")
public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

    @Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @GetMapping()
    public ModelAndView listarCoordenadores(ModelAndView modelAndView) {
        List<Coordenador> coordenadores = coordenadorService.findAll();
        modelAndView.addObject("coordenadores", coordenadores);
        modelAndView.setViewName("coordenadores/list");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("coordenadores/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              ModelAndView modelAndView) {

        Coordenador coordenador = coordenadorService.findByEmail(email);

        if (coordenador != null) {
            if (coordenador.getSenha().equals(password)) {
                httpSession.setAttribute("coordenadorLogado", coordenador);
                modelAndView.setViewName("redirect:/coordenadores/menu");
            } else {
                modelAndView.addObject("error", "Senha incorreta.");
                modelAndView.setViewName("coordenadores/login");
            }
        } else {
            modelAndView.addObject("error", "Coordenador não encontrado.");
            modelAndView.setViewName("coordenadores/login");
        }
        return modelAndView;
    }

    @GetMapping("/menu")
    public ModelAndView exibirMenu(ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.setViewName("coordenadores/menu");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }

    @GetMapping("/cadastro")
    public ModelAndView exibirFormularioDeCadastro(ModelAndView modelAndView) {
        modelAndView.addObject("coordenador", new Coordenador());
        modelAndView.setViewName("coordenadores/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarCoordenador(@Validated @ModelAttribute("coordenador") Coordenador coordenador,
                                             BindingResult result, 
                                             ModelAndView modelAndView, 
                                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            modelAndView.setViewName("coordenadores/form");
            return modelAndView;
        }
        coordenadorService.save(coordenador);
        httpSession.setAttribute("coordenadorLogado", coordenador);
        redirectAttributes.addFlashAttribute("mensagem", "Bem vindo(a)!");
        modelAndView.setViewName("redirect:/coordenadores/menu");
        return modelAndView;
    }

    @GetMapping("/ofertas")
    public ModelAndView listarOfertasEstagio(ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            List<OfertaEstagio> ofertas = ofertaEstagioService.findAll();
            List<OfertaEstagioDTO> ofertasDTO = ofertas.stream()
                    .map(OfertaEstagioDTO::new)
                    .toList();

            modelAndView.addObject("ofertasNegada", ofertasDTO.stream().filter(x -> x.getStatusName().equals("NEGADO")).toList());
            modelAndView.addObject("ofertasPendente", ofertasDTO.stream().filter(x -> x.getStatusName().equals("PENDENTE")).toList());
            modelAndView.addObject("ofertasAprovada", ofertasDTO.stream().filter(x -> x.getStatusName().equals("APROVADO")).toList());
            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.setViewName("ofertas/list");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }

    @GetMapping("/candidatos")
    public ModelAndView listarCandidatos(ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            List<Candidatura> candidaturas = candidaturaService.buscarPorAlunosNaoSelecionados();
            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.addObject("candidaturas", candidaturas);
            modelAndView.setViewName("coordenadores/list-candidatos");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }

    @GetMapping("/candidato/{candidaturaId}")
    public ModelAndView verFichaCandidato(@PathVariable Long candidaturaId,
                                          ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");
        Optional<Candidatura> candidaturaOptional = candidaturaService.findById(candidaturaId);

        if (coordenadorLogado != null) {
            if (candidaturaOptional.isPresent()) {
                Candidatura candidatura = candidaturaOptional.get();

                modelAndView.addObject("coordenador", coordenadorLogado);
                modelAndView.addObject("candidatura", candidatura);
                modelAndView.addObject("aluno", candidatura.getAluno());
                modelAndView.addObject("empresa", candidatura.getOfertaEstagio().getEmpresa());
                modelAndView.addObject("ofertaEstagio", candidatura.getOfertaEstagio());
                modelAndView.setViewName("coordenadores/ficha-candidato");
            } else {
                modelAndView.addObject("error", "Candidatura não encontrada.");
                modelAndView.setViewName("redirect:/coordenadores/candidatos");
            }
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }

    @GetMapping("/empresas")
public ModelAndView listarEmpresas(ModelAndView modelAndView) {
    Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

    if (coordenadorLogado != null) {
        List<Empresa> empresas = empresaService.findAll();  // Supondo que empresaService já exista
        modelAndView.addObject("empresas", empresas);
        modelAndView.setViewName("coordenadores/list-empresas");
    } else {
        modelAndView.setViewName("redirect:/coordenadores/login");
    }
    return modelAndView;
}

@GetMapping("/edit-empresa/{id}")
public ModelAndView editarEmpresa(@PathVariable Long id, ModelAndView modelAndView) {
    System.out.println("ID recebido: " + id); // Log para verificar o ID
    Optional<Empresa> empresa = empresaService.findById(id);
    if (empresa.isPresent()) {
        modelAndView.addObject("empresa", empresa.get());
        modelAndView.setViewName("coordenadores/edit-empresa");
    } else {
        modelAndView.setViewName("redirect:/coordenadores/empresas");
    }
    return modelAndView;
}

@PostMapping("/edit-empresa")
public ModelAndView salvarEdicaoEmpresa(@Validated @ModelAttribute("empresa") Empresa empresa,
                                        BindingResult result,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
    if (empresa.getId() == null) {
        throw new IllegalArgumentException("ID da empresa não pode ser nulo.");
    }

    Empresa empresaExistente = empresaService.findById(empresa.getId()).orElseThrow();

    if (empresa.getSenha() == null || empresa.getSenha().isEmpty()) {
        empresa.setSenha(empresaExistente.getSenha());
    }

    empresaService.save(empresa);
    redirectAttributes.addFlashAttribute("mensagem", "Empresa editada com sucesso.");
    modelAndView.setViewName("redirect:/coordenadores/empresas");
    return modelAndView;
}
    @GetMapping("/logout")
    public String logout() {
        httpSession.invalidate(); 
        return "redirect:/";
    }
}
