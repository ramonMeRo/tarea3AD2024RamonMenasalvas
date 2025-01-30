package com.ramon.tarea3AD2024base.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class EstanciaTabla {

	private long id;

	private String nombreParada;

	private String nombrePeregrino;

	private boolean vip;

	private LocalDate fecha;

	public EstanciaTabla() {
	}

	public EstanciaTabla(long id, String nombreParada, String nombrePeregrino, boolean vip, LocalDate fecha) {
		this.id = id;
		this.nombreParada = nombreParada;
		this.nombrePeregrino = nombrePeregrino;
		this.vip = vip;
		this.fecha = fecha;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreParada() {
		return nombreParada;
	}

	public void setNombreParada(String nombreParada) {
		this.nombreParada = nombreParada;
	}

	public String getNombrePeregrino() {
		return nombrePeregrino;
	}

	public void setNombrePeregrino(String nombrePeregrino) {
		this.nombrePeregrino = nombrePeregrino;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fecha, id, nombreParada, nombrePeregrino, vip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstanciaTabla other = (EstanciaTabla) obj;
		return Objects.equals(fecha, other.fecha) && id == other.id && Objects.equals(nombreParada, other.nombreParada)
				&& Objects.equals(nombrePeregrino, other.nombrePeregrino) && vip == other.vip;
	}

	@Override
	public String toString() {
		return "EstanciaTabla [id=" + id + ", nombreParada=" + nombreParada + ", nombrePeregrino=" + nombrePeregrino
				+ ", vip=" + vip + ", fecha=" + fecha + "]";
	}

}
