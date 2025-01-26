package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.repositorios.VisitaRepository;

@Service
public class VisitaService {

	@Autowired
	private VisitaRepository visitaRepository;
	
	public Visita save(Visita entity) {
		return visitaRepository.save(entity);
	}

	public Visita update(Visita entity) {
		return visitaRepository.save(entity);
	}

	public void delete(Visita entity) {
		visitaRepository.delete(entity);
	}

	public void delete(Long id) {
		visitaRepository.deleteById(id);
	}

	public Visita find(Long id) {
		return visitaRepository.findById(id).get();
	}

	public List<Visita> findAll() {
		return visitaRepository.findAll();
	}
}
