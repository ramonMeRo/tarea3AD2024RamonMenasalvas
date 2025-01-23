package com.ramon.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Peregrinos")
public class Peregrino implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "Nombre", nullable = false)
	private String nombre;

	@Column(name = "Apellido", nullable = false)
	private String apellidos;

	@Column(name = "FechaNacimiento", nullable = false)
	private LocalDate fechaNac;

	@Column(name = "Nacionalidad", nullable = false)
	private String nacionalidad;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_usuario", nullable = false)
	private Usuario usuario;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Carnet carnet;

	@OneToMany(mappedBy = "peregrino", cascade = CascadeType.ALL)
	private Set<Estancia> listaEstancias;

	private Set<Visita> paradasVisitadas;

	public Peregrino() {
	}

	public Peregrino(Long id, String nombre, String apellidos, LocalDate fechaNac, String nacionalidad, Usuario usuario,
			Carnet carnet, Set<Estancia> listaEstancias, Set<Visita> paradasVisitadas) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNac = fechaNac;
		this.nacionalidad = nacionalidad;
		this.usuario = usuario;
		this.carnet = carnet;
		this.listaEstancias = listaEstancias;
		this.paradasVisitadas = paradasVisitadas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellido(String apellido) {
		this.apellidos = apellido;
	}

	public LocalDate getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(LocalDate fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Carnet getCarnet() {
		return carnet;
	}

	public void setCarnet(Carnet carnet) {
		this.carnet = carnet;
	}

	public Set<Estancia> getListaEstancias() {
		return listaEstancias;
	}

	public void setListaEstancias(Set<Estancia> listaEstancias) {
		this.listaEstancias = listaEstancias;
	}

	public Set<Visita> getParadasVisitadas() {
		return paradasVisitadas;
	}

	public void setParadasVisitadas(Set<Visita> paradasVisitadas) {
		this.paradasVisitadas = paradasVisitadas;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidos, carnet, fechaNac, id, listaEstancias, nacionalidad, nombre, paradasVisitadas,
				usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Peregrino other = (Peregrino) obj;
		return Objects.equals(apellidos, other.apellidos) && Objects.equals(carnet, other.carnet)
				&& Objects.equals(fechaNac, other.fechaNac) && Objects.equals(id, other.id)
				&& Objects.equals(listaEstancias, other.listaEstancias)
				&& Objects.equals(nacionalidad, other.nacionalidad) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(paradasVisitadas, other.paradasVisitadas) && Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "Peregrino [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", fechaNac=" + fechaNac
				+ ", nacionalidad=" + nacionalidad + ", usuario=" + usuario + ", carnet=" + carnet + ", listaEstancias="
				+ listaEstancias + ", paradasVisitadas=" + paradasVisitadas + "]";
	}

}
