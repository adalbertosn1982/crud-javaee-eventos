package com.eventosapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventosapp.models.Convidado;
import com.eventosapp.models.Evento;
import com.eventosapp.repository.ConvidadoRepository;
import com.eventosapp.repository.EventoRepository;

@Controller
public class EventoController {
	
	
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	
	@RequestMapping("/")
	public ModelAndView index() {
		//return "index";
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}
	/*
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	*/
	
	
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.GET)
	// @RequestMapping("/cadastrarEvento")
	public String form() {
		return "evento/formEvento";
	}
	
	/*
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.POST)
	// @RequestMapping("/cadastrarEvento")
	public String formPost(Evento evento) {
		er.save(evento);
		return "redirect:/cadastrarEvento";
	}*/
	
	@RequestMapping(value="/cadastrarEvento", method = RequestMethod.POST)
	// @RequestMapping("/cadastrarEvento")
	public String formPost(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os Campos!");
			return "redirect:/cadastrarEvento";
		}
		
		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		return "redirect:/cadastrarEvento";
	}

	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		//ModelAndView mv = new ModelAndView("index");
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos);
		return mv;
	}
	/*
	@RequestMapping("/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento =  er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesevento");
		mv.addObject("evento", evento);
		return mv;
		
	}
	*/
	
	@RequestMapping(value="/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento =  er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		
		return mv;
		
	}
	
	@RequestMapping(value="/{codigo}",method = RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os Campos!");
			return "redirect:/{codigo}";
		}
		Evento evento =  er.findByCodigo(codigo);
		convidado.setEvento(evento);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
		
	}
	
	@RequestMapping(value="/deletarEvento", method = RequestMethod.GET)
	public String deletarEvento(long codigo) {
		Evento evento =  er.findByCodigo(codigo);
		er.delete(evento);
		return "redirect:/eventos";
	}
	
	
	
	@RequestMapping(value="/deletarConvidado", method = RequestMethod.GET)
	public String deletarConvidado(String rg) {
        Convidado convidado;
        convidado = cr.findByRg(rg);
        cr.delete(convidado);
		
        Evento evento =  convidado.getEvento();
		/*
		long codigoLong = evento.getCodigo();
		String codigo =""+codigoLong;
		*/
	    String codigo = Long.toString(evento.getCodigo());
		return "redirect:/"+codigo;
	}
	
	/*
	@RequestMapping(value="/deletarConvidado", method = RequestMethod.GET)
	public String deletarConvidado(HttpServletRequest request) {
        Convidado convidado;
        convidado = cr.findByRg(request.getParameter("rg"));
        cr.delete(convidado);
		
        Evento evento =  convidado.getEvento();
		long codigoLong = evento.getCodigo();
		String codigo =""+codigoLong;
        //String codigo = Long.toString(evento.getCodigo());
		return "redirect:/"+codigo;
	}
	*/
}
