package com.ramon.tarea3AD2024base.view;

import java.util.ResourceBundle;

public enum FxmlView {
	
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
	RESPONSABLE{
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("usuario.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Responsable.fxml";
		}
	},
	PEREGRINO{
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("usuario.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Peregrino.fxml";
		}
	},
	ADMINISTRADOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("usuario.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Administrador.fxml";
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
	},
	CARNETS{
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("tabla.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/TablaCarnets.fxml";
		}
	};
	

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
