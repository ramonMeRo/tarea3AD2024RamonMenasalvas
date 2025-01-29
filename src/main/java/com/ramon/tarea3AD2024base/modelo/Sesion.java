package com.ramon.tarea3AD2024base.modelo;

public class Sesion {

	private static Sesion instance;
	
	private Usuario usuario;
	
	public Sesion() {
	}

	public Sesion(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public static Sesion getSesion() {
		if (instance == null) {
			instance = new Sesion();
		}
		return instance;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
