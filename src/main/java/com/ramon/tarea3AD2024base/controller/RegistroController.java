package com.ramon.tarea3AD2024base.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.services.CarnetService;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.services.VisitaService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

@Controller
public class RegistroController implements Initializable {
	private boolean mostrarContraseña = true;

	@FXML
	private TextField usuario;
	@FXML
	private TextField nombre;
	@FXML
	private TextField apellidos;
	@FXML
	private TextField email;
	@FXML
	private TextField password;
	@FXML
	private TextField cPassword;
	@FXML
	private ComboBox<String> choiceNacionalidad;
	@FXML
	private ComboBox<Parada> choiceParadas;
	@FXML
	private Button btnConfirmar;
	@FXML
	private Button btnCancelar;
	@FXML
	private ImageView btnVisible1;
	@FXML
	private ImageView btnVisible2;
	@FXML
	private TextField passwordVisible1;
	@FXML
	private TextField passwordVisible2;
	@FXML
	private DatePicker fechaNac;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private ParadaService paradaService;

	@Autowired
	private PeregrinoService peregrinoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private VisitaService visitaService;

	@Autowired
	private CarnetService carnetService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		Image iconoCerrado = new Image(getClass().getResourceAsStream("/img/ojoCerrado.png"));
		Image iconoAbierto = new Image(getClass().getResourceAsStream("/img/ojoAbierto.png"));

		btnVisible1.setOnMouseClicked(event -> {
			mostrarContraseña = !mostrarContraseña;

			if (mostrarContraseña) {

				btnVisible1.setImage(iconoAbierto);
				passwordVisible1.setText(password.getText());
				passwordVisible1.setVisible(true);
				password.setVisible(false);
			} else {

				btnVisible1.setImage(iconoCerrado);
				password.setText(passwordVisible1.getText());
				passwordVisible1.setVisible(false);
				password.setVisible(true);
			}
		});

		btnVisible2.setOnMouseClicked(event -> {
			mostrarContraseña = !mostrarContraseña;

			if (mostrarContraseña) {

				btnVisible2.setImage(iconoAbierto);
				passwordVisible2.setText(cPassword.getText());
				passwordVisible2.setVisible(true);
				cPassword.setVisible(false);
			} else {

				btnVisible2.setImage(iconoCerrado);
				cPassword.setText(passwordVisible2.getText());
				passwordVisible2.setVisible(false);
				cPassword.setVisible(true);
			}
		});

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
	private void confirmar() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Registro de peregrino");
		alert.setHeaderText("¿Esta seguro de que estos son sus datos?");
		alert.showAndWait();

