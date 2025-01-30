package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Sesion;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.services.CarnetService;
import com.ramon.tarea3AD2024base.services.EstanciaService;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.services.VisitaService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

@Controller
public class ResponsableController implements Initializable {

	@FXML
	private ComboBox<Peregrino> choicePeregrinos;
	@FXML
	private TextField nombreSellar;
	@FXML
	private TextField apellidosSellar;
	@FXML
	private DatePicker fechaNacSellar;
	@FXML
	private RadioButton estanciaSi;
	@FXML
	private RadioButton estanciaNo;
	@FXML
	private RadioButton vipSi;
	@FXML
	private RadioButton vipNo;
	@FXML
	private ToggleGroup afirmacionEstancia;
	@FXML
	private ToggleGroup afirmacionVip;
	@FXML
	private TableView<Peregrino> tablaPeregrinos;

	@Lazy
	@Autowired
	private StageManager stagemanager;

	private Sesion sesion;

	private Usuario usuario;

	@Autowired
	private PeregrinoService peregrinoService;
	@Autowired
	private CarnetService carnetService;
	@Autowired
	private ParadaService paradaService;
	@Autowired
	private EstanciaService estanciaService;
	@Autowired
	private VisitaService visitaService;

	@FXML
	private void volver() {
		stagemanager.switchScene(FxmlView.INICIO);
	}

	@FXML
	private void gestionarEstancia() {
		if (estanciaNo.isSelected()) {
			vipSi.setDisable(true);
			vipNo.setDisable(true);
		} else if (estanciaSi.isSelected()) {
			vipSi.setDisable(false);
			vipNo.setDisable(false);
		}
	}

	@FXML
	private void sellarCarnet() {
		if (choicePeregrinos.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error durante Sellado");
			alert.setHeaderText("Debe seleccionar un peregrino primero");
			alert.show();
		}
		if (estanciaSi.isSelected() && vipSi.isSelected()) {
			Estancia estancia = new Estancia();
			estancia.setFecha(LocalDate.now());
			Parada parada = paradaService.findByUsuario(usuario);
			estancia.setParada(parada);
			estancia.setPeregrino(choicePeregrinos.getValue());
			estancia.setVip(true);

			Estancia nuevaEstancia = estanciaService.save(estancia);
			Carnet carnet = choicePeregrinos.getValue().getCarnet();

			carnet.setDistancia(carnet.getDistancia() + 5.00);
			carnet.setnVips(carnet.getnVips() + 1);

			Carnet actualizaCarnet = carnetService.update(carnet);

			Visita visita = new Visita();

			visita.setFecha(LocalDate.now());
			visita.setParada(parada);
			visita.setPeregrino(choicePeregrinos.getValue());

			Visita nuevaVisita = visitaService.save(visita);

		} else if (estanciaSi.isSelected() && vipNo.isSelected()) {
			Estancia estancia = new Estancia();
			estancia.setFecha(LocalDate.now());
			Parada parada = paradaService.findByUsuario(usuario);
			estancia.setParada(parada);
			estancia.setPeregrino(choicePeregrinos.getValue());
			estancia.setVip(false);

			Estancia nuevaEstancia = estanciaService.save(estancia);

			Carnet carnet = choicePeregrinos.getValue().getCarnet();

			carnet.setDistancia(carnet.getDistancia() + 5.00);

			Carnet actualizaCarnet = carnetService.update(carnet);

			Visita visita = new Visita();

			visita.setFecha(LocalDate.now());
			visita.setParada(parada);
			visita.setPeregrino(choicePeregrinos.getValue());

			Visita nuevaVisita = visitaService.save(visita);
		} else if (estanciaNo.isSelected()) {

			Parada parada = paradaService.findByUsuario(usuario);

			Carnet carnet = choicePeregrinos.getValue().getCarnet();

			carnet.setDistancia(carnet.getDistancia() + 5.00);

			Carnet actualizaCarnet = carnetService.update(carnet);

			Visita visita = new Visita();

			visita.setFecha(LocalDate.now());
			visita.setParada(parada);
			visita.setPeregrino(choicePeregrinos.getValue());

			Visita nuevaVisita = visitaService.save(visita);
		}
	}

