package com.ramon.tarea3AD2024base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ramon.tarea3AD2024base.controller.RegistroController;
import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.services.VisitaService;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
class RegistroControllerTest {

	RegistroController controller;

	@Mock
	PeregrinoService peregrinoService;
	@Mock
	VisitaService visitaService;
	@Mock
	ParadaService paradaService;

	Parada paradaInicial = new Parada(1L, null, "España", 'E', "Parada 1", new HashSet<>(), new HashSet<>());

	@BeforeAll
	static void initJfx() {
		new JFXPanel();
	}

	@BeforeEach
	void setUp() {
		controller = new RegistroController();
		controller.peregrinoService = peregrinoService;
		controller.visitaService = visitaService;
		controller.paradaService = paradaService;

		controller.usuario = new TextField();
		controller.nombre = new TextField();
		controller.apellidos = new TextField();
		controller.email = new TextField();
		controller.password = new TextField();
		controller.cPassword = new TextField();
		controller.choiceNacionalidad = new ComboBox<>();
		controller.choiceParadas = new ComboBox<>();
		controller.fechaNac = new DatePicker();

		controller.choiceNacionalidad.getItems().add("España");

		controller.choiceParadas.getItems().add(paradaInicial);
	}

	@Test
	void registroOk() throws InterruptedException {
		when(peregrinoService.save(any())).thenAnswer(invocation -> {
			Peregrino p = invocation.getArgument(0);
			p.setCarnet(new Carnet());
			return p;
		});

		controller.usuario.setText("peregrino");
		controller.nombre.setText("Juan");
		controller.apellidos.setText("Perez");
		controller.email.setText("peregrino@mail.com");
		controller.password.setText("password123");
		controller.cPassword.setText("password123");
		controller.choiceNacionalidad.setValue("España");
		controller.choiceParadas.setValue(paradaInicial);
		controller.fechaNac.setValue(LocalDate.of(1990, 1, 1));

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			controller.registrarPeregrino();
			latch.countDown();
		});
		latch.await(2, TimeUnit.SECONDS);

		verify(peregrinoService, times(1)).save(any());
	}

	@Test
	void registroKo_EmailInvalido() throws InterruptedException {
		controller.usuario.setText("peregrino");
		controller.nombre.setText("Juan");
		controller.apellidos.setText("Perez");
		controller.email.setText("emailIncorrecto");
		controller.password.setText("password123");
		controller.cPassword.setText("password123");
		controller.choiceNacionalidad.setValue("España");
		controller.choiceParadas.setValue(controller.choiceParadas.getItems().get(0));
		controller.fechaNac.setValue(LocalDate.of(1990, 1, 1));

		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			controller.registrarPeregrino();
			latch.countDown();
		});
		latch.await(2, TimeUnit.SECONDS);

		verify(peregrinoService, never()).save(any());
	}
}
