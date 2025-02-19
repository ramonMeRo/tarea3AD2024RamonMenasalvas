package com.ramon.tarea3AD2024base.data;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Component
public class ObjectdbConnection {

	private static ObjectdbConnection INSTANCE = null;
	private EntityManagerFactory emf;
	private EntityManager em;

	private ObjectdbConnection() {
		emf = Persistence.createEntityManagerFactory("objectdb:$objectdb/db/Envios.odb");
		em = emf.createEntityManager();
	}

	public static synchronized ObjectdbConnection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ObjectdbConnection();
		}
		return INSTANCE;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void closeConnection() {
		if (em != null && em.isOpen()) {
			em.close();
		}
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
		INSTANCE = null;
	}
}
