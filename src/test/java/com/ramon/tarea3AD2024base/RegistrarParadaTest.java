package com.ramon.tarea3AD2024base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ramon.tarea3AD2024base.controller.AdministradorController;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.UsuarioService;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrarParadaTest {

    // Usamos la subclase test-friendly
    TestAdministradorController controller = new TestAdministradorController();

    @BeforeAll
    public static void initJFX() {
    	Platform.startup(() ->{});
    }

    @Test
    public void testRegistrarParada_ok() throws Exception {
        // Configurar los controles en el controller test-friendly
        controller.nombreParada = new TextField();
        controller.regionParada = new TextField();
        controller.usuarioResponsable = new TextField();
        controller.nombreResponsable = new TextField();
        controller.email = new TextField();
        controller.password = new PasswordField();
        controller.passwordConf = new PasswordField();
        controller.userId = new Label();

        // Simulamos un registro nuevo forzando que userId sea null o vacío
        controller.userId.setText(null);

        // Datos válidos
        controller.nombreParada.setText("Parada");
        controller.regionParada.setText("Norte");
        controller.email.setText("responsable@example.com");
        controller.password.setText("pass");
        controller.passwordConf.setText("pass");
        controller.usuarioResponsable.setText("responsable");
        controller.nombreResponsable.setText("Responsable");

        // Inyectar mocks
        UsuarioService userServiceMock = mock(UsuarioService.class);
        ParadaService paradaServiceMock = mock(ParadaService.class);
        controller.userService = userServiceMock;
        controller.paradaService = paradaServiceMock;

        // Stub para que no se detecte duplicado
        when(paradaServiceMock.existsByNombre("Parada")).thenReturn(false);
        when(paradaServiceMock.existsByRegion('N')).thenReturn(false);

        // Stub para guardar
        Usuario savedUser = new Usuario();
        savedUser.setNombre("responsable");
        savedUser.setEmail("responsable@example.com");
        savedUser.setPassword("pass");
        savedUser.setPerfil(Perfil.PARADA);
        when(userServiceMock.save(any(Usuario.class))).thenReturn(savedUser);

        Parada savedParada = new Parada();
        savedParada.setNombre("Parada");
        savedParada.setRegion('N');
        savedParada.setResponsable("Responsable");
        when(paradaServiceMock.save(any(Parada.class))).thenReturn(savedParada);

        // Ejecutar en el FX Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.registrarUsuario(null);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);

        // Verificar las interacciones
        verify(userServiceMock, times(1)).save(any(Usuario.class));
        verify(paradaServiceMock, times(1)).save(any(Parada.class));

        // Verificar que se hayan limpiado los campos
        assertNull(controller.userId.getText());
        assertEquals("", controller.regionParada.getText());
        assertEquals("", controller.usuarioResponsable.getText());
        assertEquals("", controller.nombreResponsable.getText());
        assertEquals("", controller.nombreParada.getText());
        assertEquals("", controller.email.getText());
        assertEquals("", controller.password.getText());
        assertEquals("", controller.passwordConf.getText());
    }

    @Test
    public void testRegistrarParada_ko_invalidNombreParada() throws Exception {
        // Configurar controles
        controller.nombreParada = new TextField();
        controller.regionParada = new TextField();
        controller.usuarioResponsable = new TextField();
        controller.nombreResponsable = new TextField();
        controller.email = new TextField();
        controller.password = new PasswordField();
        controller.passwordConf = new PasswordField();
        controller.userId = new Label();

        controller.userId.setText(null);

        // Datos inválidos: nombreParada vacío
        controller.nombreParada.setText("");
        controller.regionParada.setText("Norte");
        controller.email.setText("responsable@example.com");
        controller.password.setText("pass");
        controller.passwordConf.setText("pass");
        controller.usuarioResponsable.setText("responsable");
        controller.nombreResponsable.setText("Responsable");

        // Inyectar mocks
        UsuarioService userServiceMock = mock(UsuarioService.class);
        ParadaService paradaServiceMock = mock(ParadaService.class);
        controller.userService = userServiceMock;
        controller.paradaService = paradaServiceMock;

        // Ejecutar en el FX Application Thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.registrarUsuario(null);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);

        // Verificar que no se invoca save() porque falla la validación
        verify(userServiceMock, never()).save(any(Usuario.class));
        verify(paradaServiceMock, never()).save(any(Parada.class));

        // El campo nombreParada conserva el valor vacío
        assertEquals("", controller.nombreParada.getText());
    }

    // Subclase para evitar mostrar Alert durante el test
    public class TestAdministradorController extends AdministradorController {
        protected void guardarAlerta(Usuario user) {
            // Evitar mostrar el Alert en test
        }
       
        protected void validacionAlerta(String campo, boolean vacio) {
            // Evitar mostrar el Alert en test
        }
       
        protected void actualizarAlerta(Usuario user) {
            // Evitar mostrar el Alert en test
        }
    }
}
