package com.ramon.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PaqueteContratado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private double precioTotal;

	private char modoPago;

	private String extra = null;

	private Long idEstancia;

	private Set<Long> servicios = new HashSet<Long>();

	public PaqueteContratado() {
	}

	public PaqueteContratado(Long id, double precioTotal, char modoPago, String extra, Long idEstancia,
			Set<Long> servicios) {
		this.id = id;
		this.precioTotal = precioTotal;
		this.modoPago = modoPago;
		this.extra = extra;
		this.idEstancia = idEstancia;
		this.servicios = servicios;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public char getModoPago() {
		return modoPago;
	}

	public void setModoPago(char modoPago) {
		this.modoPago = modoPago;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Long getIdEstancia() {
		return idEstancia;
	}

	public void setIdEstancia(Long idEstancia) {
		this.idEstancia = idEstancia;
	}

	public Set<Long> getServicios() {
		return servicios;
	}

	public void setServicios(Set<Long> servicios) {
		this.servicios = servicios;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(extra, id, idEstancia, modoPago, precioTotal, servicios);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaqueteContratado other = (PaqueteContratado) obj;
		return Objects.equals(extra, other.extra) && Objects.equals(id, other.id)
				&& Objects.equals(idEstancia, other.idEstancia) && modoPago == other.modoPago
				&& Double.doubleToLongBits(precioTotal) == Double.doubleToLongBits(other.precioTotal)
				&& Objects.equals(servicios, other.servicios);
	}

	@Override
	public String toString() {
		return "PaqueteContratado [id=" + id + ", precioTotal=" + precioTotal + ", modoPago=" + modoPago + ", extra="
				+ extra + ", idEstancia=" + idEstancia + ", servicios=" + servicios + "]";
	}

}