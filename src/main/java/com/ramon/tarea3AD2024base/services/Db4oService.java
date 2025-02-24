package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.PaqueteContratado;
import com.ramon.tarea3AD2024base.modelo.Servicio;
import com.ramon.tarea3AD2024base.repositorios.Db4oRepository;

@Service
public class Db4oService {

	private Db4oRepository db4o;
	
	@Autowired
	public Db4oService (Db4oRepository db4o) {
		this.db4o = db4o;
	}
	
	public void saveServicio(Servicio servicio) {
		db4o.saveServicio(servicio);
	}
	
	public void updateServicio(Servicio servicio) {
		db4o.updateServicio(servicio);
	}
	
	public List<Servicio> findAllServicio(){
		return db4o.findAllServicio();
	}
	
	public Servicio findByServicioId(Long id) {
		return db4o.findByServicioId(id);
	}
	
	public Servicio findByServicioNombre(String nombre) {
		return db4o.findByServicioNombre(nombre);
	}
	
	public Long findServicioLastId() {
		return db4o.findServicioLastId();
	}
	
	public void savePc(PaqueteContratado pc) {
		db4o.savePc(pc);
	}
	
	public PaqueteContratado findByPcId(Long id) {
		return db4o.findByPcId(id);
	}
	
	public List<PaqueteContratado> findAllPc(){
		return db4o.findAllPc();
	}
	
	public PaqueteContratado findByPcNombre(String nombre) {
		return db4o.findByPcNombre(nombre);
	}
	
	public Long findPcLastId() {
		return db4o.findPcLastId();
	}
	
	public void cerrarBase() {
		db4o.cerrarBase();
	}
}
