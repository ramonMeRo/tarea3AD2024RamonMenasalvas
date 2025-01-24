package com.ramon.tarea3AD2024base.modelo;

import java.io.Serializable;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "Paradas")
public class Parada implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "fk_usuario", nullable = false)
	private Usuario usuario;

	@Column(name = "Nombre", nullable = false)
	private String nombre;

	@Column(name = "Region", nullable = false)
	private char region;

	@Column(name = "Responsable", nullable = false)
	private String responsable;

	@OneToMany(mappedBy = "paradaInicial", cascade = CascadeType.ALL)
	private Set<Carnet> listaCarnets;

	@OneToMany(mappedBy = "parada", cascade = CascadeType.ALL)
	private Set<Estancia> listaEstancias;

	public Parada() {

	}

	public Parada(Long id, Usuario usuario, String nombre, char region, String responsable, Set<Carnet> listaCarnets,
			Set<Estancia> listaEstancias, Set<Visita> paradasVisitadas) {
		this.id = id;
		this.usuario = usuario;
		this.nombre = nombre;
		this.region = region;
		this.responsable = responsable;
		this.listaCarnets = listaCarnets;
		this.listaEstancias = listaEstancias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public char getRegion() {
		return region;
	}

	public void setRegion(char region) {
		this.region = region;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public Set<Carnet> getListaCarnets() {
		return listaCarnets;
	}

	public void setListaCarnets(Set<Carnet> listaCarnets) {
		this.listaCarnets = listaCarnets;
	}

	public Set<Estancia> getListaEstancias() {
		return listaEstancias;
	}

	public void setListaEstancias(Set<Estancia> listaEstancias) {
		this.listaEstancias = listaEstancias;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, listaCarnets, listaEstancias, nombre, region, responsable, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parada other = (Parada) obj;
		return Objects.equals(id, other.id) && Objects.equals(listaCarnets, other.listaCarnets)
				&& Objects.equals(listaEstancias, other.listaEstancias) && Objects.equals(nombre, other.nombre)
				&& region == other.region && Objects.equals(responsable, other.responsable)
				&& Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "Parada [id=" + id + ", usuario=" + usuario + ", nombre=" + nombre + ", region=" + region
				+ ", responsable=" + responsable + ", listaCarnets=" + listaCarnets + ", listaEstancias="
				+ listaEstancias + "]";
	}

}
