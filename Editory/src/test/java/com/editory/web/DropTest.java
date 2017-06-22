package com.editory.web;

import static org.junit.Assert.*;

import org.junit.Test;

import com.editory.dropbox.DropboxManager;

public class DropTest {

	@Test
	public void testLinkCarpeta() {
		String nombre = "Thu Jun 22 20:29:40 CEST 20172029082081";
		String carpeta = DropboxManager.linkCarpeta(nombre);
		System.out.println(carpeta);
	}

}
