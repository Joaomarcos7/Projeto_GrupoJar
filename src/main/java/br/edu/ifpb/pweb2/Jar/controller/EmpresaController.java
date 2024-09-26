package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.dto.*;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import jakarta.servlet.http.HttpSession;
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
            if (empresa.getSenha().equals(password)) {
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
    public ModelAndView detalhesEmpresa(ModelAndView modelAndView) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");

        if (empresaLogada != null) {
            modelAndView.addObject("empresa", empresaLogada);
            modelAndView.setViewName("empresas/menu");
        } else {
            modelAndView.setViewName("empresas/login");
        }
        return modelAndView;
    }

    @GetMapping("/ofertas")
    public ModelAndView listarOfertas(ModelAndView modelAndView) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");

        if (empresaLogada != null) {
            List<OfertaEstagioDTO> ofertas = empresaLogada.getOfertaEstagios().stream()
                    .map(OfertaEstagioDTO::new)
                    .collect(Collectors.toList());
            modelAndView.addObject("empresa", empresaLogada);
            modelAndView.addObject("ofertas", ofertas);
            modelAndView.setViewName("empresas/ofertas");
        } else {
            modelAndView.addObject("mensagem", "Empresa não encontrada.");
            modelAndView.setViewName("empresas/login");
        }
        return modelAndView;
    }

    @GetMapping("/ofertas/{ofertaId}/alunos")
    public ModelAndView consultarAlunosPorOferta(@PathVariable Long ofertaId,
                                                 ModelAndView modelAndView) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");
        Optional<OfertaEstagio> ofertaOptional = ofertaEstagioService.findById(ofertaId);

        if (ofertaOptional.isPresent()) {
            OfertaEstagio ofertaEstagio = ofertaOptional.get();

            // Buscar todas as candidaturas para a oferta de estágio
            List<Candidatura> candidaturas = candidaturaService.buscarPorOferta(ofertaEstagio);

            // Filtrar alunos que ainda estão com candidatura pendente
            List<AlunoDTO> alunosNaoSelecionados = candidaturas.stream()
                    .filter(candidatura -> candidatura.getEstado() == EstadoCandidatura.PENDENTE)
                    .map(candidatura -> new AlunoDTO(candidatura.getAluno()))
                    .collect(Collectors.toList());

            modelAndView.addObject("empresa", empresaLogada);
            modelAndView.addObject("oferta", ofertaEstagio);
            modelAndView.addObject("alunos", alunosNaoSelecionados);
            modelAndView.setViewName("empresas/oferta-aluno");
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