		if (true) {
			registrarPeregrino();

		}
	}

	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	@FXML
	private void salir() {
		VistaUtils.Salir();
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

	@FXML
	public void registrarPeregrino() {
		if (validate("Nombre de Usuario", getUsuario(), "[a-zA-Z]+")) {

			{
				if (validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
						&& validacionVacia("Password", getPassword().isEmpty())
						&& validacionVacia("PasswordConf", getCPassword().isEmpty())
						&& validacionVacia("Usuario", getUsuario().isEmpty())
						&& validacionVacia("Nombre", getNombre().isEmpty())
						&& validacionVacia("Apellidos", getApellidos().isEmpty())
						&& validacionVacia("Email", getEmail().isEmpty())
						&& validacionVacia("Fecha nacimiento", getFechaNac() == null)) {

					if (!passwordsCoinciden(getPassword(), getCPassword())) {
						validacionAlerta("Contraseñas no coinciden", true);
						return;
					}
					if (choiceParadas.getValue() == null) {
						validacionAlerta("Parada inicial no seleccionada", true);
						return;
					}
					if (choiceNacionalidad.getValue() == null) {
						validacionAlerta("Nacionalidad no seleccionada", true);
						return;
					}
					try {
						Usuario usuario = new Usuario();
						usuario.setNombre(getUsuario());
						usuario.setEmail(getEmail());
						usuario.setPassword(getPassword());
						usuario.setPerfil(Perfil.PEREGRINO);

						Parada parada = getChoiceParadas().getValue();

						Peregrino peregrino = new Peregrino();
						peregrino.setNombre(getNombre());
						peregrino.setApellido(getApellidos());
						peregrino.setNacionalidad(choiceNacionalidad.getValue());
						peregrino.setFechaNac(fechaNac.getValue());
						
						
						Carnet carnet = new Carnet();
						carnet.setDistancia(0.0);
						carnet.setFechaExp(LocalDate.now());
						carnet.setnVips(0);
						carnet.setParadaInicial(parada);
						peregrino.setCarnet(carnet);
						
						Visita visita = new Visita();
						visita.setFecha(LocalDate.now());
						visita.setParada(parada);
						visita.setPeregrino(peregrino);

					
					//	Usuario nuevoUsusario = usuarioService.save(usuario);
						peregrino.setUsuario(usuario);
						Peregrino nuevoPeregrino = peregrinoService.save(peregrino);
						
		
						Visita nuevaVisita = visitaService.save(visita);
						Set<Visita> visitas = new HashSet<Visita>();
						visitas.add(nuevaVisita);
						
						
						peregrino.getParadasVisitadas().add(visita);
						
						
						VistaUtils.ExportarCarnet(nuevoPeregrino);

						stageManager.switchScene(FxmlView.PEREGRINO);

						
					} catch (Exception e) {
						e.printStackTrace();
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error durante registro");
						alert.setHeaderText("Se ha producido un error durante el registro");
						alert.show();
					}
				}
			}

		}
	}

	private boolean validate(String field, String value, String pattern) {
		if (!value.isEmpty()) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(value);
			if (m.find() && m.group().equals(value)) {
				return true;
			} else {
				validacionAlerta(field, false);
				return false;
			}
		} else {
			validacionAlerta(field, true);
			return false;
		}
	}

	private boolean validacionVacia(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validacionAlerta(field, true);
			return false;
		}
	}

	private void validacionAlerta(String field, boolean empty) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error de Validación");
		alert.setHeaderText(null);
		if (field.equals("choiceNacionalidad"))
			alert.setContentText("Porfavor selecciones una nacionalidad: " + field);
		else if (field.equals("choiceParadas"))
			alert.setContentText("Porfavor seleccione una parada de inicio:" + field);
		else {
			if (empty)
				alert.setContentText("Porfavor introduzca: " + field);
			else
				alert.setContentText("Porfavor introduzca:  " + field + " valido");
		}
		alert.showAndWait();
	}

//	private void guardarAlerta(Usuario user) {
//
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle("Ususario creado correctamente.");
//		alert.setHeaderText(null);
//		alert.setContentText("Usuario: " + user.getNombre() + " " + " creado correctamente ");
//		alert.showAndWait();
//	}

	private boolean passwordsCoinciden(String password, String cPassword) {
		if (cPassword.equals(password))
			return true;
		else
			return false;
	}

	public String getUsuario() {
		return usuario.getText();
	}

	public void setUsuario(TextField usuarioRegis) {
		this.usuario = usuarioRegis;
	}

	public String getNombre() {
		return nombre.getText();
	}

	public void setNombre(TextField nombreRegis) {
		this.nombre = nombreRegis;
	}

	public String getApellidos() {
		return apellidos.getText();
	}

	public void setApellidos(TextField apellidosRegis) {
		this.apellidos = apellidosRegis;
	}

	public String getEmail() {
		return email.getText();
	}

	public void setEmail(TextField emailRegis) {
		this.email = emailRegis;
	}

	public String getPassword() {
		return password.getText();
	}

	public void setPassword(TextField passwordRegis) {
		this.password = passwordRegis;
	}

	public String getCPassword() {
		return cPassword.getText();
	}

	public void setCPassword(TextField cPasswordRegis) {
		this.cPassword = cPasswordRegis;
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

	public DatePicker getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(DatePicker fechaNac) {
		this.fechaNac = fechaNac;
	}

}
