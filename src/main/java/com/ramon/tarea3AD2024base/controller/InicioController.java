package com.ramon.tarea3AD2024base.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.Utils.VistaUtils;
import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controlador de la ventana del inicio que tiene funciones para la
 * autentificación de un usuario y en funcion del tipo le redirige a una ventana
 * u otra y la redireccion a la ventana de registro de peregrinos.
 * 
 * @author Ramon
 * @since 01-01-2025
 */

@Controller
public class InicioController implements Initializable {

	private boolean mostrarContraseña = true;

	@FXML
	private Button btnLogin;

	@FXML
	private ImageView btnVisible;

	@FXML
	public PasswordField password;

	@FXML
	public TextField txtUsuario;

	@FXML
	private TextField passwordVisible;

	@FXML
	private Label lblLogin;

	@FXML
	private Hyperlink hlRegistro;

	@Autowired
	public UsuarioService userService;

	@Lazy
	@Autowired
	public StageManager stageManager;

	public static Sesion sesion = new Sesion();

	/**
	 * Cierra la aplicación.
	 */
	@FXML
	private void salir() {
		VistaUtils.Salir();
	}

	/**
	 * Maneja el evento de inicio de sesión. Verifica las credenciales del usuario y
	 * redirige a la vista correspondiente según el perfil.
	 * 
	 * @param event Evento del botón de inicio de sesión.
	 * @throws IOException Si ocurre un error al cambiar de escena.
	 */
	@FXML
	public void login(ActionEvent event) throws IOException {
		if (userService.authenticate(getUsername(), getPassword())) {
			Usuario usuario = userService.findByNombreOrEmail(getUsername());

			if (usuario.getPerfil().equals(Perfil.ADMIN)) {

				sesion.setUsuario(usuario);

				stageManager.switchScene(FxmlView.ADMINISTRADOR);
			}

			else if (usuario.getPerfil().equals(Perfil.PARADA)) {

				sesion.setUsuario(usuario);

				stageManager.switchScene((FxmlView.RESPONSABLE));
			} else if (usuario.getPerfil().equals(Perfil.PEREGRINO)) {

				sesion.setUsuario(usuario);

				stageManager.switchScene(FxmlView.PEREGRINO);
			}

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error de inicio de sesión");
			alert.setHeaderText("Intento de inicio de sesion");
			alert.setContentText("Usuario y/o contraseña incorrectos");
			alert.showAndWait();

		}
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

			String url = getClass().getResource("/help/html/Inicio.html").toExternalForm();
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
	 * Redirige al usuario a la pantalla de registro.
	 * 
	 * @param event Evento del botón de registro.
	 */
	@FXML
	private void registrarse(ActionEvent event) {
		if (hlRegistro.isPressed())
			System.out.println("Registro");
		stageManager.switchScene(FxmlView.REGISTRO);
	}

	/**
	 * Obtiene la contraseña ingresada.
	 * 
	 * @return Contraseña ingresada en el campo de texto.
	 */
	public String getPassword() {
		return password.getText();
	}

	/**
	 * Obtiene el nombre de usuario ingresado.
	 * 
	 * @return Nombre de usuario ingresado en el campo de texto.
	 */
	public String getUsername() {
		return txtUsuario.getText();
	}

	/**
	 * Establece el campo de texto del nombre de usuario.
	 * 
	 * @param txtUsuario Campo de texto del nombre de usuario.
	 */
	public void setTxtUsuario(TextField txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	/**
	 * Establece el campo de texto de la contraseña.
	 * 
	 * @param password Campo de texto de la contraseña.
	 */
	public void setPassword(PasswordField password) {
		this.password = password;
	}

	/**
	 * Inicializa la vista, configurando la funcionalidad de mostrar u ocultar la
	 * contraseña.
	 * 
	 * @param location  URL de inicialización.
	 * @param resources Recursos para la inicialización.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		passwordVisible.textProperty().bindBidirectional(password.textProperty());

		Image iconoCerrado = new Image(getClass().getResourceAsStream("/img/ojoCerrado.png"));
		Image iconoAbierto = new Image(getClass().getResourceAsStream("/img/ojoAbierto.png"));

		btnVisible.setOnMouseClicked(event -> {
			mostrarContraseña = !mostrarContraseña;

			if (mostrarContraseña) {
				btnVisible.setImage(iconoAbierto);
				passwordVisible.setVisible(true);
				passwordVisible.setManaged(true);
				password.setVisible(false);
				password.setManaged(false);
			} else {
				btnVisible.setImage(iconoCerrado);
				password.setVisible(true);
				password.setManaged(true);
				passwordVisible.setVisible(false);
				passwordVisible.setManaged(false);
			}
		});

	}
}
