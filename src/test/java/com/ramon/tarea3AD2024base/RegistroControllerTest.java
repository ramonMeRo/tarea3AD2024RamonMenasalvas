package com.ramon.tarea3AD2024base;

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.controller.RegistroController;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.services.VisitaService;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

@ExtendWith(MockitoExtension.class)
public class RegistroControllerTest {

	@InjectMocks
	private RegistroController registroController;

	@Mock
	private TextField usuario;
	@Mock
	private TextField nombre;
	@Mock
	private TextField apellidos;
	@Mock
	private TextField email;
	@Mock
	private TextField password;
	@Mock
	private TextField cPassword;
	@Mock
	private ComboBox<String> choiceNacionalidad;
	@Mock
	private ComboBox<Parada> choiceParadas;
	@Mock
	private Button btnConfirmar;
	@Mock
	private Button btnCancelar;
	@Mock
	private ImageView btnVisible1;
	@Mock
	private ImageView btnVisible2;
	@Mock
	private TextField passwordVisible1;
	@Mock
	private TextField passwordVisible2;
	@Mock
	private DatePicker fechaNac;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private ParadaService paradaService;

	@Autowired
	private PeregrinoService peregrinoService;

	@Autowired
	private VisitaService visitaService;

	private String contra = "";

	private String contra1 = "";

	public Sesion sesion;

	@BeforeEach
	public void SetUp() {
		Usuario usuarioResp = new Usuario(1L, "responsable", Perfil.PARADA, "usuario@usuario.com", "usuario");
		Set<Estancia> listaEstancias = new HashSet<>();
		Set<Visita> listaVisitas = new HashSet<>();
		when(usuario.getText()).thenReturn("Peregrino1");
		when(nombre.getText()).thenReturn("Peregrino Nombre");
		when(apellidos.getText()).thenReturn("Peregrino Apellidos");
		when(email.getText()).thenReturn("peregrino@peregrino.com");
		when(password.getText()).thenReturn("peregrino");
		when(cPassword.getText()).thenReturn("peregrino");
		when(choiceNacionalidad.getValue()).thenReturn("España");
		when(choiceParadas.getValue())
				.thenReturn(new Parada(1L, usuarioResp, "España", 'E', "Responsable 1", listaEstancias, listaVisitas));
		
		
		
	}

	public String getUsuario() {
		return usuario.getText();
	}

	public void setUsuario(TextField usuarioRegis) {
		this.usuario = usuarioRegis;
	}

	public String getNombre() {
		return nombre.getText();
	}

	public void setNombre(TextField nombreRegis) {
		this.nombre = nombreRegis;
	}

	public String getApellidos() {
		return apellidos.getText();
	}

	public void setApellidos(TextField apellidosRegis) {
		this.apellidos = apellidosRegis;
	}

	public String getEmail() {
		return email.getText();
	}

	public void setEmail(TextField emailRegis) {
		this.email = emailRegis;
	}

	public String getPassword() {
		return password.getText();
	}

	public void setPassword(TextField passwordRegis) {
		this.password = passwordRegis;
	}

	public String getCPassword() {
		return cPassword.getText();
	}

	public void setCPassword(TextField cPasswordRegis) {
		this.cPassword = cPasswordRegis;
	}

	public ComboBox<String> getChoiceNacionalidad() {
		return choiceNacionalidad;
	}

	public void setChoiceNacionalidad(ComboBox<String> choiceNacionalidad) {
		this.choiceNacionalidad = choiceNacionalidad;
	}

	public ComboBox<Parada> getChoiceParadas() {
		return choiceParadas;
	}

	public void setChoiceParadas(ComboBox<Parada> choiceParadas) {
		this.choiceParadas = choiceParadas;
	}

	public ParadaService getParadaService() {
		return paradaService;
	}

	public void setParadaService(ParadaService paradaService) {
		this.paradaService = paradaService;
	}

	public DatePicker getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(DatePicker fechaNac) {
		this.fechaNac = fechaNac;
	}
}
