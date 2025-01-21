package com.ramon.tarea3AD2024base.modelo;

import java.time.LocalDate;
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

	@Column(name = "Nombre", nullable = false)
	private String nombre;

	@Column(name = "FechaNacimiento", nullable = false)
	private LocalDate fechaNac;

	@Column(name = "Genero", nullable = false)
	private String genero;

	@Enumerated(EnumType.STRING)
	@Column(name = "Perfil", nullable = false)
	private Perfil perfil = Perfil.INVITADO;

	@Column(name = "Email", nullable = true, unique = true)
	private String email;

	@Column(name = "Password", nullable = false)
	private String password;

	public Usuario() {
	}

	public Usuario(long id, String nombre, LocalDate fechaNac, String genero, Perfil perfil, String email,
			String password) {
		this.id = id;
		this.nombre = nombre;
		this.fechaNac = fechaNac;
		this.genero = genero;
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

	public LocalDate getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(LocalDate fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
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
		return Objects.hash(email, fechaNac, genero, id, nombre, password, perfil);
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
		return Objects.equals(email, other.email) && Objects.equals(fechaNac, other.fechaNac)
				&& Objects.equals(genero, other.genero) && id == other.id && Objects.equals(nombre, other.nombre)
				&& Objects.equals(password, other.password) && perfil == other.perfil;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", fechaNac=" + fechaNac + ", genero=" + genero
				+ ", perfil=" + perfil + ", email=" + email + ", password=" + password + "]";
	}
}
