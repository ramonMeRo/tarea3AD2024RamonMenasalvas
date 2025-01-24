package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.Utils.VistaUtils;
import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
	private ChoiceBox<String> choiceNacionalidad;
	@FXML
	private ChoiceBox<Parada> choiceParada;
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

	private void llenarChoiceConParadas() {
		List<Parada> paradas = paradaService.findAll();
		choiceParada.getItems().clear();
		choiceParada.getItems().addAll(paradas);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		choiceParada.setConverter(new StringConverter<>() {

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

		choiceParada.getItems().add(0, null);
		choiceParada.setValue(null);

		choiceParada.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.intValue() == 0) {
				choiceParada.setValue(null);
			}
		});

	}

}
