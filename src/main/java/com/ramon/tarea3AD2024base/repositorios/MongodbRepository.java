package com.ramon.tarea3AD2024base.repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.modelo.CopiaCarnets;

@Repository
public interface MongodbRepository extends MongoRepository <CopiaCarnets, String> {
	
}
