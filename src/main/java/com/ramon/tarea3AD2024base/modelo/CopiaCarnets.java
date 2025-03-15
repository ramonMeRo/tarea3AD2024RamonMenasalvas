package com.ramon.tarea3AD2024base.modelo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "copia_carnets")
public class CopiaCarnets {
	@Id
	private String id;
	private String nombreArchivo;
	private LocalDateTime fechaBackup;

	 @Field("carnets")
	private List<Carnet> carnets;

	public CopiaCarnets() {
	}

	public CopiaCarnets(String nombreArchivo, List<Carnet> carnets) {
		this.nombreArchivo = nombreArchivo;
		this.fechaBackup = LocalDateTime.now();
		this.carnets = carnets;
	}

	public String getId() {
		return id;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public LocalDateTime getFechaBackup() {
		return fechaBackup;
	}

	public List<Carnet> getCarnets() {
		return carnets;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public void setFechaBackup(LocalDateTime fechaBackup) {
		this.fechaBackup = fechaBackup;
	}

	public void setCarnets(List<Carnet> carnets) {
		this.carnets = carnets;
	}


}
