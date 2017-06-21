package com.editory.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.editory.dropbox.DropboxManager;
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
			@RequestParam String presupuesto,
			@RequestParam String carpeta,
			Model model) {
		
		HashMap<String, String> camposCorreo = new HashMap<>();
		camposCorreo.put("TIPO DE VIDEO:", "Recuerdo");
		camposCorreo.put("nombre", Nombre);
		camposCorreo.put("mail",Email);
		camposCorreo.put("telefono",Telefono);
		camposCorreo.put("inicial",TextoInicial);
		camposCorreo.put("final",TextoFinal);
		camposCorreo.put("cancion",Cancion);
		camposCorreo.put("sugerencias",Sugerencias);
		camposCorreo.put("preguntas",Preguntas);
		camposCorreo.put("presupuesto", presupuesto);
		camposCorreo.put("carpeta", carpeta);
		
		EmailSender mensajero = new EmailSender(camposCorreo);
		mensajero.enviar();
		
		
		return "redirect:/index.html";
		
	}
	
	
	///enviarCorreoRegalo
	
	@RequestMapping("/enviarCorreoRegalo")
	public String enviarCorreoRegalo(@RequestParam String Nombre,
			@RequestParam String Email,
			@RequestParam String Telefono,@RequestParam String TextoInicial,
			@RequestParam String TextoFinal,
			@RequestParam String Cancion,
			@RequestParam String Presupuesto,
			Model model) {
		
		HashMap<String, String> camposCorreo = new HashMap<>();

		camposCorreo.put("TIPO DE VIDEO", "Regalo");
		camposCorreo.put("nombre", Nombre);
		camposCorreo.put("mail",Email);
		camposCorreo.put("telefono",Telefono);
		camposCorreo.put("inicial",TextoInicial);
		camposCorreo.put("final",TextoFinal);
		camposCorreo.put("cancion",Cancion);
		camposCorreo.put("presupuesto",Presupuesto+"€");

		
		EmailSender mensajero = new EmailSender(camposCorreo);
		mensajero.enviar();
		
		
		return "redirect:/index.html";
		
	}
	
	
	
	@RequestMapping("/recuerdo")
	public String recuerdo(Model model){
		return "recuerdo";
	}
	
	@RequestMapping("/subirArchivos")
	public String subirArchivos(Model model, @RequestParam("dato") List<MultipartFile> datos,
            RedirectAttributes redirectAttributes, @RequestParam("duracion") String duracion){
		
		DbxClientV2 dbxClient;
		DbxRequestConfig requestConfig;
		requestConfig = new DbxRequestConfig("g3stj3fo1bw6wji");
		dbxClient = new DbxClientV2(requestConfig, "X20MDsAeKYMAAAAAAAAAd2pijnx1RldL8bt_wtTm4HX3SZsOjJsH09_z12_JkSdD");
	
		String carpeta = new Date().toString();
		carpeta = carpeta + Integer.toString(new Random().nextInt()	);	
		for(MultipartFile dato : datos){
			
				
			String extension = dato.getOriginalFilename().split("\\.")[1];
			String nombre = dato.getOriginalFilename().split("\\.")[0];
			
			
			
			String dropboxPath = "/"+carpeta+"/"+nombre+"."+extension;
			
			DropboxManager gestorDropbox = new DropboxManager();
			gestorDropbox.chunkedUploadFile(dbxClient, dato, dropboxPath);
			
		}
		
		int precioBarato = Integer.parseInt(duracion);
		int precioMedio=0, precioCaro=0;
		switch(precioBarato){
		case 75: precioMedio = 90; precioCaro = 190; break;
		case 100: precioMedio= 160; precioCaro = 250; break;
		case 200: precioMedio = 300; precioCaro = 400; break;
		}
		
		
		model.addAttribute("carpeta",carpeta);
		model.addAttribute("precioBarato", precioBarato);
		model.addAttribute("precioMedio", precioMedio);
		model.addAttribute("precioCaro", precioCaro);

		
		return "confirmaRecuerdo";
	}
	
	///confirmarRegalo
	@RequestMapping("/confirmarRegalo")
	public String confirmarRegalo(Model model,  @RequestParam("videos") String videos, @RequestParam("duracion") String duracion,@RequestParam("fotos") String fotos){
	
		int precioTotal = 70 + Integer.parseInt(videos)+ Integer.parseInt(duracion)+ Integer.parseInt(fotos);
		model.addAttribute("precioTotal", precioTotal);
		return "confirmaRegalo";
	
	}
}
