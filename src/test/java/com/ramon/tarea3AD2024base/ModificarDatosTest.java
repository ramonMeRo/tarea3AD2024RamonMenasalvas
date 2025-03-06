package com.ramon.tarea3AD2024base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.ramon.tarea3AD2024base.controller.PeregrinoController;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.PeregrinoService;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ModificarDatosTest {

    @BeforeAll
    public static void initJFX() {

        Platform.startup(() ->{});
    }

    @Test
    public void testActualizarPeregrino_ok() throws Exception {
  
        PeregrinoController controller = new PeregrinoController();
        controller.nombre = new TextField();
        controller.apellidos = new TextField();
        controller.usuario = new TextField();
        controller.email = new TextField();
        controller.fechaNac = new DatePicker();
        controller.nacionalidad = new ComboBox<>();
        controller.actualizar = new Button();
        controller.reiniciar = new Button();

        controller.nombre.setText("Juan");
        controller.apellidos.setText("Perez");
        controller.fechaNac.setValue(LocalDate.of(1990, 1, 1));
        controller.nacionalidad.setValue("Spain");

        Peregrino peregrino = new Peregrino();
        peregrino.setNombre("NombreAntiguo");
        peregrino.setApellido("ApellidoAntiguo");
        peregrino.setFechaNac(LocalDate.of(1980, 1, 1));
        peregrino.setNacionalidad("PaisAntiguo");
        controller.peregrino = peregrino;

        Usuario usuario = new Usuario();
        usuario.setNombre("usuarioTest");
        usuario.setEmail("test@example.com");
        controller.usuarioSesion = usuario;

        PeregrinoService serviceMock = mock(PeregrinoService.class);
        controller.peregrinoService = serviceMock;

  
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.actualizarPeregrino();
            latch.countDown();
        });
  
        latch.await(5, TimeUnit.SECONDS);

        assertEquals("Juan", peregrino.getNombre());
        assertEquals("Perez", peregrino.getApellidos());
        assertEquals(LocalDate.of(1990, 1, 1), peregrino.getFechaNac());
        assertEquals("Spain", peregrino.getNacionalidad());
        verify(serviceMock, times(1)).update(peregrino);
    }

    @Test
    public void testActualizarPeregrino_ko_invalidName() throws Exception {

        PeregrinoController controller = new PeregrinoController();
        controller.nombre = new TextField();
        controller.apellidos = new TextField();
        controller.usuario = new TextField();
        controller.email = new TextField();
        controller.fechaNac = new DatePicker();
        controller.nacionalidad = new ComboBox<>();
        controller.actualizar = new Button();
        controller.reiniciar = new Button();

        controller.nombre.setText("");
        controller.apellidos.setText("Perez");
        controller.fechaNac.setValue(LocalDate.of(1990, 1, 1));
        controller.nacionalidad.setValue("Spain");


        Peregrino peregrino = new Peregrino();
        peregrino.setNombre("NombreAntiguo");
        peregrino.setApellido("ApellidoAntiguo");
        peregrino.setFechaNac(LocalDate.of(1980, 1, 1));
        peregrino.setNacionalidad("PaisAntiguo");
        controller.peregrino = peregrino;

 
        Usuario usuario = new Usuario();
        usuario.setNombre("usuarioTest");
        usuario.setEmail("test@example.com");
        controller.usuarioSesion = usuario;

  
        PeregrinoService serviceMock = mock(PeregrinoService.class);
        controller.peregrinoService = serviceMock;

   
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.actualizarPeregrino();
            latch.countDown();
        });
    
        latch.await(5, TimeUnit.SECONDS);


        verify(serviceMock, never()).update(ArgumentMatchers.any());
    }
}
