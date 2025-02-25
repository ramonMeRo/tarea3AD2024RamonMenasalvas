package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Servicio;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.Db4oService;
import com.ramon.tarea3AD2024base.services.ParadaService;
import com.ramon.tarea3AD2024base.services.UsuarioService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author Ramon
 * @since 05-04-2017
 */

@Controller
public class AdministradorController implements Initializable {

	@FXML
	private Button btnSalir;

	@FXML
	private Label userId;

	@FXML
	private TextField nombreParada;

	@FXML
	private TextField regionParada;

	@FXML
	private TextField txtServicio;

	@FXML
	private TextField txtPrecio;

	@FXML
	private TextField usuarioResponsable;

	@FXML
	private TextField nombreResponsable;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField passwordConf;

	@FXML
	private Button reinicio;

	@FXML
	private Button guardarusuario;

	@FXML
	private Button reinicioServicios;

	@FXML
	private Button confirmarServicios;

	@FXML
	private Button terminaServicios;

	@FXML
	private TableView<Parada> paradaTable;

	@FXML
	private TableColumn<Parada, Long> colParadaId;

	@FXML
	private TableColumn<Parada, Character> colRegion;

	@FXML
	private TableColumn<Parada, String> colNombreParada;

	@FXML
	private TableColumn<Parada, String> colResponsable;

	@FXML
	private TableView<Servicio> serviciosTable;

	@FXML
	private TableColumn<Servicio, Long> colIdServicio;

	@FXML
	private TableColumn<Servicio, String> colNombreServicio;

	@FXML
	private TableColumn<Servicio, String> colPrecio;

	@FXML
	private TableColumn<Servicio, List<Long>> colIdParadas;

	@FXML
	private ListView<Parada> listParadas;

	@FXML
	private MenuItem borrarUsuario;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private UsuarioService userService;
	@Autowired
	private ParadaService paradaService;
	@Autowired
	private Db4oService db4oService;

	private ObservableList<Parada> paradaList = FXCollections.observableArrayList();

	private ObservableList<Servicio> servicioList = FXCollections.observableArrayList();

	private ObservableList<Parada> paradasSelect = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		serviciosTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		serviciosTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				txtServicio.setText(newValue.getNombre());
				txtPrecio.setText(String.valueOf(newValue.getPrecio()));
				paradasSelect.clear();
				List<Long> idParadas = newValue.getIdParadas();
				for (Long id : idParadas) {
					Parada parada = paradaService.find(id);
					paradasSelect.add(parada);
				}

				paradaTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		});

		listParadas.setItems(paradasSelect);

		paradaTable.setRowFactory(tv -> {
			TableRow<Parada> fila = new TableRow();
			fila.setOnMousePressed(event -> {

				if (!fila.isEmpty() && event.getClickCount() == 1) {
					Parada parada = fila.getItem();
					if (paradasSelect.contains(parada)) {
						paradasSelect.remove(parada);
					} else {
						paradasSelect.add(parada);
					}
				}
			});
			return fila;
		});
		setColumnProperties();

		// Add all users into table
		loadParadaDetails();
		loadServicioDetails();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {
		colParadaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNombreParada.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
		colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));

		colIdServicio.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNombreServicio.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		colIdParadas.setCellValueFactory(new PropertyValueFactory<>("idParadas"));
	}

	private void loadParadaDetails() {
		paradaList.clear();
		paradaList.addAll(paradaService.findAll());

		paradaTable.setItems(paradaList);
	}

	private void loadServicioDetails() {
		servicioList.clear();
		servicioList.addAll(db4oService.findAllServicio());

		serviciosTable.setItems(servicioList);
	}

	Callback<TableColumn<Parada, Boolean>, TableCell<Parada, Boolean>> cellFactory = new Callback<TableColumn<Parada, Boolean>, TableCell<Parada, Boolean>>() {
		@Override
		public TableCell<Parada, Boolean> call(final TableColumn<Parada, Boolean> param) {
			final TableCell<Parada, Boolean> cell = new TableCell<Parada, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							Parada parada = getTableView().getItems().get(getIndex());
							updateUser(parada);
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateUser(Parada user) {
					userId.setText(Long.toString(user.getId()));
					nombreParada.setText(user.getNombre());
				}
			};
			return cell;
		}
	};

	/*
	 * Validations
	 */
	private boolean valida(String campo, String valor, String patron) {
		if (!valor.isEmpty()) {
			Pattern p = Pattern.compile(patron);
			Matcher m = p.matcher(valor);
			if (m.find() && m.group().equals(valor)) {
				return true;
			} else {
				validacionAlerta(campo, false);
				return false;
			}
		} else {
			validacionAlerta(campo, true);
			return false;
		}
	}

	private boolean validacionVacia(String campo, boolean vacio) {
		if (!vacio) {
			return true;
		} else {
			validacionAlerta(campo, true);
			return false;
		}
	}

	private void validacionAlerta(String campo, boolean vacio) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error de Validación");
		alert.setHeaderText(null);
		if (vacio)
			alert.setContentText("Porfavor rellena el campo: " + campo);
		else
			alert.setContentText("Porfavor introduzca un valor valido en: " + campo);

		alert.showAndWait();
	}

	public void ayudaF1(KeyEvent event) {
		if (event.getCode().toString().equals("F1")) {
			ayuda();
		}
	}

	@FXML
	private void ayuda() {
		try {
			WebView webView = new WebView();

			String url = getClass().getResource("/help/html/Administrador.html").toExternalForm();
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

	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	@FXML
	private void registrarUsuario(ActionEvent event) {

		if (valida("Nombre Parada", getNombreParada(), "[a-zA-Z]+") && !regionParada.getText().isEmpty()) {

			if (userId.getText() == null || userId.getText() == "") {
				if (valida("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
						&& validacionVacia("Password", getPassword().isEmpty())
						&& validacionVacia("PasswordConf", getPasswordConf().isEmpty())
						&& validacionVacia("Usuario Responsable", getUsuarioResponsable().isEmpty())
						&& validacionVacia("Nombre Responsable", getNombreResponsable().isEmpty())) {

					Usuario user = new Usuario();
					user.setNombre(getUsuarioResponsable());
					user.setEmail(getEmail());
					user.setPassword(getPassword());
					user.setPerfil(Perfil.PARADA);

					Parada parada = new Parada();
					parada.setNombre(getNombreParada());
					parada.setRegion(getRegionParada());
					parada.setResponsable(getNombreResponsable());

					if (paradaService.existsByNombre(parada.getNombre())
							&& paradaService.existsByRegion(parada.getRegion())) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Parada ya existe.");
						alert.setContentText("La parada que intentas introducir ya existe");
						alert.showAndWait();
						return;
					}

					Usuario newUser = userService.save(user);
					parada.setUsuario(user);

					@SuppressWarnings("unused")
					Parada nuevaParada = paradaService.save(parada);

					guardarAlerta(newUser);
				}

			} else {
				Usuario user = userService.find(Long.parseLong(userId.getText()));
				user.setNombre(getNombreParada());
				Usuario updatedUser = userService.update(user);
			}
			limpiarCamposParada();
		}

	}

	@FXML
	private void iniciarRegistroServicio() {
		txtPrecio.setDisable(false);
		txtServicio.setDisable(false);
		listParadas.setDisable(false);
		reinicioServicios.setDisable(false);
		confirmarServicios.setDisable(false);
		terminaServicios.setVisible(true);

	}

	@FXML
	private void terminarRegistroServicio() {
		txtPrecio.setDisable(true);
		txtServicio.setDisable(true);
		listParadas.setDisable(true);
		reinicioServicios.setDisable(true);
		confirmarServicios.setDisable(true);
		terminaServicios.setVisible(false);
		db4oService.cerrarBase();

	}

	@FXML
	private void registrarServicio() {
		if (valida("Nombre Servicio", getTxtServicio(), "^[a-zA-Z\\s]+$") && txtServicio != null
				&& valida("Precio servicio", getTxtPrecio(), "^(0|[1-9]\\d*)([.,]\\d{2})?$") && txtPrecio != null
				&& listParadas != null) {

			if (db4oService.findByServicioNombre(getTxtServicio().toUpperCase()) != null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error al guardar servicio.");
				alert.setHeaderText(null);
				alert.setContentText("Servicio ya existe");
				alert.showAndWait();
			} else {

				Servicio servicio = new Servicio();

				List<Parada> paradas = listParadas.getItems();

				List<Long> ids = new ArrayList<Long>();
				for (Parada parada : paradas) {
					ids.add(parada.getId());
				}

				servicio.setId(db4oService.findServicioLastId());
				servicio.setNombre(getTxtServicio().toUpperCase());
				servicio.setPrecio(Double.parseDouble(getTxtPrecio()));
				servicio.setIdParadas(ids);

				db4oService.saveServicio(servicio);

				guardarAlertaServicio(servicio);

				limpiarCamposServicio();

				loadServicioDetails();
			}
		}
	}

	@FXML
	private void actualizarServicio() {
		Servicio servicioActualizar = serviciosTable.getSelectionModel().getSelectedItem();

		if (servicioActualizar == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Atención");
			alert.setHeaderText(null);
			alert.setContentText("Por favor, seleccione un servicio para actualizar.");
			alert.showAndWait();
			return;
		}
		if (valida("Nombre Servicio", getTxtServicio(), "^[a-zA-Z\\s]+$") && txtServicio != null
				&& valida("Precio servicio", getTxtPrecio(), "^(0|[1-9]\\d*)([.,]\\d{2})?$") && txtPrecio != null
				&& listParadas != null) {

			servicioActualizar.setNombre(getTxtServicio().toUpperCase());
			servicioActualizar.setPrecio(Double.valueOf(getTxtPrecio()));

			List<Parada> paradas = listParadas.getItems();
			List<Long> ids = new ArrayList<>();
			for (Parada parada : paradas) {
				ids.add(parada.getId());
			}
			servicioActualizar.setIdParadas(ids);

			db4oService.updateServicio(servicioActualizar);
			actualizarAlerta(servicioActualizar);
		}
		loadServicioDetails();
	}

	@FXML
	private void limpiarCamposParada() {
		userId.setText(null);
		regionParada.clear();
		usuarioResponsable.clear();
		nombreResponsable.clear();
		nombreParada.clear();
		email.clear();
		password.clear();
		passwordConf.clear();
	}

	@FXML
	private void limpiarCamposServicio() {
		txtServicio.clear();
		txtPrecio.clear();
		paradasSelect.clear();

	}

	private void guardarAlerta(Usuario user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usuario introducido correctamente.");
		alert.setHeaderText(null);
		alert.setContentText("Usuario: " + user.getNombre() + " " + " creado correctamente ");
		alert.showAndWait();
	}

	private void guardarAlertaServicio(Servicio servicio) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Servicio introducido correctamente.");
		alert.setHeaderText(null);
		alert.setContentText("Servicio: " + servicio.getNombre() + " " + " creado correctamente ");
		alert.showAndWait();
	}

	private void actualizarAlerta(Servicio servicio) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Servicio actualizado correctamente.");
		alert.setHeaderText(null);
		alert.setContentText("Servicio" + servicio.getNombre() + " " + " actualizado correctamente.");
		alert.showAndWait();
	}

	public String getNombreParada() {
		return nombreParada.getText();
	}

	public char getRegionParada() {
		return regionParada.getText().charAt(0);
	}

	public void setRegionParada(TextField regionParada) {
		this.regionParada = regionParada;
	}

	public String getUsuarioResponsable() {
		return usuarioResponsable.getText();
	}

	public void setUsuarioResponsable(TextField usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}

	public String getNombreResponsable() {
		return nombreResponsable.getText();
	}

	public void setNombreResponsable(TextField nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	public String getPasswordConf() {
		return passwordConf.getText();
	}

	public void setPasswordConf(PasswordField passwordConf) {
		this.passwordConf = passwordConf;
	}

	public void setNombreParada(TextField nombreParada) {
		this.nombreParada = nombreParada;
	}

	public void setEmail(TextField email) {
		this.email = email;
	}

	public void setPassword(PasswordField password) {
		this.password = password;
	}

	public String getEmail() {
		return email.getText();
	}

	public String getPassword() {
		return password.getText();
	}

	public String getTxtServicio() {
		return txtServicio.getText();
	}

	public void setTxtServicio(TextField txtServicio) {
		this.txtServicio = txtServicio;
	}

	public String getTxtPrecio() {
		return txtPrecio.getText();
	}

	public void setTxtPrecio(TextField txtPrecio) {
		this.txtPrecio = txtPrecio;
	}

}
