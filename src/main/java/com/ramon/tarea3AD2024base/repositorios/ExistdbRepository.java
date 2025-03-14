package com.ramon.tarea3AD2024base.repositorios;

import java.util.ArrayList;
import java.util.List;

import org.exist.xmldb.EXistResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import com.ramon.tarea3AD2024base.data.ExistdbConnection;

@Repository
public class ExistdbRepository {

	@Autowired
	ExistdbConnection existdb;

	public void crearColeccion(String parada) {
		Collection baseColeccion = existdb.getInstance();
		try {

			String url = existdb.getUrl();

			baseColeccion = DatabaseManager.getCollection(url, existdb.getUsuario(), existdb.getPassword());
			if (baseColeccion == null) {
				System.out.println("error");
			}

			CollectionManagementService cms = (CollectionManagementService) baseColeccion
					.getService("CollectionManagementService", "1.0");

			cms.createCollection(parada);
			System.out.println("Colección '" + parada + "' creada correctamente.");
		} catch (XMLDBException e) {
			System.out.println("Error al crear la carpeta: ");
			e.printStackTrace();
		} finally {
			if (baseColeccion != null) {
				try {
					baseColeccion.close();
				} catch (XMLDBException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void guardarCarnetsPorParada(String parada, String fichero, String contenido) {
		Collection baseColeccion = existdb.getInstance();
		try {
			String ruta = existdb.getUrl() + "/" + parada;
			Collection coleccion = DatabaseManager.getCollection(ruta, existdb.getUsuario(), existdb.getPassword());

			if (coleccion == null) {
				CollectionManagementService cms = (CollectionManagementService) baseColeccion
						.getService("CollectionManagementService", "1.0");

				cms.createCollection(parada);
				coleccion = DatabaseManager.getCollection(ruta, existdb.getUsuario(), existdb.getPassword());
			}
			XMLResource recurso = (XMLResource) coleccion.createResource(fichero, "XMLResource");

			contenido = quitarBom(contenido).trim();

			((EXistResource) recurso).setMimeType("text/xml");
			recurso.setContent(contenido);

			coleccion.storeResource(recurso);
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String quitarBom(String xml) {
		if (xml != null && xml.startsWith("\uFEFF")) {
			xml = xml.substring(1);
		}
		return xml;
	}

	public List<String> contenidoCarnet(String parada) {
		List<String> carnets = new ArrayList<>();
		
		Collection baseColeccion = existdb.getInstance();

		try {
			String ruta = existdb.getUrl() + "/" + parada;
			Collection coleccion = DatabaseManager.getCollection(ruta, existdb.getUsuario(), existdb.getPassword());

			if (coleccion == null) {
				return null;
			}

			String[] contenido = coleccion.listResources();

			for (String datos : contenido) {
				carnets.add(datos);
			}

		} catch (Exception e) {
			System.out.println("Falla aqui");
		}
		return carnets;
	}
}
