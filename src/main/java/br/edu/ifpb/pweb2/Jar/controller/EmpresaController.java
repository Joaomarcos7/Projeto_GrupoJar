package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.Habilidade;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @GetMapping("/cadastro")
    public ModelAndView showCadastroForm(ModelAndView modelAndView) {
        modelAndView.addObject("empresa", new Empresa());
        modelAndView.setViewName("empresas/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarEmpresa(@Validated @ModelAttribute("empresa") Empresa empresa,
                                   BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            modelAndView.setViewName("empresas/form");
            return modelAndView;
        }
        Empresa empresaSalva = empresaService.save(empresa);
        modelAndView.setViewName("redirect:/ofertas/" + empresaSalva.getId() + "/cadastro");
        return modelAndView;
    }

    @GetMapping("/{id}/ofertas")
    public ModelAndView listarOfertas(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Optional<Empresa> empresaOptional = empresaService.findById(id);

        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            modelAndView.addObject("empresa", empresa);
            modelAndView.setViewName("empresas/ofertas");
        } else {
            // Lógica para tratar quando a empresa não é encontrada
            modelAndView.setViewName("empresas/list");
        }

        return modelAndView;
    }

    @GetMapping("{id}/ofertas/nova")
    public ModelAndView showOfertaForm(@PathVariable("id") Long id, ModelAndView modelAndView) {
        modelAndView.addObject("oferta", new OfertaEstagio());
        modelAndView.addObject("idEmpresa", id);
        modelAndView.setViewName("ofertas/cadastro");
        return modelAndView;
    }


    @GetMapping("/list")
    public ModelAndView listarEmpresas(ModelAndView modelAndView) {
        List<Empresa> empresas = empresaService.findAll();
        modelAndView.addObject("empresas", empresas);
        modelAndView.setViewName("empresas/list");
        return modelAndView;
    }

}
