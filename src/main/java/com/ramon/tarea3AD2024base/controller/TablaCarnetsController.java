package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.ExistdbService;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

@Controller
public class TablaCarnetsController implements Initializable {

	@FXML
	private TreeView tablaCarnets;

	@FXML
	private ImageView volver;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private ExistdbService existdbService;
	
	@Autowired
	private ParadaService paradaService;

	public Sesion sesion;
	
	private Usuario usuario;

	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.RESPONSABLE);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		mostrarCarnets();
	}

	private void mostrarCarnets() {
		sesion = InicioController.sesion;
		usuario = sesion.getUsuario();
		List<String> carnets = existdbService.contenidoCarnet(paradaService.findByUsuario(usuario).getNombre());

		TreeItem<String> root = new TreeItem<>("Carnets");
		root.setExpanded(true);

		for (String carnet : carnets) {
			TreeItem<String> item = new TreeItem<>(carnet);
			root.getChildren().add(item);
		}
		tablaCarnets.setRoot(root);
	}
}
