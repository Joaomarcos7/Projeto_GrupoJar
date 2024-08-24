package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
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
                                         BindingResult result, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            modelAndView.setViewName("empresas/form");
            return modelAndView;
        }
        Empresa empresaSalva = empresaService.save(empresa);
        redirectAttributes.addFlashAttribute("mensagem", "Cadastro conclúido com sucesso!");
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
        Optional<Empresa> empresaOptional = empresaService.findById(id);

        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            modelAndView.addObject("empresa", empresa);
            modelAndView.addObject("ofertas", empresa.getOfertaEstagios());
            modelAndView.setViewName("empresas/ofertas");
        } else {
            modelAndView.addObject("mensagem", "Empresa não encontrada.");
            modelAndView.setViewName("empresas/menu");
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
