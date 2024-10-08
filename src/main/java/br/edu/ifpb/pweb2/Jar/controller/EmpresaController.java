package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.*;
import br.edu.ifpb.pweb2.Jar.model.dto.*;
import br.edu.ifpb.pweb2.Jar.model.pagination.NavPage;
import br.edu.ifpb.pweb2.Jar.model.pagination.NavePageBuilder;
import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CandidaturaService candidaturaService;

    @Autowired
    private EstagioService estagioService;

    @Autowired
    private AlunoService alunoService;



    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("empresas/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("cnpj") String cnpj,
                              @RequestParam("password") String password,
                              ModelAndView modelAndView) {

        Empresa empresa = empresaService.findByCnpj(cnpj);

        if (empresa != null) {
            if (true) {
                httpSession.setAttribute("empresaLogada", empresa);
                modelAndView.setViewName("redirect:/empresas/menu");
            } else {
                modelAndView.addObject("error", "Senha incorreta.");
                modelAndView.setViewName("empresas/login");
            } 
        } else {
            modelAndView.addObject("error", "Empresa não encontrada.");
            modelAndView.setViewName("empresas/login");
        }
        return modelAndView;
    }
    

    @GetMapping("/cadastro")
    public ModelAndView showCadastroForm(ModelAndView modelAndView) {
        modelAndView.addObject("empresa", new Empresa());
        modelAndView.setViewName("empresas/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarEmpresa(@Validated @ModelAttribute("empresa") Empresa empresa,
                                         BindingResult result, 
                                         ModelAndView modelAndView, 
                                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("mensagem", "Houve um erro no seu cadastro! Tente novamente");
            modelAndView.setViewName("empresas/form");
            return modelAndView;
        }

        empresaService.save(empresa);

        redirectAttributes.addFlashAttribute("mensagem", "Cadastro concluído com sucesso!");

        httpSession.setAttribute("empresaLogada", empresa);

        modelAndView.setViewName("redirect:/empresas/menu");
        return modelAndView;
    }

    @GetMapping("/menu")
    public ModelAndView detalhesEmpresa(ModelAndView modelAndView,@AuthenticationPrincipal UserDetails userDetails) {
            Empresa empresa= empresaService.findByUsername(userDetails.getUsername());
            httpSession.setAttribute("empresaLogada",empresa);
            modelAndView.addObject("empresa", empresa);
            modelAndView.setViewName("empresas/menu");
            return modelAndView;
    }

    @PostMapping("/aprovar-candidatos")
    public ModelAndView AprovarCandidatos(@RequestBody Map<String, Object> payload, ModelAndView modelAndView){
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");
        Long ofertaId = Long.valueOf(payload.get("ofertaId").toString());
        List<String> emails = (List<String>) payload.get("emails");
        System.out.println(emails);
        OfertaEstagio oferta = this.ofertaEstagioService.findById(ofertaId).orElseThrow();
        oferta.setStatus(4);
        this.ofertaEstagioService.save(oferta);
        Estagio estagio = new Estagio();
        estagio.setEmpresa(empresaLogada);
        estagio.setOferta(oferta);
        Set<Aluno> alunos = new HashSet<>();
        for(String email : emails){
            Candidatura candidatura = this.candidaturaService.buscarPorOferta(oferta).stream().filter(x->x.getAluno().getEmail().equals(email)).findFirst().orElseThrow();
            candidatura.setEstado(EstadoCandidatura.ACEITA);
            Aluno aluno = alunoService.findByEmail(email);
            alunos.add(aluno);
            this.candidaturaService.save((candidatura));
        }
        System.out.println(alunos);
        estagio.setAluno(alunos);
        estagio.setDataInicio(LocalDate.now());
        estagio.setValor(oferta.getValorPago());
        estagio.setDataFim(LocalDate.now().plusYears(1));
        this.estagioService.save(estagio);
        modelAndView.addObject("empresa",empresaLogada);
        modelAndView.setViewName("redirect:/empresas/menu");
        return modelAndView;
    }

    @GetMapping("/ofertas")
    public ModelAndView listarOfertas(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "3") int size,
                                      ModelAndView modelAndView) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");

        if (empresaLogada != null) {
            Pageable pageable = PageRequest.of(page - 1, size);

            Page<OfertaEstagio> ofertasPage = ofertaEstagioService.findByEmpresa(empresaLogada.getId(), pageable);
            List<OfertaEstagioDTO> ofertas = ofertasPage.getContent().stream()
                    .map(OfertaEstagioDTO::new)
                    .collect(Collectors.toList());

            NavPage navPage = NavePageBuilder.newNavPage(ofertasPage.getNumber() + 1,
                    ofertasPage.getTotalElements(), ofertasPage.getTotalPages(), size);

            modelAndView.addObject("empresa", empresaLogada);
            modelAndView.addObject("ofertas", ofertas);
            modelAndView.addObject("navPage", navPage);
            modelAndView.setViewName("empresas/ofertas");
        } else {
            modelAndView.addObject("mensagem", "Empresa não encontrada.");
            modelAndView.setViewName("empresas/login");
        }
        return modelAndView;
    }


    @GetMapping("/ofertas/{ofertaId}/alunos")
    public ModelAndView consultarAlunosPorOferta(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @PathVariable Long ofertaId,
                                                 ModelAndView modelAndView, Model model) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");
        Optional<OfertaEstagio> ofertaOptional = ofertaEstagioService.findById(ofertaId);

        if (ofertaOptional.isPresent()) {
            OfertaEstagio ofertaEstagio = ofertaOptional.get();

            Pageable paging = PageRequest.of(page - 1, size);

            // Buscar todas as candidaturas para a oferta de estágio
            Page<Candidatura> candidaturasPage = candidaturaService.buscarPorOfertaPaginado(ofertaEstagio, paging);

            // Filtrar alunos que ainda estão com candidatura pendente
            List<AlunoDTO> alunosNaoSelecionados = candidaturasPage.getContent().stream()
                    .filter(candidatura -> candidatura.getEstado() == EstadoCandidatura.PENDENTE)
                    .map(candidatura -> new AlunoDTO(candidatura.getAluno()))
                    .collect(Collectors.toList());

            NavPage navPage = NavePageBuilder.newNavPage(candidaturasPage.getNumber() + 1,
                    candidaturasPage.getTotalElements(), candidaturasPage.getTotalPages(), size);

            modelAndView.addObject("empresa", empresaLogada);
            modelAndView.addObject("oferta", ofertaEstagio);
            modelAndView.addObject("alunos", alunosNaoSelecionados);
            modelAndView.addObject("navPage", navPage);
            modelAndView.setViewName("empresas/oferta-aluno");
        } else {
            modelAndView.setViewName("auth/login");
        }

        return modelAndView;
    }

    @GetMapping()
    public ModelAndView listarEmpresas(ModelAndView modelAndView) {
        List<Empresa> empresas = empresaService.findAll();
        modelAndView.addObject("empresas", empresas);
        modelAndView.setViewName("empresas/list");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout() {
        httpSession.invalidate();
        return "redirect:/";
    }

}
