package com.ramon.tarea3AD2024base.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

@Controller
public class PeregrinoController implements Initializable {
	
	@FXML
	private TextField nombre;
	@FXML
	private TextField apellidos;
	@FXML
	private TextField usuario;
	@FXML
	private TextField email;
	@FXML
	private DatePicker fechaNac;
	@FXML
	private ComboBox<String> nacionalidad;
	@FXML
	private TableView<Estancia> estancias;
	@FXML
	private TableColumn<Estancia, Long> idEstancia;

	@FXML
	private TableColumn<Parada, String> nombreParada;

	@FXML
	private TableColumn<Parada, Character> regionParada;

	@FXML
	private TableColumn<Estancia, LocalDate> fecha;

	@FXML
	private TableColumn<Estancia, Boolean> vip;
	
	@Lazy
	@Autowired
	private StageManager stagemanager;
	@Autowired
	private PeregrinoService peregrinoService;
	
	@FXML
	private void activarEdicion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Editar datos");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere editar sus datos?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			nombre.setEditable(true);
			apellidos.setEditable(true);
			email.setEditable(true);
			usuario.setEditable(true);
			fechaNac.setEditable(true);
			nacionalidad.setEditable(true);
			
			fechaNac.setDisable(false);
			nacionalidad.setDisable(false);
		} 
	}
	
	@FXML
	private void actualizarDatosPeregrino() {
		
	}
	
	@FXML
	private void volver() {
		stagemanager.switchScene(FxmlView.INICIO);
	}
	
	private void llenarNaciones() {
		List<String> naciones = leerNaciones();
		nacionalidad.getItems().clear();
		nacionalidad.getItems().addAll(naciones);
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Usuario usuarioSesion = InicioController.sesion.getUsuario();
		System.out.println(usuarioSesion.getNombre().toString());
		
		Peregrino peregrino = peregrinoService.findByUsuario(usuarioSesion);
		
		nombre.setText(peregrino.getNombre());
		apellidos.setText(peregrino.getApellidos());
		usuario.setText(usuarioSesion.getNombre());
		email.setText(usuarioSesion.getEmail());
		fechaNac.setValue(peregrino.getFechaNac());
		nacionalidad.setValue(peregrino.getNacionalidad());
		
		
		llenarNaciones();
		
		
		
		
	}

}
