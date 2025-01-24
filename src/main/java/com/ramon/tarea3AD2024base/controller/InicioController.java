package com.ramon.tarea3AD2024base.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Ramon
 * @since 05-04-2017
 */

@Controller
public class InicioController implements Initializable {

	@FXML
	private Button btnLogin;

	@FXML
	private PasswordField password;

	@FXML
	private TextField txtUsuario;

	@FXML
	private Label lblLogin;
	@FXML
	private Hyperlink hlRegistro;

	@Autowired
	private UsuarioService userService;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@FXML
	private void login(ActionEvent event) throws IOException {
		if (userService.authenticate(getUsername(), getPassword())) {
			Usuario usuario = userService.findByNombreOrEmail(getUsername());
			if (usuario.getPerfil().equals(Perfil.ADMIN))
				stageManager.switchScene(FxmlView.ADMINISTRADOR);
			else if (usuario.getPerfil().equals(Perfil.PARADA))
				stageManager.switchScene((FxmlView.RESPONSABLE));
			else if (usuario.getPerfil().equals(Perfil.PEREGRINO))
				stageManager.switchScene(FxmlView.PEREGRINO);

		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Error de inicio de sesión");
			alert.setHeaderText(null);
			alert.setContentText("Usuario y/o contraseña incorrectos");
			Optional<ButtonType> action = alert.showAndWait();
		}
	}

	@FXML
	private void registrarse(ActionEvent event) {
		if (hlRegistro.isPressed())
			System.out.println("Registro");
		stageManager.switchScene(FxmlView.REGISTRO);
	}

	public String getPassword() {
		return password.getText();
	}

	public String getUsername() {
		return txtUsuario.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
