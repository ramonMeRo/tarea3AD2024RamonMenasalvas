package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.Utils.VistaUtils;
import com.ramon.tarea3AD2024base.config.StageManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

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
	private ChoiceBox choiceNacionalidad;
	@FXML
	private ChoiceBox choiceParada;
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

	@FXML
	private void volver() {
		VistaUtils.volver();
	}

	@FXML
	private void salir() {
		VistaUtils.Salir();
	}

	@FXML
	private void registrar() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
