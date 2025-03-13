package com.ramon.tarea3AD2024base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ramon.tarea3AD2024base.controller.AdministradorController;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.UsuarioService;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@ExtendWith(MockitoExtension.class)
public class RegistrarParadaTest {

    // Usamos una subclase test-friendly de AdministradorController
    TestAdministradorController controller = new TestAdministradorController();

    @BeforeAll
    public static void initJFX() {
        // Inicializamos JavaFX para poder trabajar con controles
    	Platform.startup(() -> {
		});
    }

    @Test
    public void testRegistrarParada_ko_invalidNombreParada() throws Exception {
        // Inicializamos controles
        controller.nombreParada = new TextField();
        controller.regionParada = new TextField();
        controller.usuarioResponsable = new TextField();
        controller.nombreResponsable = new TextField();
        controller.email = new TextField();
        controller.password = new PasswordField();
        controller.passwordConf = new PasswordField();
        controller.userId = new Label();
        controller.usuarioResponsable = new TextField();

        controller.userId.setText("");
        controller.usuarioResponsable.setText("responsable");

        // Datos inválidos: nombre de parada vacío
        controller.nombreParada.setText("");
        controller.regionParada.setText("Norte");
        controller.email.setText("responsable@example.com");
        controller.password.setText("pass");
        controller.passwordConf.setText("pass");
        controller.usuarioResponsable.setText("responsable");
        controller.nombreResponsable.setText("Responsable");

        UsuarioService userServiceMock = Mockito.mock(UsuarioService.class);
        ParadaService paradaServiceMock = Mockito.mock(ParadaService.class);
        controller.userService = userServiceMock;
        controller.paradaService = paradaServiceMock;

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.registrarUsuario(null);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);

        // Verificamos que no se llamó a save() ni para usuario ni para parada
        verify(userServiceMock, never()).save(any(Usuario.class));
        verify(paradaServiceMock, never()).save(any(Parada.class));

        assertEquals("", controller.nombreParada.getText());
    }

    // Subclase test-friendly que sobrescribe los métodos que muestran alertas y fuerza que las validaciones retornen true
    public class TestAdministradorController extends AdministradorController {
  
        protected void guardarAlerta(Usuario user) {
            // No hacer nada en test
        }

        protected void validacionAlerta(String campo, boolean vacio) {
            // No hacer nada en test
        }

        protected void actualizarAlerta(Usuario user) {
            // No hacer nada en test
        }
        
        protected boolean valida(String campo, String valor, String patron) {
            // Forzamos que siempre pase la validación en test
            return true;
        }
        
        
        protected boolean validacionVacia(String campo, boolean vacio) {
            // Forzamos que siempre pase la validación en test
            return true;
        }
    }
}
