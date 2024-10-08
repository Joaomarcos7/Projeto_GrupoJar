package br.edu.ifpb.pweb2.Jar.controller;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.StatusOfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.dto.OfertaEstagioDTO;
import br.edu.ifpb.pweb2.Jar.service.EmpresaService;
import br.edu.ifpb.pweb2.Jar.service.OfertaEstagioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ofertas")
public class OfertaEstagioController {

    @Autowired
    private OfertaEstagioService ofertaEstagioService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private EmpresaService empresaService;

    @GetMapping()
    public ModelAndView listarTodasOfertas(ModelAndView modelAndView) {
        List<OfertaEstagio> ofertas = ofertaEstagioService.findAll();

        List<OfertaEstagioDTO> ofertasDTO = ofertas.stream()
                .map(OfertaEstagioDTO::new)
                .toList();

        modelAndView.addObject("ofertasNegada", ofertasDTO.stream().filter(x-> x.getStatusName().equals("NEGADO")).toList());
        modelAndView.addObject("ofertasPendente", ofertasDTO.stream().filter(x-> x.getStatusName().equals("PENDENTE")).toList());
        modelAndView.addObject("ofertasAprovada", ofertasDTO.stream().filter(x-> x.getStatusName().equals("APROVADO")).toList());
        modelAndView.setViewName("ofertas/list");
        return modelAndView;
    }

    @GetMapping("/empresa/cadastro")
    public ModelAndView exibirFormularioCadastroOferta(ModelAndView modelAndView) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");

        modelAndView.addObject("empresa", empresaLogada);
        modelAndView.addObject("empresaId", empresaLogada.getId());
        modelAndView.addObject("oferta", new OfertaEstagio());
        modelAndView.setViewName("ofertas/cadastro");
        return modelAndView;
    }

    @PostMapping("/empresa/cadastro")
    public ModelAndView cadastrarOferta(
                                        OfertaEstagio ofertaEstagio,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");

        if (empresaLogada != null) {
            ofertaEstagio.setEmpresa(empresaLogada);
            ofertaEstagio.setDataPublicacao(LocalDate.now());
            ofertaEstagio.setStatus(StatusOfertaEstagio.PENDENTE.getStatus());

            empresaLogada.getOfertaEstagios().add(ofertaEstagio);
            ofertaEstagioService.save(ofertaEstagio);

            redirectAttributes.addFlashAttribute("mensagem", "Oferta cadastrada com sucesso!");
            modelAndView.setViewName("redirect:/empresas/ofertas");
        } else {
            modelAndView.addObject("mensagem", "Empresa não encontrada.");
            modelAndView.setViewName("empresas/login");
        }
        return modelAndView;
    }

    @PostMapping("/cancelar/{id}")
    public ModelAndView cancelarOferta(@PathVariable Long id,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        Empresa empresaLogada = (Empresa) httpSession.getAttribute("empresaLogada");
        Optional<OfertaEstagio> ofertaEstagioOptional = ofertaEstagioService.findById(id);

        if (ofertaEstagioOptional.isPresent()) {
            OfertaEstagio ofertaEstagio = ofertaEstagioOptional.get();

            empresaLogada.getOfertaEstagios().remove(ofertaEstagio);
            ofertaEstagioService.delete(ofertaEstagio);

            redirectAttributes.addFlashAttribute("message", "Oferta cancelada com sucesso.");
            modelAndView.setViewName("redirect:/empresas/ofertas");
        } else {
            redirectAttributes.addFlashAttribute("error", "Oferta não encontrada.");
            modelAndView.setViewName("redirect:/empresas/menu");
        }

        return modelAndView;
    }

    @PostMapping("updateStatus/{ofertaId}/{statusOferta}")
    public ModelAndView atualizarStatusOferta(@PathVariable Long ofertaId,
                                              @PathVariable int statusOferta,
                                              ModelAndView modelAndView,
                                              RedirectAttributes redirectAttributes){
        Optional<OfertaEstagio> ofertaEstagioOptional = ofertaEstagioService.findById(ofertaId);

        if (ofertaEstagioOptional.isPresent()) {
            OfertaEstagio oferta = ofertaEstagioOptional.get();

            oferta.setStatus(statusOferta);
            ofertaEstagioService.save(oferta);
            redirectAttributes.addFlashAttribute("mensagem", "Oferta atualizada com sucesso!");
            modelAndView.setViewName("redirect:/coordenadores/menu");
        } else {
            modelAndView.setViewName("redirect:/coordenadores/ofertas/pendentes");
        }

        return modelAndView;
    }

}
