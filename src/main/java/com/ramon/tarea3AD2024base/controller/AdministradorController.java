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
 * Controlador de la ventana del administrador que tiene funciones para la
 * administración de paradas y usuarios Permite la gestión de paradas, registro
 * de usuarios y generación de informes
 * 
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

	/**
	 * Redirige a la pantalla de inicio.
	 */
	@FXML
	private void volver() {
		stageManager.switchScene(FxmlView.INICIO);
	}

	/**
	 * Limpia los campos de entrada en el formulario.
	 */
	@FXML
	void reset(ActionEvent event) {
		limpiarCampos();
	}

	/**
	 * Registra un nuevo usuario y su respectiva parada.
	 * 
	 * @param event Evento de acción.
	 */
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
			loadParadaDetails();
		}

	}

	/**
	 * Limpia los campos de entrada del formulario.
	 */
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

	/**
	 * Muestra un mensaje de alerta cuando un usuario se guarda correctamente.
	 * 
	 * @param user Usuario registrado
	 */
	private void guardarAlerta(Usuario user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usuario introducido correctamente.");
		alert.setHeaderText(null);
		alert.setContentText("Usuario: " + user.getNombre() + " " + " creado correctamente ");
		alert.showAndWait();
	}

	/**
	 * Muestra una alerta indicando que un usuario ha sido actualizado
	 * correctamente.
	 *
	 * @param user El usuario que ha sido actualizado.
	 */
	private void actualizarAlerta(Usuario user) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		alert.setHeaderText(null);
		alert.setContentText("Usuario " + user.getNombre() + " " + " actualizado correctamente.");
		alert.showAndWait();
	}

	/**
	 * Inicializa la vista, Cargando las tablas con datos.
	 * 
	 * @param location  URL de inicialización.
	 * @param resources Recursos para la inicialización.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		// Add all users into table
		loadParadaDetails();
	}

	/**
	 * Configura las propiedades de las columnas de la tabla.
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

	/**
	 * Carga los detalles de las paradas en la tabla.
	 */
	private void loadParadaDetails() {
		paradaList.clear();
		paradaList.addAll(paradaService.findAll());

		userTable.setItems(paradaList);
	}

	/**
	 * Valida los campos de entrada con expresiones regulares.
	 * 
	 * @param campo  Nombre del campo
	 * @param valor  Valor ingresado
	 * @param patron Expresión regular a validar
	 * @return true si es válido, false en caso contrario
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

	/**
	 * Genera un informe en formato PDF de las estancias por parada.
	 */
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

	/**
	 * Abre un archivo PDF generado previamente.
	 * 
	 * @param rutaSalida Ruta del archivo a abrir.
	 */
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

	/**
	 * Obtiene el origen de datos para la conexión con la base de datos.
	 * 
	 * @return DataSource Configuración de conexión a la BD.
	 */
	private DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:mysql://localhost:3306/bdtarea3adramonmenasalvas?useSSL=false");
		ds.setUsername("root");
		ds.setPassword("");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		return ds;
	}

	/**
	 * Obtiene el nombre de la parada ingresado en el campo de texto.
	 * 
	 * @return Nombre de la parada.
	 */
	public String getNombreParada() {
		return nombreParada.getText();
	}

	/**
	 * Obtiene la región de la parada ingresada en el campo de texto.
	 * 
	 * @return Primera letra de la región como un carácter.
	 */
	public char getRegionParada() {
		return regionParada.getText().charAt(0);
	}

	/**
	 * Establece el campo de texto de la región de la parada.
	 * 
	 * @param regionParada Campo de texto de la región de la parada.
	 */
	public void setRegionParada(TextField regionParada) {
		this.regionParada = regionParada;
	}

	/**
	 * Obtiene el usuario responsable ingresado en el campo de texto.
	 * 
	 * @return Nombre del usuario responsable.
	 */
	public String getUsuarioResponsable() {
		return usuarioResponsable.getText();
	}

	/**
	 * Establece el campo de texto del usuario responsable.
	 * 
	 * @param usuarioResponsable Campo de texto del usuario responsable.
	 */
	public void setUsuarioResponsable(TextField usuarioResponsable) {
		this.usuarioResponsable = usuarioResponsable;
	}

	/**
	 * Obtiene el nombre del responsable ingresado en el campo de texto.
	 * 
	 * @return Nombre del responsable.
	 */
	public String getNombreResponsable() {
		return nombreResponsable.getText();
	}

	/**
	 * Establece el campo de texto del nombre del responsable.
	 * 
	 * @param nombreResponsable Campo de texto del nombre del responsable.
	 */
	public void setNombreResponsable(TextField nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	/**
	 * Obtiene la confirmación de la contraseña ingresada en el campo de texto.
	 * 
	 * @return Confirmación de la contraseña.
	 */
	public String getPasswordConf() {
		return passwordConf.getText();
	}

	/**
	 * Establece el campo de texto de confirmación de contraseña.
	 * 
	 * @param passwordConf Campo de texto de confirmación de contraseña.
	 */
	public void setPasswordConf(PasswordField passwordConf) {
		this.passwordConf = passwordConf;
	}

	/**
	 * Establece el campo de texto del nombre de la parada.
	 * 
	 * @param nombreParada Campo de texto del nombre de la parada.
	 */
	public void setNombreParada(TextField nombreParada) {
		this.nombreParada = nombreParada;
	}

	/**
	 * Establece el campo de texto del correo electrónico.
	 * 
	 * @param email Campo de texto del correo electrónico.
	 */
	public void setEmail(TextField email) {
		this.email = email;
	}

	/**
	 * Establece el campo de texto de la contraseña.
	 * 
	 * @param password Campo de texto de la contraseña.
	 */
	public void setPassword(PasswordField password) {
		this.password = password;
	}

	/**
	 * Obtiene el correo electrónico ingresado en el campo de texto.
	 * 
	 * @return Correo electrónico.
	 */
	public String getEmail() {
		return email.getText();
	}

	/**
	 * Obtiene la contraseña ingresada en el campo de texto.
	 * 
	 * @return Contraseña.
	 */
	public String getPassword() {
		return password.getText();
	}

}
