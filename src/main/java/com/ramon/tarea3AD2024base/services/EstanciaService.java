package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.repositorios.EstanciaRepository;

@Service
public class EstanciaService {

	@Autowired
	private EstanciaRepository estanciaRepository;
	
	public Estancia save(Estancia entity) {
		return estanciaRepository.save(entity);
	}

	public Estancia update(Estancia entity) {
		return estanciaRepository.save(entity);
	}

	public void delete(Estancia entity) {
		estanciaRepository.delete(entity);
	}

	public void delete(Long id) {
		estanciaRepository.deleteById(id);
	}

	public Estancia find(Long id) {
		return estanciaRepository.findById(id).get();
	}

	public List<Estancia> findAll() {
		return estanciaRepository.findAll();
	}
}
