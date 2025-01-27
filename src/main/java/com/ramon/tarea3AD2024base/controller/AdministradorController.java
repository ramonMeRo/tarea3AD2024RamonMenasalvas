package com.ramon.tarea3AD2024base.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.ramon.tarea3AD2024base.Utils.VistaUtils;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
	private void salir() {
		VistaUtils.Salir();
	}

	/**
	 * Logout and go to the login page
	 */
	@FXML
	private void logout(ActionEvent event) throws IOException {
		stageManager.switchScene(FxmlView.INICIO);
	}

	@FXML
	void reset(ActionEvent event) {
		limpiarCampos();
	}

	@FXML
	private void registrarUsuario(ActionEvent event) {

		if (validate("Nombre Parada", getNombreParada(), "[a-zA-Z]+") 
				&& !regionParada.getText().isEmpty())
		{

			if (userId.getText() == null || userId.getText() == "") {
				if (validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
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
					

					Usuario newUser = userService.save(user);
					parada.setUsuario(user);
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

	@FXML
	private void borrarUsuario(ActionEvent event) {
		List<Usuario> users = userTable.getSelectionModel().getSelectedItems();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK)
			userService.deleteInBatch(users);

		loadUserDetails();
	}

	private void limpiarCampos() {
		userId.setText(null);
		nombreParada.clear();
		email.clear();
		password.clear();
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
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
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
	private boolean validate(String field, String value, String pattern) {
		if (!value.isEmpty()) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(value);
			if (m.find() && m.group().equals(value)) {
				return true;
			} else {
				validacionAlerta(field, false);
				return false;
			}
		} else {
			validacionAlerta(field, true);
			return false;
		}
	}

	private boolean validacionVacia(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validacionAlerta(field, true);
			return false;
		}
	}

	private void validacionAlerta(String field, boolean empty) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error de Validación");
		alert.setHeaderText(null);
			if (empty)
				alert.setContentText("Porfavor rellena el campo: " + field);
			else
				alert.setContentText("Porfavor introduzca un valor valido en: " + field);
		
		alert.showAndWait();
	}
}
