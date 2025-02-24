package com.ramon.tarea3AD2024base.repositorios;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import com.ramon.tarea3AD2024base.data.Db4oConnection;
import com.ramon.tarea3AD2024base.modelo.PaqueteContratado;
import com.ramon.tarea3AD2024base.modelo.Servicio;

@Repository
public class Db4oRepository {

	public Servicio findByServicioId(Long id) {
		ObjectContainer db = Db4oConnection.getInstance();
		Query query = db.query();
		query.constrain(Servicio.class);
		query.descend("id").constrain(id);
		List<Servicio> resultado = query.execute();
		return resultado.get(0);
	}

	public List<Servicio> findAllServicio() {
		ObjectContainer db = Db4oConnection.getInstance();
		Query query = db.query();
		query.constrain(Servicio.class);
		query.execute();
		List<Servicio> resultado = query.execute();
		return resultado;
	}

	public void saveServicio(Servicio servicio) {
		ObjectContainer db = Db4oConnection.getInstance();
		db.store(servicio);
	}
	
	public void updateServicio(Servicio servicio) {
		ObjectContainer db = Db4oConnection.getInstance();
		try {
			Query query = db.query();
			query.constrain(Servicio.class);
			query.descend("id").constrain(servicio.getId());
			List<Servicio> resultado = query.execute();
			
			if(!resultado.isEmpty()) {
				Servicio actualizar = resultado.get(0);
				actualizar.setNombre(servicio.getNombre());
				actualizar.setPrecio(servicio.getPrecio());
				actualizar.setIdParadas(servicio.getIdParadas());
				db.store(actualizar);
			}
		} catch (Exception e) {
			db.rollback();
		}
	}

	public Servicio findByServicioNombre(String nombre) {
		ObjectContainer db = Db4oConnection.getInstance();
		try {
			Query query = db.query();
			query.constrain(Servicio.class);
			query.descend("nombre").constrain(nombre);
			List<Servicio> resultado = query.execute();
			return resultado.get(0);
		} catch (Exception e) {
			return null;
		}
	}

	public Long findServicioLastId() {
		ObjectContainer db = Db4oConnection.getInstance();
		try {
			Query query = db.query();
			query.constrain(Servicio.class);
			query.descend("id").orderDescending();
			List<Servicio> resultado = query.execute();
			return resultado.get(0).getId() + 1;
		} catch (Exception e) {
			return 1L;
		}
	}

	public PaqueteContratado findByPcId(Long id) {
		ObjectContainer db = Db4oConnection.getInstance();
		Query query = db.query();
		query.constrain(PaqueteContratado.class);
		query.descend("id").constrain(id);
		List<PaqueteContratado> resultado = query.execute();
		return resultado.get(0);
	}

	public PaqueteContratado findByPcNombre(String nombre) {
		ObjectContainer db = Db4oConnection.getInstance();
		try {
			Query query = db.query();
			query.constrain(PaqueteContratado.class);
			query.descend("nombre").constrain(nombre);
			List<PaqueteContratado> resultado = query.execute();
			return resultado.get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<PaqueteContratado> findAllPc() {
		ObjectContainer db = Db4oConnection.getInstance();
		Query query = db.query();
		query.constrain(PaqueteContratado.class);
		query.execute();
		List<PaqueteContratado> resultado = query.execute();
		return resultado;
	}

	public Long findPcLastId() {
		ObjectContainer db = Db4oConnection.getInstance();
		try {
			Query query = db.query();
			query.constrain(PaqueteContratado.class);
			query.descend("id").orderDescending();
			List<PaqueteContratado> resultado = query.execute();

			return resultado.get(0).getId() + 1;
		} catch (Exception e) {
			return 1L;
		}
	}

	public void savePc(PaqueteContratado paquete) {
		ObjectContainer db = Db4oConnection.getInstance();
		db.store(paquete);
	}

	public void cerrarBase() {
		ObjectContainer db = Db4oConnection.getInstance();
		db.close();
	}
}
