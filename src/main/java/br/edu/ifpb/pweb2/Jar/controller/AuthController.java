package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.CoordenadorService;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
  private  AlunoService alunoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CoordenadorService coordenadorService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/cadastro-aluno")
    public ModelAndView showRegisterStudentForm(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/form-aluno");
        modelAndView.addObject("aluno", new Aluno());
        return modelAndView;
    }

    @PostMapping("/cadastro-aluno")
    public ModelAndView processRegistration(
            @Valid @ModelAttribute Aluno aluno,
            BindingResult bindingResult,
            ModelAndView modelAndView) {

        modelAndView.setViewName("auth/form-aluno");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        try {
            this.alunoService.save(aluno);
            modelAndView.setViewName("redirect:/auth/login");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }

    @GetMapping("cadastro-empresa")
    public ModelAndView showRegistrationForm(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/form-empresa");
        modelAndView.addObject("empresa", new Empresa());
        return modelAndView;
    }

    @PostMapping("/cadastro-empresa")
    public ModelAndView processRegistration(@Valid Empresa empresa, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("auth/form-empresa");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        try {
            this.empresaService.save(empresa);
            modelAndView.setViewName("redirect:/auth/login");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }

        return modelAndView;
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout";
    }
}