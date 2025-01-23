package com.ramon.tarea3AD2024base.repositorios;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Visita;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

	Visita findByFecha(LocalDate fecha);
	
	Visita findByParada(Parada parada);
	
	Visita findByPeregrino(Peregrino peregrino);
}
