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
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.services.VisitaService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controlador de la ventana del Registro que tiene funciones para el registro
 * de un usuario peregrino.
 * 
 * @author Ramon
 * @since 01-01-2025
 */

@Controller
public class RegistroController implements Initializable {

	private boolean mostrarContraseña = true;

	@FXML
	public TextField usuario;
	@FXML
	public TextField nombre;
	@FXML
	public TextField apellidos;
	@FXML
	public TextField email;
	@FXML
	public TextField password;
	@FXML
	public TextField cPassword;
	@FXML
	public ComboBox<String> choiceNacionalidad;
	@FXML
	public ComboBox<Parada> choiceParadas;
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
	public DatePicker fechaNac;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	public ParadaService paradaService;

	@Autowired
	public PeregrinoService peregrinoService;

	@Autowired
	public VisitaService visitaService;

	private String contra = "";

	private String contra1 = "";

	public Sesion sesion;

	/**
	 * Inicializa la interfaz gráfica de Registro al cargar la vista. Configura los
	 * comportamientos de los campos de contraseña, los selectores de paradas y
	 * nacionalidades.
	 * 
	 * @param location  La ubicación utilizada para resolver rutas relativas a
	 *                  recursos de la raíz del objeto.
	 * 
	 * @param resources Los recursos utilizados para la localización de la interfaz
	 *                  de usuario.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		passwordVisible1.textProperty().bindBidirectional(password.textProperty());
		passwordVisible2.textProperty().bindBidirectional(cPassword.textProperty());

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

	/**
	 * Maneja la pulsación de la tecla F1 para mostrar la ayuda.
	 * 
	 * @param event Evento del teclado.
	 */
	public void ayudaF1(KeyEvent event) {
		if (event.getCode().toString().equals("F1")) {
			ayuda();
		}
	}

	/**
	 * Muestra la ventana de ayuda con el manual de usuario.
	 */
	@FXML
	private void ayuda() {
		try {
			WebView webView = new WebView();

			String url = getClass().getResource("/help/html/Registro.html").toExternalForm();
			webView.getEngine().load(url);

			Stage helpStage = new Stage();
			helpStage.setTitle("Ayuda");

			Scene helpScene = new Scene(webView, 850, 520);

			helpStage.setScene(helpScene);

			helpStage.initModality(Modality.APPLICATION_MODAL);
			helpStage.setResizable(true);
			helpStage.show();

		} catch (NullPointerException e) {
			System.out.print("No se ha encontrado el HTML");
		}
	}

	/**
	 * Muestra una alerta de confirmación para registrar un peregrino. Si el usuario
	 * acepta, llama al método registrarPeregrino().
	 */
	@FXML
	private void confirmar() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Registro de peregrino");
		alert.setHeaderText("¿Esta seguro de que estos son sus datos?");
		alert.showAndWait();

