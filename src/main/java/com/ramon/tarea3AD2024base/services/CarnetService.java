package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.repositorios.CarnetRepository;

public class CarnetService {

	@Autowired
	private CarnetRepository carnetRepository;
	
	public Carnet save(Carnet entity) {
		return carnetRepository.save(entity);
	}

	public Carnet update(Carnet entity) {
		return carnetRepository.save(entity);
	}

	public void delete(Carnet entity) {
		carnetRepository.delete(entity);
	}

	public void delete(Long id) {
		carnetRepository.deleteById(id);
	}

	public Carnet find(Long id) {
		return carnetRepository.findById(id).get();
	}

	public List<Carnet> findAll() {
		return carnetRepository.findAll();
	}
}
