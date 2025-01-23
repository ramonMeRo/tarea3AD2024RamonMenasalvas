package com.ramon.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ramon.tarea3AD2024base.modelo.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);
	
	@Query("""
			SELECT u FROM Usuario u
			WHERE UPPER(u.nombre) LIKE UPPER(:texto) OR UPPER(u.email) LIKE UPPER(:texto)
			""")
	Usuario findByNombreOrEmail(@Param ("texto") String texto);
	
	Usuario findByPassword(String password);
	
	
}
