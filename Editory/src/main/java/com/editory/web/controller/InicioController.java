package com.editory.web.controller;

import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.editory.web.mail.EmailSender;

@Controller
public class InicioController {

	@RequestMapping("/")
	public String inicio(Model model) {

		Random random = new Random(System.currentTimeMillis());
		model.addAttribute("userId", random.nextLong());

		//redirect:/TestStuff.html
		return "redirect:/index.html";
	}
	@RequestMapping("/enviarCorreo")
	public String enviarCorreo(@RequestParam String Nombre,
			@RequestParam String Email,
			@RequestParam String Telefono,
			@RequestParam String TextoInicial,
			@RequestParam String TextoFinal,
			@RequestParam String Cancion,
			@RequestParam String Sugerencias,
			@RequestParam String Preguntas,
			Model model) {
		
		HashMap<String, String> camposCorreo = new HashMap<>();
		camposCorreo.put("nombre", Nombre);
		camposCorreo.put("mail",Email);
		camposCorreo.put("telefono",Telefono);
		camposCorreo.put("inicial",TextoInicial);
		camposCorreo.put("final",TextoFinal);
		camposCorreo.put("cancion",Cancion);
		camposCorreo.put("sugerencias",Sugerencias);
		camposCorreo.put("preguntas",Preguntas);
		
		EmailSender mensajero = new EmailSender(camposCorreo);
		mensajero.enviar();
		
		
		return "redirect:/index.html";
		
	}
}