		if (true) {
			registrarPeregrino();

		}
	}

	/**
	 * Cambia la escena actual a la pantalla de inicio.
	 */
	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	/**
	 * Cierra la aplicación invocando el método de utilidad VistaUtils.
	 */
	@FXML
	private void salir() {
		VistaUtils.Salir();
	}

	/**
	 * Llena el ComboBox de nacionalidades con los valores obtenidos del archivo XML
	 * de países.
	 */
	private void llenarChoiceConNaciones() {
		List<String> naciones = leerNaciones();
		choiceNacionalidad.getItems().clear();
		choiceNacionalidad.getItems().addAll(naciones);
	}

	/**
	 * Llena el ComboBox de paradas con los valores obtenidos de la base de datos.
	 * Si no hay paradas disponibles, muestra una alerta de error y redirige a la
	 * pantalla de inicio.
	 */
	private void llenarChoiceConParadas() {
		List<Parada> paradas = paradaService.findAll();
		if (paradas.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Todavia no hay paradas en el sistema");
			alert.showAndWait();

			stageManager.switchScene(FxmlView.INICIO);
		} else {
			choiceParadas.getItems().clear();
			choiceParadas.getItems().addAll(paradas);
		}
	}

	/**
	 * Lee la lista de países desde un archivo XML y la devuelve en forma de lista
	 * de Strings.
	 *
	 * @return Una lista con los nombres de los países extraídos del archivo XML.
	 */
	private List<String> leerNaciones() {
		File naciones = new File("src/main/resources/readOnly/paises.xml");
		List<String> listNaciones = new ArrayList<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder constructorDocumento = dbf.newDocumentBuilder();
			Document documento = constructorDocumento.parse(naciones);

			NodeList listaPais = documento.getElementsByTagName("pais");
			@SuppressWarnings("unused")
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

	/**
	 * Registra un nuevo peregrino en el sistema. Valida los datos ingresados,
	 * verifica que los campos no estén vacíos y que la información cumpla con los
	 * formatos requeridos. Si las validaciones son exitosas, se crea un usuario, un
	 * peregrino, un carnet y se guarda una visita inicial en la base de datos.
	 * Finalmente, se exporta el carnet del peregrino y se muestra una alerta de
	 * éxito. En caso de error, se muestra una alerta informando el fallo.
	 */
	@FXML
	public void registrarPeregrino() {
		if (valida("Nombre de Usuario", getUsuario(), "^[a-zA-Z0-9]+$")
				&& valida("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
				&& valida("Password", getPassword(), "^\\S+$") && valida("PasswordConf", getCPassword(), "^\\S+$")
				&& getFechaNac().getValue().isBefore(LocalDate.now())) {

			{
				if (validacionVacia("Password", getPassword().isEmpty())
						&& validacionVacia("PasswordConf", getCPassword().isEmpty())
						&& validacionVacia("Usuario", getUsuario().isEmpty())
						&& validacionVacia("Nombre", getNombre().isEmpty())
						&& validacionVacia("Apellidos", getApellidos().isEmpty())
						&& validacionVacia("Email", getEmail().isEmpty())
						&& validacionVacia("Fecha nacimiento", getFechaNac().getValue() == null)
						&& validacionVacia("choiceNacionalidad", getChoiceNacionalidad().getValue() == null)
						&& validacionVacia("choiceParadas", getChoiceParadas().getValue() == null)) {

					if (!passwordsCoinciden(contra, contra1)) {
						validacionAlerta("Contraseñas no coinciden", true);
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

						peregrino.setUsuario(usuario);
						Peregrino nuevoPeregrino = peregrinoService.save(peregrino);

						Visita nuevaVisita = visitaService.save(visita);
						Set<Visita> visitas = new HashSet<Visita>();
						visitas.add(nuevaVisita);

						peregrino.getParadasVisitadas().add(visita);

						VistaUtils.ExportarCarnet(nuevoPeregrino);

						guardarAlerta(peregrino);

						stageManager.switchScene(FxmlView.INICIO);

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

	/**
	 * Muestra una alerta informando que el peregrino ha sido registrado
	 * exitosamente.
	 *
	 * @param peregrino El objeto Peregrino recién registrado.
	 */
	private void guardarAlerta(Peregrino peregrino) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usuario introducido correctamente.");
		alert.setHeaderText(null);
		alert.setContentText(
				"Usuario: " + peregrino.getNombre() + " " + peregrino.getApellidos() + " creado correctamente ");
		alert.showAndWait();
	}

	/**
	 * Valida un campo utilizando una expresión regular.
	 *
	 * @param campo  Nombre del campo a validar.
	 * @param valor  Valor ingresado en el campo.
	 * @param patron Expresión regular para validar el valor.
	 * @return true si el valor cumple con la expresión regular, false en caso
	 *         contrario.
	 */
	private boolean valida(String campo, String valor, String patron) {
		if (!valor.isEmpty()) {
			Pattern p = Pattern.compile(patron);
			Matcher m = p.matcher(valor);
			if (m.find() && m.group().equals(valor)) {
				return true;
			} else {
				validacionAlerta(campo, false);
				return false;
			}
		} else {
			validacionAlerta(campo, true);
			return false;
		}
	}

	/**
	 * Verifica si un campo está vacío y muestra una alerta si es necesario.
	 *
	 * @param field Nombre del campo a verificar.
	 * @param empty {@code true} si el campo está vacío, {@code false} en caso
	 *              contrario.
	 * @return true si el campo no está vacío, false si está vacío y muestra una
	 *         alerta.
	 */
	private boolean validacionVacia(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validacionAlerta(field, true);
			return false;
		}
	}

	/**
	 * Muestra una alerta de validación en caso de que un campo esté vacío o tenga
	 * un valor inválido.
	 *
	 * @param field Nombre del campo que se está validando.
	 * @param empty true si el campo está vacío, false si el valor no es válido.
	 */
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

	/**
	 * Limpia los campos de entrada en el formulario de registro.
	 */
	@FXML
	private void limpiarCampos() {
		usuario.clear();
		;
		nombre.clear();
		apellidos.clear();
		fechaNac.setValue(null);
		email.clear();
		password.clear();
		cPassword.clear();
		choiceParadas.setValue(null);
		choiceNacionalidad.setValue(null);
	}

	/**
	 * Verifica si las contraseñas ingresadas coinciden.
	 *
	 * @param password  Primera contraseña ingresada.
	 * @param cPassword Confirmación de la contraseña.
	 * @return true si las contraseñas coinciden, false en caso contrario.
	 */
	private boolean passwordsCoinciden(String password, String cPassword) {
		if (cPassword.equals(password))
			return true;
		else
			return false;
	}

	/**
	 * Obtiene el texto ingresado en el campo de usuario.
	 *
	 * @return Un {@link String} con el nombre de usuario.
	 */
	public String getUsuario() {
		return usuario.getText();
	}

	/**
	 * Establece el campo de usuario.
	 *
	 * @param usuarioRegis Un objeto {@link TextField} que representa el campo de
	 *                     usuario.
	 */
	public void setUsuario(TextField usuarioRegis) {
		this.usuario = usuarioRegis;
	}

	/**
	 * Obtiene el nombre ingresado en el campo de texto.
	 *
	 * @return Un {@link String} con el nombre del usuario.
	 */
	public String getNombre() {
		return nombre.getText();
	}

	/**
	 * Establece el nombre en el campo de texto.
	 *
	 * @param nombreRegis Un objeto {@link TextField} con el nombre del usuario.
	 */
	public void setNombre(TextField nombreRegis) {
		this.nombre = nombreRegis;
	}

	/**
	 * Obtiene los apellidos ingresados en el campo de texto.
	 *
	 * @return Un {@link String} con los apellidos del usuario.
	 */
	public String getApellidos() {
		return apellidos.getText();
	}

	/**
	 * Establece los apellidos en el campo de texto.
	 *
	 * @param apellidosRegis Un objeto {@link TextField} con los apellidos del
	 *                       usuario.
	 */
	public void setApellidos(TextField apellidosRegis) {
		this.apellidos = apellidosRegis;
	}

	/**
	 * Obtiene el correo electrónico ingresado en el campo de texto.
	 *
	 * @return Un {@link String} con la dirección de correo electrónico.
	 */
	public String getEmail() {
		return email.getText();
	}

	/**
	 * Establece el correo electrónico en el campo de texto.
	 *
	 * @param emailRegis Un objeto {@link TextField} con la dirección de correo
	 *                   electrónico.
	 */
	public void setEmail(TextField emailRegis) {
		this.email = emailRegis;
	}

	/**
	 * Obtiene la contraseña ingresada en el campo de texto.
	 *
	 * @return Un {@link String} con la contraseña del usuario.
	 */
	public String getPassword() {
		return password.getText();
	}

	/**
	 * Establece la contraseña en el campo de texto.
	 *
	 * @param passwordRegis Un objeto {@link TextField} con la contraseña del
	 *                      usuario.
	 */
	public void setPassword(TextField passwordRegis) {
		this.password = passwordRegis;
	}

	/**
	 * Obtiene la confirmación de la contraseña ingresada en el campo de texto.
	 *
	 * @return Un {@link String} con la confirmación de la contraseña.
	 */
	public String getCPassword() {
		return cPassword.getText();
	}

	/**
	 * Establece la confirmación de la contraseña en el campo de texto.
	 *
	 * @param cPasswordRegis Un objeto {@link TextField} con la confirmación de la
	 *                       contraseña.
	 */
	public void setCPassword(TextField cPasswordRegis) {
		this.cPassword = cPasswordRegis;
	}

	/**
	 * Obtiene el ComboBox de nacionalidades.
	 *
	 * @return Un {@link ComboBox} con las opciones de nacionalidad.
	 */
	public ComboBox<String> getChoiceNacionalidad() {
		return choiceNacionalidad;
	}

	/**
	 * Establece el ComboBox de nacionalidades.
	 *
	 * @param choiceNacionalidad Un objeto {@link ComboBox} con las opciones de
	 *                           nacionalidad.
	 */
	public void setChoiceNacionalidad(ComboBox<String> choiceNacionalidad) {
		this.choiceNacionalidad = choiceNacionalidad;
	}

	/**
	 * Obtiene el ComboBox de paradas disponibles.
	 *
	 * @return Un {@link ComboBox} con las paradas disponibles.
	 */
	public ComboBox<Parada> getChoiceParadas() {
		return choiceParadas;
	}

	/**
	 * Establece el ComboBox de paradas disponibles.
	 *
	 * @param choiceParadas Un objeto {@link ComboBox} con las paradas disponibles.
	 */
	public void setChoiceParadas(ComboBox<Parada> choiceParadas) {
		this.choiceParadas = choiceParadas;
	}

	/**
	 * Obtiene el servicio de paradas.
	 *
	 * @return Un objeto {@link ParadaService} que maneja las operaciones sobre las
	 *         paradas.
	 */
	public ParadaService getParadaService() {
		return paradaService;
	}

	/**
	 * Establece el servicio de paradas.
	 *
	 * @param paradaService Un objeto {@link ParadaService} que maneja las
	 *                      operaciones sobre las paradas.
	 */
	public void setParadaService(ParadaService paradaService) {
		this.paradaService = paradaService;
	}

	/**
	 * Obtiene la fecha de nacimiento del usuario.
	 *
	 * @return Un objeto {@link DatePicker} con la fecha de nacimiento seleccionada.
	 */
	public DatePicker getFechaNac() {
		return fechaNac;
	}

	/**
	 * Establece la fecha de nacimiento del usuario.
	 *
	 * @param fechaNac Un objeto {@link DatePicker} con la fecha de nacimiento
	 *                 seleccionada.
	 */
	public void setFechaNac(DatePicker fechaNac) {
		this.fechaNac = fechaNac;
	}

}
