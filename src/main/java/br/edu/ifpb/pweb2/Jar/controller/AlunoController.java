package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.service.AlunoService;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/cadastro")
    public ModelAndView showCadastroForm(ModelAndView modelAndView) {
        modelAndView.addObject("aluno", new Aluno());
        modelAndView.setViewName("alunos/form");
        return modelAndView;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrarAluno(@Validated @ModelAttribute("aluno") Aluno aluno,
                                         BindingResult result, ModelAndView modelAndView) {
        if (result.hasErrors()) {
            modelAndView.setViewName("form");
            return modelAndView;
        }
        alunoService.save(aluno);
        modelAndView.setViewName("redirect:/alunos");
        return modelAndView;
    }

    @GetMapping()
    public ModelAndView listarAlunos(ModelAndView modelAndView) {
        List<Aluno> alunos = alunoService.findAll();
        modelAndView.addObject("alunos", alunos);
        modelAndView.setViewName("alunos/list");
        return modelAndView;
    }
}
