package com.ramon.tarea3AD2024base.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

@Component
public class ExistdbConnection {

	@Value("${existdb.uri}")
	private String url;

	@Value("${existdb.usuario}")
	private String usuario;

	@Value("${existdb.password}")
	private String password;

	public Collection getInstance() {
		Collection coleccion = null;
		try {
			Class<?> clase = Class.forName("org.exist.xmldb.DatabaseImpl");
			Database bd = (Database) clase.getDeclaredConstructor().newInstance();
			bd.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(bd);

			coleccion = DatabaseManager.getCollection(url, usuario, password);

			if (coleccion == null) {
				System.out.println("Colección principal no existe, se intentará crearla...");
				Collection root = DatabaseManager.getCollection("xmldb:exist://localhost:8080/exist/xmlrpc/db", usuario,
						password);
				CollectionManagementService mgtService = (CollectionManagementService) root
						.getService("CollectionManagementService", "1.0");
				mgtService.createCollection(url.substring(url.lastIndexOf("/") + 1));
				coleccion = DatabaseManager.getCollection(url, usuario, password);
			}

		} catch (Exception e) {
			System.out.println("Error al establecer la conexión con eXistdb: " + e.getMessage());
			e.printStackTrace();
		}
		return coleccion;
	}

	public void cerrarConexion(Collection coleccion) {
		if (coleccion != null) {
			try {
				coleccion.close();
			} catch (XMLDBException e) {
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public String getUrl() {
		return url;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getPassword() {
		return password;
	}
}