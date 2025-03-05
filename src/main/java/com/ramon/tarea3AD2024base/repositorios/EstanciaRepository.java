package com.ramon.tarea3AD2024base.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Peregrino;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, Long>{
	
	List<Estancia>findByPeregrino(Peregrino peregrino);

}
