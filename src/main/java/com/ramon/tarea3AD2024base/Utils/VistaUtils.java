package com.ramon.tarea3AD2024base.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class VistaUtils {

	@Lazy
	@Autowired
	private static StageManager stageManager;

	public static void Salir() {
		Dialog<ButtonType> dialogo = new Dialog<>();
		dialogo.setTitle("Salir");
		dialogo.setHeaderText("¿Seguro que desea salir?");

		ButtonType btnSi = new ButtonType("SI");
		ButtonType btnNo = new ButtonType("NO");
		dialogo.getDialogPane().getButtonTypes().addAll(btnSi, btnNo);

		// Por lo que vi response viene del metodo ShowAndWait() y nos permite acceder
		// directamente al valor del boton pulsado
		dialogo.showAndWait().ifPresent(response -> {
			if (response == btnSi) {
				System.exit(0);
			}

		});
	}

	public static void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}
}
