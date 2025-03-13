package com.ramon.tarea3AD2024base.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Ramon
 * @since 01-01-2025
 */

@Controller
public class AdministradorController implements Initializable {

	@FXML
	private Button btnSalir;

	@FXML
	public Label userId;

	@FXML
	public TextField nombreParada;

	@FXML
	public TextField regionParada;

	@FXML
	public TextField usuarioResponsable;

	@FXML
	public TextField nombreResponsable;

	@FXML
	public TextField email;

	@FXML
	public PasswordField password;

	@FXML
	public PasswordField passwordConf;

	@FXML
	private Button reinicio;

	@FXML
	private Button guardarusuario;

	@FXML
	private Button reinicioServicios;

	@FXML
	private Button confirmarServicios;

	@FXML
	private TableView<Parada> userTable;

	@FXML
	private TableColumn<Parada, Long> colParadaId;

	@FXML
	private TableColumn<Parada, Character> colRegion;

	@FXML
	private TableColumn<Parada, String> colNombreParada;

	@FXML
	private TableColumn<Parada, String> colResponsable;

	@FXML
	private MenuItem borrarUsuario;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	public UsuarioService userService;
	@Autowired
	public ParadaService paradaService;

	private ObservableList<Parada> paradaList = FXCollections.observableArrayList();

	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	@FXML
	void reset(ActionEvent event) {
		limpiarCampos();
	}

	@FXML
	public void registrarUsuario(ActionEvent event) {

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
		colParadaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colNombreParada.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		colRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
		colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
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
	 * Add All users to observable list and update table
	 */
	private void loadUserDetails() {
		paradaList.clear();
		paradaList.addAll(paradaService.findAll());

		userTable.setItems(paradaList);
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

	public void generarInforme() {
		Connection conexion = null;
		try {
			InputStream logo = getClass().getResourceAsStream("/img/Logo.jpg");
			URL url = getClass().getResource("/reportTemplate/EstanciasPorParada.jasper");
			if (url == null) {
				System.err.println("No se encontró el archivo EstanciasPorParada.jasper");
				return;
			}
			JasperReport reporte = (JasperReport) JRLoader.loadObject(url);

			Map<String, Object> parametros = new HashMap<>();
			parametros.put("Logo", logo);

			DataSource ds = getDataSource();
			conexion = ds.getConnection();

			JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexion);

			String rutaSalida = "src/main/resources/reports/EstanciasPorParada.pdf";
			JasperExportManager.exportReportToPdfFile(print, rutaSalida);
			System.out.println("Informe generado correctamente en: " + rutaSalida);

			abrirPDF(rutaSalida);

		} catch (JRException | SQLException e) {
			e.printStackTrace();
			System.err.println("Error generando el informe de EstanciasPorParada: ");
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.err.println("Error cerrando la conexión: " + e.getMessage());
				}
			}
		}
	}

	public void abrirPDF(String rutaSalida) {
		File archivoPDF = new File(rutaSalida);
		if (!archivoPDF.exists()) {
			System.err.println("El archivo no existe: " + rutaSalida);
			return;
		}
		try {
			Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start", "", archivoPDF.getAbsolutePath() });
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al abrir el archivo PDF: " + e.getMessage());
		}
	}

	private DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:mysql://localhost:3306/bdtarea3adramonmenasalvas?useSSL=false");
		ds.setUsername("root");
		ds.setPassword("");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return ds;
	}

}
