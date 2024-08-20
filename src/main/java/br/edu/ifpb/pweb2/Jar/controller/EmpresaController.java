package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/cadastro")
    public ModelAndView showCadastroForm(ModelAndView modelAndView) {
        modelAndView.addObject("empresa", new Empresa());
        modelAndView.setViewName("empresa/cadastro");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarEmpresa(@Validated @ModelAttribute("empresa") Empresa empresa,
                                   BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            modelAndView.setViewName("empresa/cadastro");
            return modelAndView;
        }
        empresaService.save(empresa);
        modelAndView.setViewName("redirect:/empresas");
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView listarEmpresas(ModelAndView modelAndView) {
        List<Empresa> empresas = empresaService.findAll();
        modelAndView.addObject("empresas", empresas);
        modelAndView.setViewName("empresa/list");
        return modelAndView;
    }

}
