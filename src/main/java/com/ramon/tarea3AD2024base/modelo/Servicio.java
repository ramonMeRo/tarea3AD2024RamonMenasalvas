package com.ramon.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Servicio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String nombre;

	private double precio;

	private long idParada;

	public Servicio() {
	}

	public Servicio(Long id, String nombre, double precio, long idParada) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.idParada = idParada;
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

	public long getIdParada() {
		return idParada;
	}

	public void setIdParada(long idParada) {
		this.idParada = idParada;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idParada, nombre, precio);
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
		return Objects.equals(id, other.id) && idParada == other.idParada && Objects.equals(nombre, other.nombre)
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio);
	}

	@Override
	public String toString() {
		return "Servicio [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", idParada=" + idParada + "]";
	}

}
