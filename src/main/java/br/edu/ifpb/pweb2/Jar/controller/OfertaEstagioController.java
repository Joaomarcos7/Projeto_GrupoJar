package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/ofertas")
public class OfertaEstagioController {

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping()
    public ModelAndView listarTodasOfertas(ModelAndView modelAndView) {
        modelAndView.addObject("ofertas", ofertaEstagioService.findAll());
        modelAndView.setViewName("ofertas/list");
        return modelAndView;
    }

    @GetMapping("empresa/{empresaId}/cadastro")
    public ModelAndView exibirFormularioCadastroOferta(@PathVariable Long empresaId, ModelAndView modelAndView) {
        Optional<Empresa> empresa = empresaService.findById(empresaId);
        modelAndView.addObject("empresa", empresa);
        modelAndView.addObject("empresaId", empresaId);
        modelAndView.addObject("oferta", new OfertaEstagio());
        modelAndView.setViewName("ofertas/cadastro");
        return modelAndView;
    }

    @PostMapping("empresa/{empresaId}/cadastro")
    public ModelAndView cadastrarOferta(@PathVariable Long empresaId,
                                        OfertaEstagio ofertaEstagio, ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        Optional<Empresa> empresaOptional = empresaService.findById(empresaId);

        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            ofertaEstagio.setEmpresa(empresa);

            ofertaEstagioService.save(ofertaEstagio);

            redirectAttributes.addFlashAttribute("mensagem", "Oferta cadastrada com sucesso!");

            modelAndView.setViewName("redirect:/empresas/" + empresa.getId() + "/ofertas");
        } else {
            modelAndView.setViewName("ofertas/cadastro");
        }

        return modelAndView;
    }

}
