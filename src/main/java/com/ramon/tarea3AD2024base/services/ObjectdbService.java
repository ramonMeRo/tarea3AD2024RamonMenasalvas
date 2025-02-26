package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.EnvioACasa;
import com.ramon.tarea3AD2024base.repositorios.ObjectdbRepository;

@Service
public class ObjectdbService {

	private ObjectdbRepository objectdb;
	
	@Autowired
	public ObjectdbService (ObjectdbRepository objectdb) {
		this.objectdb = objectdb;
	}
	
	public void save(EnvioACasa envio) {
		
		objectdb.save(envio);
	}
	
	public void close() {
		objectdb.close();
	}
	
	public List<EnvioACasa> findAllByParada(Long id){
		return objectdb.findAllByParada(id);
	}
}
