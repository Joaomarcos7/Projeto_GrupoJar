package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

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
            model.addObject("error", "Empresa não encontrada.");
            model.setViewName("alunos/login");
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
                                   BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            modelAndView.setViewName("empresas/form");
            return modelAndView;
        }
        Empresa empresaSalva = empresaService.save(empresa);
        modelAndView.setViewName("redirect:/empresas/" + empresaSalva.getId() + "/detalhes");
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
        modelAndView.setViewName("empresas/ofertas");
        Optional<Empresa> empresaOptional = empresaService.findById(id);

        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            modelAndView.addObject("empresa", empresa);
            modelAndView.addObject("ofertas", empresa.getOfertaEstagios());
        } else {
            modelAndView.setViewName("menu");
            modelAndView.addObject("mensagem", "Empresa não encontrada.");
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

}
