package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.repositorios.ExistdbRepository;

@Service
public class ExistdbService {
	
	@Autowired
	ExistdbRepository existdb;
	
	public void crearColeccion(String parada) {
		existdb.crearColeccion(parada);
	}

	public void guardarCarnetsPorParada(String parada, String fichero, String contenido) {
		existdb.guardarCarnetsPorParada(parada, fichero, contenido);
	}
	
	public List<String> obtenerCarnetsPorParada(String parada){
		return existdb.obtenerCarnetsPorParada(parada);
	}
}
