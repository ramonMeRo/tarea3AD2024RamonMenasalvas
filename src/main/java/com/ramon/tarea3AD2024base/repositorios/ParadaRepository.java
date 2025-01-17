package com.ramon.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.modelo.Parada;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, Long> {

}