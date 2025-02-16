package com.ramon.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Perfil;
import com.ramon.tarea3AD2024base.modelo.Usuario;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
	private TableView<Usuario> userTable;

	@FXML
	private TableColumn<Usuario, Long> colUserId;

	@FXML
	private TableColumn<Usuario, String> colFirstName;

	@FXML
	private TableColumn<Usuario, LocalDate> colDOB;

	@FXML
	private TableColumn<Usuario, String> colGender;

	@FXML
	private TableColumn<Usuario, Perfil> colRole;

	@FXML
	private TableColumn<Usuario, String> colEmail;

	@FXML
	private MenuItem borrarUsuario;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private UsuarioService userService;
	@Autowired
	private ParadaService paradaService;

	private ObservableList<Usuario> userList = FXCollections.observableArrayList();

	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	@FXML
	void reset(ActionEvent event) {
		limpiarCampos();
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
				actualizarAlerta(updatedUser);
			}

			limpiarCampos();
			loadUserDetails();
		}

	}

	private void limpiarCampos() {
		userId.setText(null);
		regionParada.clear();
		usuarioResponsable.clear();
		nombreResponsable.clear();
		nombreParada.clear();
		email.clear();
		password.clear();
		passwordConf.clear();
	}

	private void guardarAlerta(Usuario user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usuario introducido correctamente.");
		alert.setHeaderText(null);
		alert.setContentText("Usuario: " + user.getNombre() + " " + " creado correctamente ");
		alert.showAndWait();
	}

	private void actualizarAlerta(Usuario user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		alert.setHeaderText(null);
		alert.setContentText("Usuario " + user.getNombre() + " " + " actualizado correctamente.");
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		// Add all users into table
		loadUserDetails();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {
		/*
		 * Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new
		 * StringConverter<LocalDate>() { String pattern = "dd/MM/yyyy";
		 * DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		 * 
		 * @Override public String toString(LocalDate date) { if (date != null) { return
		 * dateFormatter.format(date); } else { return ""; } }
		 * 
		 * @Override public LocalDate fromString(String string) { if (string != null &&
		 * !string.isEmpty()) { return LocalDate.parse(string, dateFormatter); } else {
		 * return null; } } }));
		 */

		colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("perfil"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
	}

	Callback<TableColumn<Usuario, Boolean>, TableCell<Usuario, Boolean>> cellFactory = new Callback<TableColumn<Usuario, Boolean>, TableCell<Usuario, Boolean>>() {
		@Override
		public TableCell<Usuario, Boolean> call(final TableColumn<Usuario, Boolean> param) {
			final TableCell<Usuario, Boolean> cell = new TableCell<Usuario, Boolean>() {
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
							Usuario user = getTableView().getItems().get(getIndex());
							updateUser(user);
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

				private void updateUser(Usuario user) {
					userId.setText(Long.toString(user.getId()));
					nombreParada.setText(user.getNombre());
				}
			};
			return cell;
		}
	};

	/*
	 * Add All users to observable list and update table
	 */
	private void loadUserDetails() {
		userList.clear();
		userList.addAll(userService.findAll());

		userTable.setItems(userList);
	}

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
}
