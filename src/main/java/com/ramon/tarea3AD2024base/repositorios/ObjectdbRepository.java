package com.ramon.tarea3AD2024base.repositorios;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.data.ObjectdbConnection;
import com.ramon.tarea3AD2024base.modelo.Envio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class ObjectdbRepository {

	public Set<Envio> findAllByParada(Long id) {
		
		EntityManager em = ObjectdbConnection.getEntityManager();

		TypedQuery<Envio> query = em.createQuery("Select e from Envio e where e.idParada = :id", Envio.class);
		
		Set<Envio> envios = new TreeSet<>(query.getResultList());

		return envios;

	}
	
	public void save(Envio envio) {

		EntityManager em = ObjectdbConnection.getEntityManager();
		
		em.getTransaction().begin();
		em.persist(envio);
		em.getTransaction().commit();

	}
}
