package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Coordenador;
import br.edu.ifpb.pweb2.Jar.service.CoordenadorService;
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

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("coordenadores/login");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              ModelAndView model) {

        Coordenador coordenador = coordenadorService.findByEmail(email);

        if (coordenador != null) {
            if (coordenador.getSenha().equals(password)) {
                model.setViewName("redirect:/coordenadores/" + coordenador.getId() + "/menu");
            } else {
                model.addObject("error", "Senha incorreta.");
                model.setViewName("coordenadores/login");
            }
        } else {
            model.addObject("error", "Coordenador não encontrado.");
            model.setViewName("coordenadores/login");
        }
        return model;
    }

    @GetMapping("/cadastro")
    public ModelAndView exibirFormularioDeCadastro(ModelAndView modelAndView) {
        modelAndView.addObject("coordenador", new Coordenador());
        modelAndView.setViewName("coordenadores/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarCoordenador(@Validated @ModelAttribute("coordenador") Coordenador coordenador,
                                       BindingResult result, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            modelAndView.setViewName("coordenadores/form");
            return modelAndView;
        }
        coordenadorService.save(coordenador);
        redirectAttributes.addFlashAttribute("mensagem", "Bem vindo(a)!");
        modelAndView.addObject("coordenador", coordenador);
        modelAndView.setViewName("redirect:/coordenadores/" + coordenador.getId() + "/menu");
        return modelAndView;
    }

    @GetMapping("/{id}/menu")
    public ModelAndView exibirMenu(@PathVariable("id") Long id, ModelAndView modelAndView) {
        Optional<Coordenador> coordenadorOptional = coordenadorService.findById(id);
        if (coordenadorOptional.isPresent()) {
            Coordenador coordenador = coordenadorOptional.get();
            modelAndView.addObject("coordenador", coordenador);
            modelAndView.setViewName("coordenadores/menu");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/form");
        }
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView listarCoordenadores(ModelAndView modelAndView) {
        List<Coordenador> coordenadores = coordenadorService.findAll();
        modelAndView.addObject("coordenadores", coordenadores);
        modelAndView.setViewName("coordenadores/list");
        return modelAndView;
    }
}
