package com.ramon.tarea3AD2024base.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Carnets")
public class Carnet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "FechaExpedido")
	private LocalDate fechaExp = LocalDate.now();

	@Column(name = "DistanciaRecorrida")
	private double distancia = 0.00;

	@Column(name = "ParadasVip")
	private int nVips = 0;

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Peregrino peregrino;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idParada", nullable = false)
	private Parada paradaInicial;

	public Carnet() {

	}

	public Carnet(Long id, LocalDate fechaExp, double distancia, int nVips, Peregrino peregrino, Parada paradaInicial) {
		this.id = id;
		this.fechaExp = fechaExp;
		this.distancia = distancia;
		this.nVips = nVips;
		this.peregrino = peregrino;
		this.paradaInicial = paradaInicial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaExp() {
		return fechaExp;
	}

	public void setFechaExp(LocalDate fechaExp) {
		this.fechaExp = fechaExp;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public int getnVips() {
		return nVips;
	}

	public void setnVips(int nVips) {
		this.nVips = nVips;
	}

	public Peregrino getPeregrino() {
		return peregrino;
	}

	public void setPeregrino(Peregrino peregrino) {
		this.peregrino = peregrino;
	}

	public Parada getParadaInicial() {
		return paradaInicial;
	}

	public void setParadaInicial(Parada paradaInicial) {
		this.paradaInicial = paradaInicial;
	}

	@Override
	public int hashCode() {
		return Objects.hash(distancia, fechaExp, id, nVips, paradaInicial);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carnet other = (Carnet) obj;
		return Double.doubleToLongBits(distancia) == Double.doubleToLongBits(other.distancia)
				&& Objects.equals(fechaExp, other.fechaExp) && Objects.equals(id, other.id) && nVips == other.nVips
				&& Objects.equals(paradaInicial, other.paradaInicial) && Objects.equals(peregrino, other.peregrino);
	}

	@Override
	public String toString() {
		return "Carnet [id=" + id + ", fechaExp=" + fechaExp + ", distancia=" + distancia + ", nVips=" + nVips
				+ ", peregrinoId=" + peregrino.getId() + ", paradaInicial=" + paradaInicial + "]";
	}

}
