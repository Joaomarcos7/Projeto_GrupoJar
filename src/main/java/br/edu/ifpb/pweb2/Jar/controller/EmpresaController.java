package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.dto.*;
import br.edu.ifpb.pweb2.Jar.service.CandidaturaService;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @Autowired
    private CandidaturaService candidaturaService;

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("empresas/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("cnpj") String cnpj,
                              ModelAndView model) {

        Empresa empresa = empresaService.findByCnpj(cnpj);

        if (empresa != null) {
                model.setViewName("redirect:/empresas/" + empresa.getId() + "/menu");
        } else {
            model.addObject("error", "Empresa não encontrada. Tente Novamente!");
            model.setViewName("empresas/login");
        }
        return model;
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
            modelAndView.setViewName("empresas/form");
            return modelAndView;
        }
        Empresa empresaSalva = empresaService.save(empresa);
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro conclúido com sucesso!");
        modelAndView.setViewName("redirect:/empresas/" + empresaSalva.getId() + "/menu");
        return modelAndView;
    }

    @GetMapping("/{id}/menu")
    public ModelAndView detalhesEmpresa(@PathVariable Long id, ModelAndView modelAndView) {
        Optional<Empresa> empresaOptional = empresaService.findById(id);
        modelAndView.setViewName("empresas/form");

        if (empresaOptional.isPresent()) {
            modelAndView.addObject("empresa", empresaOptional.get());
            modelAndView.setViewName("empresas/menu");
        }
        return modelAndView;
    }

    @GetMapping("/{id}/ofertas")
    public ModelAndView listarOfertas(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Optional<Empresa> empresaOptional = empresaService.findById(id);

        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            List<OfertaEstagioDTO> ofertas = empresa.getOfertaEstagios().stream()
                    .map(OfertaEstagioDTO::new)
                    .collect(Collectors.toList());
            modelAndView.addObject("empresa", empresa);
            modelAndView.addObject("ofertas", ofertas);
            modelAndView.setViewName("empresas/ofertas");
        } else {
            modelAndView.addObject("mensagem", "Empresa não encontrada.");
            modelAndView.setViewName("empresas/menu");
        }
        return modelAndView;
    }

    @GetMapping("/{empresaId}/ofertas/{ofertaId}/alunos")
    public ModelAndView consultarAlunosPorOferta(@PathVariable Long empresaId, @PathVariable Long ofertaId) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Empresa> empresaOptional = empresaService.findById(empresaId);
        Optional<OfertaEstagio> ofertaOptional = ofertaEstagioService.findById(ofertaId);
    

        // Buscar todas as candidaturas para a oferta de estágio
        List<Candidatura> candidaturas = candidaturaService.buscarPorOferta(ofertaOptional.get());

        // Filtrar alunos que ainda estão com candidatura pendente
        List<AlunoDTO> alunosNaoSelecionados = candidaturas.stream()
            .filter(candidatura -> candidatura.getEstado() == EstadoCandidatura.PENDENTE)
            .map(candidatura -> new AlunoDTO(candidatura.getAluno()))
            .collect(Collectors.toList());

        modelAndView.addObject("empresa", empresaOptional.get());
        modelAndView.addObject("oferta", ofertaOptional.get());
        modelAndView.addObject("alunos", alunosNaoSelecionados);
        modelAndView.setViewName("empresas/oferta-aluno");

        return modelAndView;
    }

    @GetMapping()
    public ModelAndView listarEmpresas(ModelAndView modelAndView) {
        List<Empresa> empresas = empresaService.findAll();
        modelAndView.addObject("empresas", empresas);
        modelAndView.setViewName("empresas/list");
        return modelAndView;
    }

}
