package com.ramon.tarea3AD2024base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.Platform;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ramon.tarea3AD2024base.controller.RegistroController;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.UsuarioService;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
class RegistroControllerTest {

	@Mock
	UsuarioService usuarioService;

	@InjectMocks
	RegistroController registroController;

	TextField nombre;
	TextField email;
	PasswordField password;

	@BeforeAll
	static void setUpJavaFX() {
			Platform.startup(() ->{});
		
	}

	@BeforeEach
	void setUp() {
		nombre = new TextField();
		email = new TextField();
		password = new PasswordField();

		registroController.nombre = nombre;
		registroController.email = email;
		registroController.password = password;
	}

	@Test
	void registroUsuarioOK() throws InterruptedException {
		nombre.setText("Juan Perez");
		email.setText("juan.perez@example.com");
		password.setText("password123");

		when(usuarioService.save(any(Usuario.class))).thenReturn(true);

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			registroController.registrarUsuario();
			latch.countDown();
		});
		latch.await();

		verify(usuarioService, times(1)).save(any(Usuario.class));
	}

	@Test
	void registroConDatosInvalidos() throws InterruptedException {
		nombre.setText("1234");
		email.setText("invalido");
		password.setText("short");

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			registroController.registrarUsuario();
			latch.countDown();
		});
		latch.await();

		verify(usuarioService, never()).save(any());
	}

	@Test
	void registrarUsuarioConDatosValidos() throws InterruptedException {
		nombre.setText("Maria Garcia");
		email.setText("maria@gmail.com");
		password.setText("claveSegura123");

		when(usuarioService.save(any(Usuario.class))).thenReturn(true);

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			registroController.registrarUsuario();
			latch.countDown();
		});
		latch.await();

		verify(usuarioService, times(1)).save(any(Usuario.class));
	}

	@Test
	void registroUsuarioExistente() throws InterruptedException {
		nombre.setText("Juan");
		email.setText("juan@gmail.com");
		password.setText("12345678");

		when(usuarioService.save(any(Usuario.class))).thenReturn(false);

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			registroController.registrarUsuario();
			latch.countDown();
		});
		latch.await();

		verify(usuarioService, times(1)).save(any(Usuario.class));
	}
}