package com.ramon.tarea3AD2024base.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Ramon
 * @since 05-04-2017
 */

@Controller
public class InicioController implements Initializable {
	
	private boolean mostrarContraseña = true;

	@FXML
	private Button btnLogin;

	@FXML
	private ImageView btnVisible;

	@FXML
	private PasswordField password;

	@FXML
	private TextField txtUsuario;

	@FXML
	private TextField passwordVisible;

	@FXML
	private Label lblLogin;

	@FXML
	private Hyperlink hlRegistro;

	@Autowired
	private UsuarioService userService;

	@Lazy
	@Autowired
	private StageManager stageManager;
	
	public static Sesion sesion = new Sesion();

	@FXML
	private void login(ActionEvent event) throws IOException {
		if (userService.authenticate(getUsername(), getPassword())) {
			Usuario usuario = userService.findByNombreOrEmail(getUsername());
			
			
			if (usuario.getPerfil().equals(Perfil.ADMIN)) {
				
				sesion.setUsuario(usuario);
				
				stageManager.switchScene(FxmlView.ADMINISTRADOR);
			}
			
			else if (usuario.getPerfil().equals(Perfil.PARADA)){
				
				sesion.setUsuario(usuario);
				
				stageManager.switchScene((FxmlView.RESPONSABLE));
			}
			else if (usuario.getPerfil().equals(Perfil.PEREGRINO)) {
				
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

		Image iconoCerrado = new Image(getClass().getResourceAsStream("/img/ojoCerrado.png"));
		Image iconoAbierto = new Image(getClass().getResourceAsStream("/img/ojoAbierto.png"));

		btnVisible.setOnMouseClicked(event -> {
	        mostrarContraseña = !mostrarContraseña;

	        if (mostrarContraseña) {
	            
	            btnVisible.setImage(iconoAbierto);
	            passwordVisible.setText(password.getText());
	            passwordVisible.setVisible(true);
	            password.setVisible(false);
	        } else {
	            
	            btnVisible.setImage(iconoCerrado);
	            password.setText(passwordVisible.getText());
	            passwordVisible.setVisible(false);
	            password.setVisible(true);
	        }
	    });

	}
}
