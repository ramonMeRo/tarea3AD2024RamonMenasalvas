package com.ramon.tarea3AD2024base.data;

import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Component
public class ObjectdbConnection {

	private static final EntityManagerFactory emf;

	static {
		Properties properties = new Properties();
		try {
			properties.load(ObjectdbConnection.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			throw new RuntimeException("No se pudo cargar el archivo", e);
		}
		String dbPath = properties.getProperty("objectdb.datasource.url");
		emf = Persistence.createEntityManagerFactory(dbPath);
	}

	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public static void close() {
		emf.close();
	}
}
