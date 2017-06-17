package com.editory.web.controller;

import java.io.IOException;
import java.io.InputStream;
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
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
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
	@RequestMapping("/recuerdo")
	public String recuerdo(Model model){
		return "recuerdo";
	}
	
	@RequestMapping("/subirArchivos")
	public String subirArchivos(@RequestParam("dato") List<MultipartFile> datos,
            RedirectAttributes redirectAttributes){
		
		DbxClientV2 dbxClient;
		DbxRequestConfig requestConfig;
		requestConfig = new DbxRequestConfig("g3stj3fo1bw6wji");
		dbxClient = new DbxClientV2(requestConfig, "X20MDsAeKYMAAAAAAAAAd2pijnx1RldL8bt_wtTm4HX3SZsOjJsH09_z12_JkSdD");
	
		
		for(MultipartFile dato : datos){
			
				
			String extension = dato.getOriginalFilename().split("\\.")[1];
			String nombre = dato.getOriginalFilename().split("\\.")[0];
			
			String dropboxPath = "/editory/"+nombre+"."+extension;
			
			
			try (InputStream in = (dato.getInputStream())) {
	            FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
	                .withMode(WriteMode.ADD)
	                .withClientModified(new Date())
	                .uploadAndFinish(in);
	
	            System.out.println(metadata.toStringMultiline());
	        } catch (UploadErrorException ex) {
	            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
	            System.exit(1);
	        } catch (DbxException ex) {
	            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
	            System.exit(1);
	        } catch (IOException ex) {
	            System.err.println("Error reading from file \"" + dato + "\": " + ex.getMessage());
	            System.exit(1);
	        }
		}
		
		
		return "redirect:/index.html";
	}
}