	private void llenarChoiceConPeregrinos() {
		List<Peregrino> peregrinos = peregrinoService.findAll();
		choicePeregrinos.getItems().clear();
		choicePeregrinos.getItems().addAll(peregrinos);
	}

	@FXML
	private void llenarCamposPeregrino() {
		Peregrino p = choicePeregrinos.getValue();
		nombreSellar.setText(p.getNombre());
		apellidosSellar.setText(p.getApellidos());
		fechaNacSellar.setValue(p.getFechaNac());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		sesion = InicioController.sesion;
		usuario = sesion.getUsuario();

		estanciaSi.setOnAction(event -> gestionarEstancia());
	    estanciaNo.setOnAction(event -> gestionarEstancia());

		choicePeregrinos.setConverter(new StringConverter<>() {

			@Override
			public String toString(Peregrino peregrino) {
				if (peregrino != null) {
					return peregrino.getNombre() + "-" + peregrino.getId();
				}
				return "Seleccione parada inicial";
			}

			@Override
			public Peregrino fromString(String string) {
				return null;
			}

		});
		llenarChoiceConPeregrinos();

		choicePeregrinos.setOnAction(event -> llenarCamposPeregrino());
	}

	public ComboBox<Peregrino> getChoicePeregrinos() {
		return choicePeregrinos;
	}

	public void setChoicePeregrinos(ComboBox<Peregrino> choicePeregrinos) {
		this.choicePeregrinos = choicePeregrinos;
	}

	public String getNombreSellar() {
		return nombreSellar.getText();
	}

	public void setNombreSellar(TextField nombreSellar) {
		this.nombreSellar = nombreSellar;
	}

	public String getApellidoSellar() {
		return apellidosSellar.getText();
	}

	public void setApellidoSellar(TextField apellidoSellar) {
		this.apellidosSellar = apellidoSellar;
	}

	public DatePicker getFechaNacSellar() {
		return fechaNacSellar;
	}

	public void setFechaNacSellar(DatePicker fechaNacSellar) {
		this.fechaNacSellar = fechaNacSellar;
	}

	public RadioButton getEstanciaSi() {
		return estanciaSi;
	}

	public void setEstanciaSi(RadioButton estanciaSi) {
		this.estanciaSi = estanciaSi;
	}

	public RadioButton getEstanciaNo() {
		return estanciaNo;
	}

	public void setEstanciaNo(RadioButton estanciaNo) {
		this.estanciaNo = estanciaNo;
	}

	public RadioButton getVipSi() {
		return vipSi;
	}

	public void setVipSi(RadioButton vipSi) {
		this.vipSi = vipSi;
	}

	public RadioButton getVipNo() {
		return vipNo;
	}

	public void setVipNo(RadioButton vipNo) {
		this.vipNo = vipNo;
	}

	public ToggleGroup getAfirmacionEstancia() {
		return afirmacionEstancia;
	}

	public void setAfirmacionEstancia(ToggleGroup afirmacionEstancia) {
		this.afirmacionEstancia = afirmacionEstancia;
	}

	public ToggleGroup getAfirmacionVip() {
		return afirmacionVip;
	}

	public void setAfirmacionVip(ToggleGroup afirmacionVip) {
		this.afirmacionVip = afirmacionVip;
	}

	public TableView<Peregrino> getTablaPeregrinos() {
		return tablaPeregrinos;
	}

	public void setTablaPeregrinos(TableView<Peregrino> tablaPeregrinos) {
		this.tablaPeregrinos = tablaPeregrinos;
	}

	public StageManager getStagemanager() {
		return stagemanager;
	}

	public void setStagemanager(StageManager stagemanager) {
		this.stagemanager = stagemanager;
	}

	public PeregrinoService getPeregrinoService() {
		return peregrinoService;
	}

	public void setPeregrinoService(PeregrinoService peregrinoService) {
		this.peregrinoService = peregrinoService;
	}

}
