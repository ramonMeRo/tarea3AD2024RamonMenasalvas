package com.ramon.tarea3AD2024base.view;

import java.util.ResourceBundle;

public enum FxmlView {
	ADMINISTRADOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("user.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Administrador.fxml";
		}
	},
	INICIO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("inicio.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Inicio.fxml";
		}
	},
	REGISTRO{
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registro.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Registro.fxml";
		}
	};
	

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
