package com.ramon.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Usuario;

@Repository
public interface PeregrinoRepository extends JpaRepository<Peregrino, Long> {

	Peregrino findById(long id);
	
	Peregrino findByApellidos(String apellidos);
	
	Peregrino findByUsuario(Usuario usuario);
}
