package com.ramon.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.repositorios.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository userRepository;

	public Usuario save(Usuario entity) {
		return userRepository.save(entity);
	}

	public Usuario update(Usuario entity) {
		return userRepository.save(entity);
	}

	public void delete(Usuario entity) {
		userRepository.delete(entity);
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	public Usuario find(Long id) {
		return userRepository.findById(id).get();
	}

	public List<Usuario> findAll() {
		return userRepository.findAll();
	}

	public boolean authenticate(String username, String password) {
		Usuario user = this.findByNombreOrEmail(username);
		if (user == null) {
			return false;
		} else {
			if (password.equals(user.getPassword()))
				return true;
			else
				return false;
		}
	}

	public Usuario findByNombreOrEmail(String email) {
		return userRepository.findByNombreOrEmail(email);
	}
	

	public void deleteInBatch(List<Usuario> users) {
		userRepository.deleteAll(users);
	}

}
