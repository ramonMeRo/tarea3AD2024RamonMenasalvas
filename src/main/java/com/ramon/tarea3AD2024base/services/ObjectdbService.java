package com.ramon.tarea3AD2024base.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.Envio;
import com.ramon.tarea3AD2024base.repositorios.ObjectdbRepository;

@Service
public class ObjectdbService {

	private ObjectdbRepository objectdb;
	
	@Autowired
	public ObjectdbService (ObjectdbRepository objectdb) {
		this.objectdb = objectdb;
	}
	
	public void save(Envio envio) {
		
		objectdb.save(envio);
	}
	
	public Set<Envio> findAllByParada(Long id){
		return objectdb.findAllByParada(id);
	}
}
