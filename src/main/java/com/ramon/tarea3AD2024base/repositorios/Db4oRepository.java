package com.ramon.tarea3AD2024base.repositorios;

import java.util.List;

import com.db4o.query.Query;
import com.ramon.tarea3AD2024base.data.Db4oConnection;
import com.ramon.tarea3AD2024base.modelo.PaqueteContratado;
import com.ramon.tarea3AD2024base.modelo.Servicio;

public class Db4oRepository {

	private Servicio findByServicioId(Long id) {

		Servicio servicio;

		Query query = Db4oConnection.getInstance().query();
		query.constrain(Servicio.class);
		query.descend("id").constrain(id);

		List<Servicio> resultado = query.execute();

		servicio = resultado.get(0);
		Db4oConnection.getInstance().close();
		return servicio;

	}

	private Servicio findByServicioNombre(String nombre) {

		Servicio servicio;
		Query query = Db4oConnection.getInstance().query();
		query.constrain(Servicio.class);
		query.descend("nombre").constrain(nombre);

		List<Servicio> resultado = query.execute();
		servicio = resultado.get(0);
		Db4oConnection.getInstance().close();
		return servicio;
	}

	private Long findServicioLastId() {

		Query query = Db4oConnection.getInstance().query();
		query.constrain(Servicio.class);
		query.descend("id").orderDescending();

		List<Servicio> resultado = query.execute();
		if (resultado.isEmpty()) {
			Db4oConnection.getInstance().close();
			return 0L;
		}
		Db4oConnection.getInstance().close();
		return resultado.get(0).getId() + 1;

	}

	private PaqueteContratado findByPcId(Long id) {
		PaqueteContratado pc;
		Query query = Db4oConnection.getInstance().query();
		query.constrain(Servicio.class);
		query.descend("id").constrain(id);
		List<PaqueteContratado> resultado = query.execute();
		pc = resultado.get(0);
		Db4oConnection.getInstance().close();
		return pc;
	}

	private PaqueteContratado findByPcNombre(String nombre) {
		PaqueteContratado pc;
		Query query = Db4oConnection.getInstance().query();
		query.constrain(Servicio.class);
		query.descend("nombre").constrain(nombre);
		List<PaqueteContratado> resultado = query.execute();
		pc = resultado.get(0);
		Db4oConnection.getInstance().close();
		return pc;
	}

	private Long findPcLastId() {

		Query query = Db4oConnection.getInstance().query();
		query.constrain(Servicio.class);
		query.descend("id");

		List<PaqueteContratado> resultado = query.execute();
		if (resultado.isEmpty()) {
			Db4oConnection.getInstance().close();
			return 0L;
		}
		Db4oConnection.getInstance().close();
		return resultado.get(0).getId() + 1;

	}

}
