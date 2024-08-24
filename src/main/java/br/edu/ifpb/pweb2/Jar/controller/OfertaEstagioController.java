package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.StatusOfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ofertas")
public class OfertaEstagioController {

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("")
    public ModelAndView listarTodasOfertas(ModelAndView modelAndView) {

        // Obtém a lista de ofertas
        List<OfertaEstagio> ofertas = ofertaEstagioService.findAll();

        // Converte cada oferta para DTO
        List<OfertaEstagioDTO> ofertasDTO = ofertas.stream()
                .map(OfertaEstagioDTO::new) // Usa o construtor que aplica a conversão
                .collect(Collectors.toList());

        // Adiciona a lista de DTOs ao modelo
        modelAndView.addObject("ofertas", ofertasDTO);
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
            ofertaEstagio.setDataPublicacao(LocalDate.now());
            ofertaEstagio.setStatus(StatusOfertaEstagio.PENDENTE.getStatus());

            empresa.getOfertaEstagios().add(ofertaEstagio);

            ofertaEstagioService.save(ofertaEstagio);

            redirectAttributes.addFlashAttribute("mensagem", "Oferta cadastrada com sucesso!");

            modelAndView.setViewName("redirect:/empresas/" + empresa.getId() + "/ofertas");
        } else {
            modelAndView.setViewName("ofertas/cadastro");
        }

        return modelAndView;
    }

    @PostMapping("{ofertaId}/atualizar")
    public ModelAndView atualizarStatusOferta(@PathVariable Long ofertaId,OfertaEstagio ofertaEstagio,ModelAndView modelAndView,
                                              RedirectAttributes redirectAttributes){
        Optional<OfertaEstagio> ofertaEstagioOptional = ofertaEstagioService.findById(ofertaId);

        if (ofertaEstagioOptional.isPresent()) {
            OfertaEstagio oferta = ofertaEstagioOptional.get();

            oferta.setStatus(ofertaEstagio.getStatus());
            ofertaEstagioService.save(oferta);
            redirectAttributes.addFlashAttribute("mensagem", "Oferta atualizada com sucesso!");
            modelAndView.setViewName("redirect:/ofertas");

        }
        modelAndView.setViewName("redirect:/ofertas");
        return modelAndView;
    }

}
