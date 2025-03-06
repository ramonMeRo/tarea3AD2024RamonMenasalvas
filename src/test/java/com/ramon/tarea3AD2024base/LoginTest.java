package com.ramon.tarea3AD2024base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
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

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.controller.InicioController;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
class LoginTest {

	@Mock
	UsuarioService userService;

	@Mock
	StageManager stageManager;

	@InjectMocks
	InicioController controller;

	TextField username;
	PasswordField password;

	@BeforeAll
	static void initJfx() {
		new JFXPanel();
	}

	@BeforeEach
	void setUp() {
		username = new TextField();
		password = new PasswordField();
		controller.txtUsuario = username;
		controller.password = password;
	}

	@Test
	void loginOkTest() throws InterruptedException {
		when(userService.authenticate(anyString(), anyString())).thenReturn(true);
		Usuario usuario = new Usuario();
		usuario.setPerfil(Perfil.ADMIN);
		when(userService.findByNombreOrEmail(anyString())).thenReturn(usuario);

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			controller.login(new ActionEvent());
			latch.countDown();
		});
		latch.await();

		verify(stageManager).switchScene(FxmlView.ADMINISTRADOR);
	}

	@Test
	void loginKoTest() throws InterruptedException {
		when(userService.authenticate(anyString(), anyString())).thenReturn(false);

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			controller.login(new ActionEvent());
			latch.countDown();
		});
		latch.await();

		verify(stageManager, never()).switchScene(any());
	}
}