package com.ramon.tarea3AD2024base.modelo;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Entity
@Table(name = "Usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private long id;

	@Column(name = "Nombre", nullable = false, unique = true)
	private String nombre;

	@Enumerated(EnumType.STRING)
	@Column(name = "Perfil", nullable = false)
	private Perfil perfil = Perfil.INVITADO;

	@Column(name = "Email", nullable = false, unique = true)
	private String email;

	@Column(name = "Password", nullable = false)
	private String password;

	public Usuario() {
	}

	public Usuario(long id, String nombre, Perfil perfil, String email, String password) {
		this.id = id;
		this.nombre = nombre;
		this.perfil = perfil;
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, nombre, password, perfil);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(email, other.email) && id == other.id && Objects.equals(nombre, other.nombre)
				&& Objects.equals(password, other.password) && perfil == other.perfil;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", perfil=" + perfil + ", email=" + email + ", password="
				+ password + "]";
	}
}
