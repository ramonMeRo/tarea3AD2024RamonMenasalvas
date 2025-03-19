package com.ramon.tarea3AD2024base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.ramon.tarea3AD2024base.controller.ResponsableController;
import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.services.CarnetService;
import com.ramon.tarea3AD2024base.services.EstanciaService;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.VisitaService;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // 🔹 Evita errores de stubbing innecesario
class ParadaControllerTest {

    @InjectMocks
    private ResponsableController paradaController;

    @Mock
    private ParadaService paradaService;

    @Mock
    private VisitaService visitaService;

    @Mock
    private EstanciaService estanciaService;

    @Mock
    private CarnetService carnetService;

    @Mock
    private ComboBox<Peregrino> choicePeregrinos;

    @Mock
    private RadioButton estanciaSi, estanciaNo, vipSi, vipNo;

    @Mock
    private Usuario usuario;

    private Peregrino peregrino;
    private Parada parada;
    private Carnet carnet;

    @BeforeAll
    public static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() {
        // Simulamos un peregrino con su carnet y parada
        peregrino = new Peregrino();
        peregrino.setId(1L);
        peregrino.setNombre("Juan");
        peregrino.setApellido("Pérez");

        carnet = new Carnet();
        carnet.setId(1L);
        carnet.setDistancia(10.0);
        carnet.setnVips(2);
        peregrino.setCarnet(carnet);

        parada = new Parada();
        parada.setId(1L);
        parada.setNombre("Parada 1");

        // Simulación del servicio de parada
        Mockito.lenient().when(paradaService.findByUsuario(usuario)).thenReturn(parada);
    }

    @Test
    void testSellarCarnet_SinSeleccionarPeregrino() {
        Mockito.lenient().when(choicePeregrinos.getValue()).thenReturn(null);

        Platform.runLater(() -> {
            paradaController.sellarCarnet();
            // Se espera que el método no continúe y genere una alerta de error
            verify(visitaService, never()).findByPeregrino(any());
        });
    }

    @Test
    void testSellarCarnet_PeregrinoYaSelladoHoy() {
        Mockito.lenient().when(choicePeregrinos.getValue()).thenReturn(peregrino);
        List<Visita> visitasHoy = List.of(new Visita(peregrino, parada, LocalDate.now()));

        Mockito.lenient().when(visitaService.findByPeregrino(peregrino)).thenReturn(visitasHoy);

        Platform.runLater(() -> {
            paradaController.sellarCarnet();
            // Verificamos que no se intente registrar otra visita
            verify(visitaService, never()).save(any());
        });
    }

    @Test
    void testSellarCarnet_ConEstanciaYVip() {
        Mockito.lenient().when(choicePeregrinos.getValue()).thenReturn(peregrino);
        Mockito.lenient().when(estanciaSi.isSelected()).thenReturn(true);
        Mockito.lenient().when(vipSi.isSelected()).thenReturn(true);
        Mockito.lenient().when(visitaService.findByPeregrino(peregrino)).thenReturn(new ArrayList<>());

        Platform.runLater(() -> {
            paradaController.sellarCarnet();
            verify(estanciaService).save(any(Estancia.class));
            verify(visitaService).save(any(Visita.class));
            verify(carnetService).update(any(Carnet.class));
        });
    }

    @Test
    void testSellarCarnet_ConEstanciaSinVip() {
        Mockito.lenient().when(choicePeregrinos.getValue()).thenReturn(peregrino);
        Mockito.lenient().when(estanciaSi.isSelected()).thenReturn(true);
        Mockito.lenient().when(vipNo.isSelected()).thenReturn(true);
        Mockito.lenient().when(visitaService.findByPeregrino(peregrino)).thenReturn(new ArrayList<>());

        Platform.runLater(() -> {
            paradaController.sellarCarnet();
            verify(estanciaService).save(any(Estancia.class));
            verify(visitaService).save(any(Visita.class));
            verify(carnetService).update(any(Carnet.class));
        });
    }

    @Test
    void testSellarCarnet_SinEstancia() {
        Mockito.lenient().when(choicePeregrinos.getValue()).thenReturn(peregrino);
        Mockito.lenient().when(estanciaNo.isSelected()).thenReturn(true);
        Mockito.lenient().when(visitaService.findByPeregrino(peregrino)).thenReturn(new ArrayList<>());

        Platform.runLater(() -> {
            paradaController.sellarCarnet();
            verify(visitaService).save(any(Visita.class));
            verify(carnetService).update(any(Carnet.class));
        });
    }
}
