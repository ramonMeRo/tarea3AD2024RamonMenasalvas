package com.ramon.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Servicio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nombre;

	private double precio;

	private List<Long> idParadas = new ArrayList<Long>();

	public Servicio() {
	}

	public Servicio(Long id, String nombre, double precio, List<Long> idParadas) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.idParadas = idParadas;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public List<Long> getIdParadas() {
		return idParadas;
	}

	public void setIdParadas(List<Long> idParadas) {
		this.idParadas = idParadas;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idParadas, nombre, precio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Servicio other = (Servicio) obj;
		return Objects.equals(id, other.id) && Objects.equals(idParadas, other.idParadas)
				&& Objects.equals(nombre, other.nombre)
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio);
	}

	@Override
	public String toString() {
		return "Servicio [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", idParadas=" + idParadas + "]";
	}

}
