package com.ramon.tarea3AD2024base.repositorios;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.data.ObjectdbConnection;
import com.ramon.tarea3AD2024base.modelo.EnvioACasa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ObjectdbRepository {

	public List<EnvioACasa> findAllByParada(Long id) {

		EntityManager em = ObjectdbConnection.getEntityManager();

		List<EnvioACasa> query = em.createQuery("Select e from EnvioACasa e where e.idParada = :id", EnvioACasa.class)
				.setParameter("id", id).getResultList();

		return query;

	}

	public void save(EnvioACasa envio) {

		EntityManager em = ObjectdbConnection.getEntityManager();

		em.getTransaction().begin();
		em.persist(envio);
		em.getTransaction().commit();

	}

	public void close() {
		EntityManager em = ObjectdbConnection.getEntityManager();
		em.close();
	}
}
