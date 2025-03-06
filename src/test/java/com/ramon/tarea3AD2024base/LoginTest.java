package com.ramon.tarea3AD2024base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.controller.InicioController;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

	InicioController controller;

	@Mock
	UsuarioService userService;
	@Mock
	StageManager stageManager;

	@BeforeAll
	static void initJfx() {
		Platform.startup(() -> {
		});
	}

	@BeforeEach
	void setUp() {
		controller = new InicioController();
		controller.userService = userService;
		controller.stageManager = stageManager;

		controller.txtUsuario = new TextField();
		controller.password = new PasswordField();
	}

	@Test
	void loginOk_Admin() throws InterruptedException, IOException {
		Usuario usuario = new Usuario();
		usuario.setPerfil(Perfil.ADMIN);

		when(userService.authenticate("admin", "adminpass")).thenReturn(true);
		when(userService.findByNombreOrEmail("admin")).thenReturn(usuario);

		controller.txtUsuario.setText("admin");
		controller.password.setText("adminpass");

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			try {
				controller.login(new ActionEvent());
			} catch (IOException e) {
				e.printStackTrace();
			}
			latch.countDown();
		});
		latch.await(2, TimeUnit.SECONDS);

		verify(stageManager).switchScene(FxmlView.ADMINISTRADOR);
	}

	@Test
	void loginKo_CredencialesInvalidas() throws InterruptedException, IOException {
		when(userService.authenticate(any(), any())).thenReturn(false);

		controller.txtUsuario.setText("invalidUser");
		controller.password.setText("wrongPass");

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			try {
				controller.login(new ActionEvent());
			} catch (IOException e) {
				e.printStackTrace();
			}
			latch.countDown();
		});
		latch.await(2, TimeUnit.SECONDS);

		verify(stageManager, never()).switchScene(any());
	}
}
