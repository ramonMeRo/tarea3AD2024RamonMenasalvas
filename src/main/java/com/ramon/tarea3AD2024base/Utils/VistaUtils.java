package com.ramon.tarea3AD2024base.Utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class VistaUtils {

	@Lazy
	@Autowired
	private static StageManager stageManager;

	public static void Salir() {
		Dialog<ButtonType> dialogo = new Dialog<>();
		dialogo.setTitle("Salir");
		dialogo.setHeaderText("¿Seguro que desea salir?");

		ButtonType btnSi = new ButtonType("SI");
		ButtonType btnNo = new ButtonType("NO");
		dialogo.getDialogPane().getButtonTypes().addAll(btnSi, btnNo);

		// Por lo que vi response viene del metodo ShowAndWait() y nos permite acceder
		// directamente al valor del boton pulsado
		dialogo.showAndWait().ifPresent(response -> {
			if (response == btnSi) {
				System.exit(0);
			}

		});
	}
	
	public static void leerNaciones() {
		File naciones = new File("@../readOnly/paises.xml");
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder constructorDocumento = dbf.newDocumentBuilder();
	        Document documento = constructorDocumento.parse(naciones);

	        NodeList listaPais = documento.getElementsByTagName("pais");
	        Element pais, id, nombre;
	        
	        int indicePais = 0;

	        while (indicePais < listaPais.getLength()) {
	            pais = (Element) listaPais.item(indicePais);

	            id = (Element) pais.getElementsByTagName("id").item(0);
	            nombre = (Element) pais.getElementsByTagName("nombre").item(0);
	            
	            indicePais++;
	        }

	    } catch (ParserConfigurationException e) {
	    	// TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (SAXException e) {
	    	// TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
