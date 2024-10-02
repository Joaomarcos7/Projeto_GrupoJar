package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.*;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
import br.edu.ifpb.pweb2.Jar.model.pagination.NavPage;
import br.edu.ifpb.pweb2.Jar.model.pagination.NavePageBuilder;
import br.edu.ifpb.pweb2.Jar.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private EstagioService estagioService;

    @GetMapping()
    public ModelAndView listarCoordenadores(ModelAndView modelAndView) {
        List<Coordenador> coordenadores = coordenadorService.findAll();
        modelAndView.addObject("coordenadores", coordenadores);
        modelAndView.setViewName("coordenadores/list");
        return modelAndView;
    }

    @GetMapping("/estagios")
    public ModelAndView estagios(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 ModelAndView modelAndView) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Estagio> estagiosPage = estagioService.findAll(pageable);

        NavPage navPage = NavePageBuilder.newNavPage(estagiosPage.getNumber() + 1,
                estagiosPage.getTotalElements(), estagiosPage.getTotalPages(), size);

        modelAndView.addObject("estagios", estagiosPage.getContent());
        modelAndView.addObject("navPage", navPage);
        modelAndView.setViewName("coordenadores/estagios");
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

    // Ofertas pendentes
    @GetMapping("/ofertas/pendentes")
    public ModelAndView listarOfertasPendentes(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "5") int size,
                                               ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<OfertaEstagio> ofertasPage = ofertaEstagioService.findAllPaged(paging);

            List<OfertaEstagioDTO> ofertasPendentes = ofertasPage.getContent().stream()
                    .map(OfertaEstagioDTO::new)
                    .filter(oferta -> oferta.getStatusName().equals("PENDENTE"))
                    .toList();

            NavPage navPage = NavePageBuilder.newNavPage(ofertasPage.getNumber() + 1,
                    ofertasPage.getTotalElements(), ofertasPage.getTotalPages(), size);

            modelAndView.addObject("ofertasPendentes", ofertasPendentes);
            modelAndView.addObject("navPage", navPage);
            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.setViewName("ofertas/ofertas-pendentes");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }

    // Ofertas negadas
    @GetMapping("/ofertas/negadas")
    public ModelAndView listarOfertasNegadas(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "5") int size,
                                             ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<OfertaEstagio> ofertasPage = ofertaEstagioService.findAllPaged(paging);

            List<OfertaEstagioDTO> ofertasNegadas = ofertasPage.getContent().stream()
                    .map(OfertaEstagioDTO::new)
                    .filter(oferta -> oferta.getStatusName().equals("NEGADO"))
                    .toList();

            NavPage navPage = NavePageBuilder.newNavPage(ofertasPage.getNumber() + 1,
                    ofertasPage.getTotalElements(), ofertasPage.getTotalPages(), size);

            modelAndView.addObject("ofertasNegadas", ofertasNegadas);
            modelAndView.addObject("navPage", navPage);
            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.setViewName("ofertas/ofertas-negadas");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }

    // Ofertas aprovadas
    @GetMapping("/ofertas/aprovadas")
    public ModelAndView listarOfertasAprovadas(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "5") int size,
                                               ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<OfertaEstagio> ofertasPage = ofertaEstagioService.findAllPaged(paging);

            List<OfertaEstagioDTO> ofertasAprovadas = ofertasPage.getContent().stream()
                    .map(OfertaEstagioDTO::new)
                    .filter(oferta -> oferta.getStatusName().equals("APROVADO"))
                    .toList();

            NavPage navPage = NavePageBuilder.newNavPage(ofertasPage.getNumber() + 1,
                    ofertasPage.getTotalElements(), ofertasPage.getTotalPages(), size);

            modelAndView.addObject("ofertasAprovadas", ofertasAprovadas);
            modelAndView.addObject("navPage", navPage);
            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.setViewName("ofertas/ofertas-aprovadas");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/login");
        }
        return modelAndView;
    }


    @GetMapping("/candidatos")
    public ModelAndView listarCandidatos(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "5") int size,
                                         ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            Pageable paging = PageRequest.of(page -1, size);
            Page<Candidatura> candidaturasPage = candidaturaService.buscarPorAlunosNaoSelecionadosPaginado(paging);
            NavPage navPage = NavePageBuilder.newNavPage(candidaturasPage.getNumber() + 1,
                    candidaturasPage.getTotalElements(), candidaturasPage.getTotalPages(), size);

            modelAndView.addObject("coordenador", coordenadorLogado);
            modelAndView.addObject("candidaturas", candidaturasPage);
            modelAndView.addObject("navPage", navPage);
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
    public ModelAndView listarEmpresas(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       ModelAndView modelAndView) {
        Coordenador coordenadorLogado = (Coordenador) httpSession.getAttribute("coordenadorLogado");

        if (coordenadorLogado != null) {
            Pageable paging = PageRequest.of(page -1, size);

            Page<Empresa> empresasPage = empresaService.findAllPaged(paging);

            NavPage navPage = NavePageBuilder.newNavPage(empresasPage.getNumber() + 1,
                    empresasPage.getTotalElements(), empresasPage.getTotalPages(), size);

            modelAndView.addObject("empresas", empresasPage);
            modelAndView.addObject("navPage", navPage);
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
