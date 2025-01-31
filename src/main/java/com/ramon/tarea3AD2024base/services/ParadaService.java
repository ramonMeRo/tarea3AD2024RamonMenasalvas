package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.repositorios.ParadaRepository;

@Service
public class ParadaService {

	@Autowired
	private ParadaRepository paradaRepository;
	
	public Parada save(Parada entity) {
		return paradaRepository.save(entity);
	}

	public Parada update(Parada entity) {
		return paradaRepository.save(entity);
	}

	public void delete(Parada entity) {
		paradaRepository.delete(entity);
	}

	public void delete(Long id) {
		paradaRepository.deleteById(id);
	}

	public Parada find(Long id) {
		return paradaRepository.findById(id).get();
	}

	public List<Parada> findAll() {
		return paradaRepository.findAll();
	}
	
	public Parada findByUsuario(Usuario usuario) {
		return paradaRepository.findByUsuario(usuario);
	}
	
	public boolean existsByNombre(String nombre) {
		return paradaRepository.existsByNombre(nombre);
	}
	
	public boolean existsByRegion(char region) {
		return paradaRepository.existsByRegion(region);
	}
}
