package com.ramon.tarea3AD2024base.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ramon.tarea3AD2024base.Utils.VistaUtils;
import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

@Controller
public class RegistroController implements Initializable {

	@FXML
	private TextField usuarioRegis;
	@FXML
	private TextField nombreRegis;
	@FXML
	private TextField apellidosRegis;
	@FXML
	private TextField emailRegis;
	@FXML
	private TextField passwordRegis;
	@FXML
	private TextField cPasswordRegis;
	@FXML
	private ComboBox<String> choiceNacionalidad;
	@FXML
	private ComboBox<Parada> choiceParadas;
	@FXML
	private Button btnConfirmar;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnVisible1;
	@FXML
	private Button btnVisible2;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private ParadaService paradaService;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
		choiceParadas.setConverter(new StringConverter<>() {

			@Override
			public String toString(Parada parada) {
				if (parada != null) {
					return parada.getNombre() + "-" + parada.getRegion();
				}
				return "Seleccione parada inicial";
			}

			@Override
			public Parada fromString(String string) {
				return null;
			}

		});
		llenarChoiceConParadas();
		llenarChoiceConNaciones();

		choiceParadas.getItems().add(0, null);
		choiceParadas.setValue(null);

		choiceParadas.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() == 0) {
				choiceParadas.setValue(null);
			}
		});

	}

	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	@FXML
	private void salir() {
		VistaUtils.Salir();
	}

	@FXML
	private void registrar() {

	}
	
	private void llenarChoiceConNaciones() {
		List<String> naciones = leerNaciones();
		choiceNacionalidad.getItems().clear();
		choiceNacionalidad.getItems().addAll(naciones);
	}

	private void llenarChoiceConParadas() {
		List<Parada> paradas = paradaService.findAll();
		choiceParadas.getItems().clear();
		choiceParadas.getItems().addAll(paradas);
	}
	
	private List<String> leerNaciones() {
		File naciones = new File("src/main/resources/readOnly/paises.xml");
		List<String> listNaciones = new ArrayList<>();
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
	            String nombreNacion = pais.getElementsByTagName("nombre").item(0).getTextContent();
	            
	            listNaciones.add(nombreNacion);
	            
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
	    return listNaciones;
	}
	
	
//	@FXML
//	public void registrarPeregrino() {
//		if (validate("Nombre de Usuario", getUsuarioReg(), "[a-zA-Z]+") 
//				&& !regionParada.getText().isEmpty())
//		{
//
//			if (userId.getText() == null || userId.getText() == "") {
//				if (validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
//						&& validacionVacia("Password", getPassword().isEmpty()) 
//						&& validacionVacia("PasswordConf", getPasswordConf().isEmpty())
//						&& validacionVacia("Usuario Responsable", getUsuarioResponsable().isEmpty())
//						&& validacionVacia("Nombre Responsable", getNombreResponsable().isEmpty())) {
//
//					Usuario user = new Usuario();
//					user.setNombre(getUsuarioResponsable());
//					user.setEmail(getEmail());
//					user.setPassword(getPassword());
//					user.setPerfil(Perfil.PARADA);
//					
//					Parada parada = new Parada();
//					parada.setNombre(getNombreParada());
//					parada.setRegion(getRegionParada());
//					parada.setResponsable(getNombreResponsable());
//					
//
//					Usuario newUser = userService.save(user);
//					
//
//					guardarAlerta(newUser);
//				}
//
//			} else {
//				Usuario user = userService.find(Long.parseLong(userId.getText()));
//				user.setNombre(getNombreParada());
//				Usuario updatedUser = userService.update(user);
//				actualizarAlerta(updatedUser);
//			}
//
//			
//		}
//
//	}
	
	

	public String getUsuarioRegis() {
		return usuarioRegis.getText();
	}

	public void setUsuarioRegis(TextField usuarioRegis) {
		this.usuarioRegis = usuarioRegis;
	}

	public String getNombreRegis() {
		return nombreRegis.getText();
	}

	public void setNombreRegis(TextField nombreRegis) {
		this.nombreRegis = nombreRegis;
	}

	public String getApellidosRegis() {
		return apellidosRegis.getText();
	}

	public void setApellidosRegis(TextField apellidosRegis) {
		this.apellidosRegis = apellidosRegis;
	}

	public String getEmailRegis() {
		return emailRegis.getText();
	}

	public void setEmailRegis(TextField emailRegis) {
		this.emailRegis = emailRegis;
	}

	public String getPasswordRegis() {
		return passwordRegis.getText();
	}

	public void setPasswordRegis(TextField passwordRegis) {
		this.passwordRegis = passwordRegis;
	}

	public String getcPasswordRegis() {
		return cPasswordRegis.getText();
	}

	public void setcPasswordRegis(TextField cPasswordRegis) {
		this.cPasswordRegis = cPasswordRegis;
	}

	public ComboBox<String> getChoiceNacionalidad() {
		return choiceNacionalidad;
	}

	public void setChoiceNacionalidad(ComboBox<String> choiceNacionalidad) {
		this.choiceNacionalidad = choiceNacionalidad;
	}

	public ComboBox<Parada> getChoiceParadas() {
		return choiceParadas;
	}

	public void setChoiceParadas(ComboBox<Parada> choiceParadas) {
		this.choiceParadas = choiceParadas;
	}

	public ParadaService getParadaService() {
		return paradaService;
	}

	public void setParadaService(ParadaService paradaService) {
		this.paradaService = paradaService;
	}



	
	

}
