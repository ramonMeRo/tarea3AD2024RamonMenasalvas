package com.luisdbb.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Visitas")
public class Visita implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idPeregrino", nullable = false)
	private Peregrino peregrino;

	@ManyToOne
	@JoinColumn(name = "idParada", nullable = false)
	private Parada parada;

	@Column(name = "Fecha", nullable = false, unique = true)
	private LocalDate fecha;

	public Visita() {
	}

	public Visita(Long id, Peregrino peregrino, Parada parada, LocalDate fecha) {
		this.id = id;
		this.peregrino = peregrino;
		this.parada = parada;
		this.fecha = fecha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Peregrino getPeregrino() {
		return peregrino;
	}

	public void setPeregrino(Peregrino peregrino) {
		this.peregrino = peregrino;
	}

	public Parada getParada() {
		return parada;
	}

	public void setParada(Parada parada) {
		this.parada = parada;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fecha, id, parada, peregrino);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Visita other = (Visita) obj;
		return Objects.equals(fecha, other.fecha) && Objects.equals(id, other.id)
				&& Objects.equals(parada, other.parada) && Objects.equals(peregrino, other.peregrino);
	}

	@Override
	public String toString() {
		return "Visita [id=" + id + ", peregrino=" + peregrino + ", parada=" + parada + ", fecha=" + fecha + "]";
	}

}
