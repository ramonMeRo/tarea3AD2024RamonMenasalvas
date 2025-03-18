package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.EstanciaTabla;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controlador de la ventana del responsable de una parada que tiene funciones
 * para la administración de su parada, sellado del carnet de peregrinos y la
 * contratacion de servicios por parte de estos
 * 
 * @author Ramon
 * @since 01-01-2025
 */

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
	private TableView<EstanciaTabla> tablaEstancias;
	@FXML
	private TableColumn<EstanciaTabla, Long> idEstancia;
	@FXML
	private TableColumn<EstanciaTabla, String> nombreParada;
	@FXML
	private TableColumn<EstanciaTabla, String> nombrePeregrino;
	@FXML
	private TableColumn<EstanciaTabla, Boolean> columnaVip;
	@FXML
	private TableColumn<EstanciaTabla, LocalDate> columnaFecha;
	@FXML
	private DatePicker fechaInicio;
	@FXML
	private DatePicker fechaFin;

	@FXML
	private ObservableList<EstanciaTabla> listaFxEstancia = FXCollections.observableArrayList();

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

	/**
	 * Vuelve a la pantalla de inicio de la aplicación.
	 * 
	 * @see StageManager#switchScene(FxmlView)
	 */
	@FXML
	private void volver() {
		stagemanager.switchScene(FxmlView.INICIO);
	}

	/**
	 * Exporta la información de estancias registradas en una parada específica.
	 * 
	 * Este método obtiene la lista de estancias asociadas a la parada gestionada
	 * por el usuario actual. Si la fecha de inicio es anterior a la fecha de fin,
	 * recorre todas las estancias y las almacena en una lista `listaFxEstancia`
	 * para su visualización en la interfaz gráfica.
	 * 
	 * 
	 * Cada estancia se transforma en un objeto `EstanciaTabla`, el cual contiene:
	 * 
	 * ID de la estancia Nombre de la parada Nombre completo del peregrino Si la
	 * estancia fue VIP Fecha de la estancia
	 * 
	 * 
	 * Luego, la lista `listaFxEstancia` se asigna a `tablaEstancias` para
	 * actualizar la interfaz gráfica.
	 * 
	 * @see ParadaService#findByUsuario(Usuario)
	 * @see Parada#getListaEstancias()
	 * @see EstanciaTabla
	 * @see javafx.scene.control.TableView#setItems(javafx.collections.ObservableList)
	 */
	@FXML
	private void exportarParada() {

		Parada parada = paradaService.findByUsuario(usuario);
		EstanciaTabla estanciaTabla = new EstanciaTabla();
		listaFxEstancia.clear();
		if (fechaInicio.getValue().isBefore(fechaFin.getValue())) {
			Set<Estancia> listaEstancias = parada.getListaEstancias();
			for (Estancia estancia : listaEstancias) {
				estanciaTabla = new EstanciaTabla();
				estanciaTabla.setId(estancia.getId());
				estanciaTabla.setNombreParada(estancia.getParada().getNombre());
				estanciaTabla.setNombrePeregrino(
						estancia.getPeregrino().getNombre() + " " + estancia.getPeregrino().getApellidos());
				estanciaTabla.setVip(estancia.isVip());
				estanciaTabla.setFecha(estancia.getFecha());

				System.out.println(estanciaTabla.toString());

				listaFxEstancia.add(estanciaTabla);
			}
			tablaEstancias.setItems(listaFxEstancia);
		}
	}

	/**
	 * Maneja la pulsación de la tecla F1 para mostrar la ayuda.
	 * 
	 * @param event Evento del teclado.
	 */
	public void ayudaF1(KeyEvent event) {
		if (event.getCode().toString().equals("F1")) {
			ayuda();
		}
	}

	/**
	 * Muestra la ventana de ayuda con el manual de usuario.
	 */
	@FXML
	private void ayuda() {
		try {
			WebView webView = new WebView();

			String url = getClass().getResource("/help/html/Responsable.html").toExternalForm();
			webView.getEngine().load(url);

			Stage helpStage = new Stage();
			helpStage.setTitle("Ayuda");

			Scene helpScene = new Scene(webView, 850, 520);

			helpStage.setScene(helpScene);

			helpStage.initModality(Modality.APPLICATION_MODAL);
			helpStage.setResizable(true);
			helpStage.show();

		} catch (NullPointerException e) {
			System.out.print("No se ha encontrado el HTML");
		}
	}

	/**
	 * Gestiona la disponibilidad de las opciones VIP en función de la selección de
	 * estancia.
	 * 
	 * Si el usuario selecciona que no se quedará en la parada (estanciaNo),
	 * entonces las opciones de VIP (vipSi y vipNo) se deshabilitan, ya que solo
	 * pueden aplicarse cuando hay una estancia. En caso contrario, si el usuario
	 * elige quedarse (estanciaSi), las opciones de VIP se habilitan nuevamente.
	 * 
	 * Este método se activa cuando el usuario interactúa con los botones de radio
	 * para seleccionar si se quedará en la parada.
	 * 
	 * @see #estanciaNo
	 * @see #estanciaSi
	 * @see #vipSi
	 * @see #vipNo
	 */
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

	/**
	 * Realiza el proceso de sellado del carnet de un peregrino en la parada actual.
	 * 
	 * Este método verifica que el peregrino no haya sellado su carnet en la fecha
	 * actual y, dependiendo de si ha realizado una estancia y si ha sido VIP,
	 * actualiza los registros en la base de datos, incluyendo las entidades
	 * Estancia, Visita y Carnet.
	 * 
	 * Si el peregrino ya selló en el día actual, se muestra un mensaje de error.
	 * 
	 * Si se ha seleccionado estancia y VIP, se registra la estancia como VIP y se
	 * actualiza la distancia y visitas VIP del carnet.
	 * 
	 * Si solo se ha seleccionado estancia, se registra sin VIP y se actualiza la
	 * distancia del carnet.
	 * 
	 * Si no se selecciona estancia, solo se registra la visita y se actualiza la
	 * distancia del carnet.
	 * 
	 * Si el proceso se completa correctamente, se almacenan los cambios en la base
	 * de datos.
	 *
	 * @throws NullPointerException si no se selecciona un peregrino.
	 */
	@FXML
	private void sellarCarnet() {

		if (choicePeregrinos.getValue() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error durante Sellado");
			alert.setHeaderText("Debe seleccionar un peregrino primero");
			alert.show();
		}

		List<Visita> visitasPeregrino = visitaService.findByPeregrino(choicePeregrinos.getValue());

		for (Visita visita : visitasPeregrino) {

			if (visita.getFecha().equals(LocalDate.now())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error durante Sellado");
				alert.setHeaderText("El peregrino ya sello hoy");
				alert.show();
				return;
			}
		}

		if (estanciaSi.isSelected() && vipSi.isSelected()) {
			Estancia estancia = new Estancia();
			estancia.setFecha(LocalDate.now());
			Parada parada = paradaService.findByUsuario(usuario);
			estancia.setParada(parada);
			estancia.setPeregrino(choicePeregrinos.getValue());
			estancia.setVip(true);

			@SuppressWarnings("unused")
			Estancia nuevaEstancia = estanciaService.save(estancia);
			Carnet carnet = choicePeregrinos.getValue().getCarnet();

			carnet.setDistancia(carnet.getDistancia() + 5.00);
			carnet.setnVips(carnet.getnVips() + 1);

			@SuppressWarnings("unused")
			Carnet actualizaCarnet = carnetService.update(carnet);

			Visita visita = new Visita();

			visita.setFecha(LocalDate.now());
			visita.setParada(parada);
			visita.setPeregrino(choicePeregrinos.getValue());

			parada.getVisitas().add(visita);

			@SuppressWarnings("unused")
			Visita nuevaVisita = visitaService.save(visita);

		} else if (estanciaSi.isSelected() && vipNo.isSelected()) {
			Estancia estancia = new Estancia();
			estancia.setFecha(LocalDate.now());
			Parada parada = paradaService.findByUsuario(usuario);
			estancia.setParada(parada);
			estancia.setPeregrino(choicePeregrinos.getValue());
			estancia.setVip(false);

			@SuppressWarnings("unused")
			Estancia nuevaEstancia = estanciaService.save(estancia);

			Carnet carnet = choicePeregrinos.getValue().getCarnet();

			carnet.setDistancia(carnet.getDistancia() + 5.00);

			@SuppressWarnings("unused")
			Carnet actualizaCarnet = carnetService.update(carnet);

			Visita visita = new Visita();

			visita.setFecha(LocalDate.now());
			visita.setParada(parada);
			visita.setPeregrino(choicePeregrinos.getValue());

			parada.getVisitas().add(visita);

			@SuppressWarnings("unused")
			Visita nuevaVisita = visitaService.save(visita);
		} else if (estanciaNo.isSelected()) {

			Parada parada = paradaService.findByUsuario(usuario);

			Carnet carnet = choicePeregrinos.getValue().getCarnet();

			carnet.setDistancia(carnet.getDistancia() + 5.00);

			@SuppressWarnings("unused")
			Carnet actualizaCarnet = carnetService.update(carnet);

			Visita visita = new Visita();

			visita.setFecha(LocalDate.now());
			visita.setParada(parada);
			visita.setPeregrino(choicePeregrinos.getValue());

			parada.getVisitas().add(visita);

			@SuppressWarnings("unused")
			Visita nuevaVisita = visitaService.save(visita);
		}
	}

	/**
	 * Llena el ComboBox con la lista de peregrinos disponibles en la base de datos.
	 * Se obtiene la lista de peregrinos desde el servicio y se actualiza la
	 * interfaz.
	 */
	private void llenarChoiceConPeregrinos() {
		List<Peregrino> peregrinos = peregrinoService.findAll();
		choicePeregrinos.getItems().clear();
		choicePeregrinos.getItems().addAll(peregrinos);
	}

	/**
	 * Llena los campos de texto con los datos del peregrino seleccionado en el
	 * ComboBox. Al seleccionar un peregrino, sus datos se reflejan en los campos de
	 * la interfaz.
	 */
	@FXML
	private void llenarCamposPeregrino() {
		Peregrino p = choicePeregrinos.getValue();
		nombreSellar.setText(p.getNombre());
		apellidosSellar.setText(p.getApellidos());
		fechaNacSellar.setValue(p.getFechaNac());
	}

	/**
	 * Inicializa la ventana al cargar la interfaz de usuario.
	 * 
	 * @param location  La ubicación utilizada para resolver rutas relativas para el
	 *                  objeto raíz, o null si no se proporciona.
	 * 
	 * @param resources Los recursos utilizados para localizar la interfaz de
	 *                  usuario, o null si no se proporciona.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		sesion = InicioController.sesion;
		usuario = sesion.getUsuario();

		fechaFin.setValue(LocalDate.now());

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

		idEstancia.setCellValueFactory(new PropertyValueFactory<>("id"));
		nombreParada.setCellValueFactory(new PropertyValueFactory<>("nombreParada"));
		nombrePeregrino.setCellValueFactory(new PropertyValueFactory<>("nombrePeregrino"));
		columnaVip.setCellValueFactory(new PropertyValueFactory<>("vip"));
		columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
	}

	/**
	 * Obtiene el ComboBox de peregrinos disponibles.
	 * 
	 * @return El ComboBox que contiene los peregrinos.
	 */
	public ComboBox<Peregrino> getChoicePeregrinos() {
		return choicePeregrinos;
	}

	/**
	 * Establece el ComboBox de peregrinos.
	 * 
	 * @param choicePeregrinos El ComboBox a establecer.
	 */
	public void setChoicePeregrinos(ComboBox<Peregrino> choicePeregrinos) {
		this.choicePeregrinos = choicePeregrinos;
	}

	/**
	 * Obtiene el texto del campo de nombre del peregrino a sellar.
	 * 
	 * @return El nombre del peregrino a sellar.
	 */
	public String getNombreSellar() {
		return nombreSellar.getText();
	}

	/**
	 * Establece el campo de texto del nombre del peregrino a sellar.
	 * 
	 * @param nombreSellar El campo de texto a establecer.
	 */
	public void setNombreSellar(TextField nombreSellar) {
		this.nombreSellar = nombreSellar;
	}

	/**
	 * Obtiene el texto del campo de apellidos del peregrino a sellar.
	 * 
	 * @return Los apellidos del peregrino a sellar.
	 */
	public String getApellidoSellar() {
		return apellidosSellar.getText();
	}

	/**
	 * Establece el campo de texto de los apellidos del peregrino a sellar.
	 * 
	 * @param apellidoSellar El campo de texto a establecer.
	 */
	public void setApellidoSellar(TextField apellidoSellar) {
		this.apellidosSellar = apellidoSellar;
	}

	/**
	 * Obtiene la fecha de nacimiento del peregrino a sellar.
	 * 
	 * @return Un DatePicker con la fecha de nacimiento.
	 */
	public DatePicker getFechaNacSellar() {
		return fechaNacSellar;
	}

	/**
	 * Establece la fecha de nacimiento del peregrino a sellar.
	 * 
	 * @param fechaNacSellar El DatePicker a establecer.
	 */
	public void setFechaNacSellar(DatePicker fechaNacSellar) {
		this.fechaNacSellar = fechaNacSellar;
	}

	/**
	 * Obtiene el RadioButton que indica si el peregrino ha realizado una estancia.
	 * 
	 * @return El RadioButton correspondiente.
	 */
	public RadioButton getEstanciaSi() {
		return estanciaSi;
	}

	/**
	 * Establece el RadioButton que indica si el peregrino ha realizado una
	 * estancia.
	 * 
	 * @param estanciaSi El RadioButton a establecer.
	 */
	public void setEstanciaSi(RadioButton estanciaSi) {
		this.estanciaSi = estanciaSi;
	}

	/**
	 * Obtiene el RadioButton que indica si el peregrino no ha realizado una
	 * estancia.
	 * 
	 * @return El RadioButton correspondiente.
	 */
	public RadioButton getEstanciaNo() {
		return estanciaNo;
	}

	/**
	 * Establece el RadioButton que indica si el peregrino no ha realizado una
	 * estancia.
	 * 
	 * @param estanciaNo El RadioButton a establecer.
	 */
	public void setEstanciaNo(RadioButton estanciaNo) {
		this.estanciaNo = estanciaNo;
	}

	/**
	 * Obtiene el RadioButton que indica si el peregrino tiene categoría VIP.
	 * 
	 * @return El RadioButton correspondiente.
	 */
	public RadioButton getVipSi() {
		return vipSi;
	}

	/**
	 * Establece el RadioButton que indica si el peregrino tiene categoría VIP.
	 * 
	 * @param vipSi El RadioButton a establecer.
	 */
	public void setVipSi(RadioButton vipSi) {
		this.vipSi = vipSi;
	}

	/**
	 * Obtiene el RadioButton que indica si el peregrino no tiene categoría VIP.
	 * 
	 * @return El RadioButton correspondiente.
	 */
	public RadioButton getVipNo() {
		return vipNo;
	}

	/**
	 * Establece el RadioButton que indica si el peregrino no tiene categoría VIP.
	 * 
	 * @param vipNo El RadioButton a establecer.
	 */
	public void setVipNo(RadioButton vipNo) {
		this.vipNo = vipNo;
	}

	/**
	 * Obtiene el ToggleGroup para seleccionar si el peregrino realizó una estancia.
	 * 
	 * @return El ToggleGroup correspondiente.
	 */
	public ToggleGroup getAfirmacionEstancia() {
		return afirmacionEstancia;
	}

	/**
	 * Establece el ToggleGroup para seleccionar si el peregrino realizó una
	 * estancia.
	 * 
	 * @param afirmacionEstancia El ToggleGroup a establecer.
	 */
	public void setAfirmacionEstancia(ToggleGroup afirmacionEstancia) {
		this.afirmacionEstancia = afirmacionEstancia;
	}

	/**
	 * Obtiene el ToggleGroup para seleccionar si el peregrino tiene categoría VIP.
	 * 
	 * @return El ToggleGroup correspondiente.
	 */
	public ToggleGroup getAfirmacionVip() {
		return afirmacionVip;
	}

	/**
	 * Establece el ToggleGroup para seleccionar si el peregrino tiene categoría
	 * VIP.
	 * 
	 * @param afirmacionVip El ToggleGroup a establecer.
	 */
	public void setAfirmacionVip(ToggleGroup afirmacionVip) {
		this.afirmacionVip = afirmacionVip;
	}

	/**
	 * Obtiene la tabla de estancias registradas.
	 * 
	 * @return La TableView de estancias.
	 */
	public TableView<EstanciaTabla> getTablaEstancias() {
		return tablaEstancias;
	}

	/**
	 * Establece la tabla de estancias registradas.
	 * 
	 * @param tablaEstancias La TableView a establecer.
	 */
	public void setTablaPeregrinos(TableView<EstanciaTabla> tablaEstancias) {
		this.tablaEstancias = tablaEstancias;
	}

	/**
	 * Obtiene el administrador de ventanas de la aplicación.
	 * 
	 * @return El StageManager actual.
	 */
	public StageManager getStagemanager() {
		return stagemanager;
	}

	/**
	 * Establece el administrador de ventanas de la aplicación.
	 * 
	 * @param stagemanager El StageManager a establecer.
	 */
	public void setStagemanager(StageManager stagemanager) {
		this.stagemanager = stagemanager;
	}

	/**
	 * Obtiene el servicio de gestión de peregrinos.
	 * 
	 * @return El servicio de PeregrinoService.
	 */
	public PeregrinoService getPeregrinoService() {
		return peregrinoService;
	}

	/**
	 * Establece el servicio de gestión de peregrinos.
	 * 
	 * @param peregrinoService El PeregrinoService a establecer.
	 */
	public void setPeregrinoService(PeregrinoService peregrinoService) {
		this.peregrinoService = peregrinoService;
	}

}
