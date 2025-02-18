package com.ramon.tarea3AD2024base.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ramon.tarea3AD2024base.Utils.VistaUtils;
import com.ramon.tarea3AD2024base.config.StageManager;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Parada;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class PeregrinoController implements Initializable {

	@FXML
	private TextField nombre;
	@FXML
	private TextField apellidos;
	@FXML
	private TextField usuario;
	@FXML
	private TextField email;
	@FXML
	private DatePicker fechaNac;
	@FXML
	private ComboBox<String> nacionalidad;
	@FXML
	private Button actualizar;
	@FXML
	private Button reiniciar;

	@FXML
	private TableView<Estancia> estancias;
	@FXML
	private TableColumn<Estancia, Long> idEstancia;
	@FXML
	private TableColumn<Parada, String> nombreParada;
	@FXML
	private TableColumn<Parada, Character> regionParada;
	@FXML
	private TableColumn<Estancia, LocalDate> fecha;
	@FXML
	private TableColumn<Estancia, Boolean> vip;

	@Lazy
	@Autowired
	private StageManager stagemanager;
	@Autowired
	private PeregrinoService peregrinoService;

	private Usuario usuarioSesion;

	private Peregrino peregrino;

	@FXML
	private void activarEdicion() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Editar datos");
		alert.setHeaderText(null);
		alert.setContentText("¿Quiere editar sus datos?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			nombre.setEditable(true);
			apellidos.setEditable(true);
			fechaNac.setDisable(false);
			nacionalidad.setDisable(false);
			actualizar.setDisable(false);
			reiniciar.setDisable(false);

			nombre.setStyle("-fx-border-color:  #edc82f; -fx-border-width: 2px;");
			apellidos.setStyle("-fx-border-color:  #edc82f; -fx-border-width: 2px;");
			fechaNac.setStyle("-fx-border-color:  #edc82f; -fx-border-width: 2px;");
			nacionalidad.setStyle("-fx-border-color:  #edc82f; -fx-border-width: 2px;");
		}

		if (action.get() == ButtonType.CANCEL) {

			nombre.setText(peregrino.getNombre());
			apellidos.setText(peregrino.getApellidos());
			usuario.setText(usuarioSesion.getNombre());
			email.setText(usuarioSesion.getEmail());
			fechaNac.setValue(peregrino.getFechaNac());
			nacionalidad.setValue(peregrino.getNacionalidad());

			nombre.setEditable(false);
			apellidos.setEditable(false);
			fechaNac.setDisable(true);
			nacionalidad.setDisable(true);
			actualizar.setDisable(true);
			reiniciar.setDisable(true);

			nombre.setStyle("");
			apellidos.setStyle("");
			fechaNac.setStyle("");
			nacionalidad.setStyle("");
		}
	}

	@FXML
	private void reiniciar() {
		nombre.setText(peregrino.getNombre());
		apellidos.setText(peregrino.getApellidos());
		usuario.setText(usuarioSesion.getNombre());
		email.setText(usuarioSesion.getEmail());
		fechaNac.setValue(peregrino.getFechaNac());
		nacionalidad.setValue(peregrino.getNacionalidad());
	}

	@FXML
	private void actualizarPeregrino() {
		if (valida("Nombre", nombre.getText(), "^[a-zA-Z\\s]+$")
				&& valida("Apellidos", apellidos.getText(), "^[a-zA-Z\\s]+$")) {
			peregrino.setNombre(nombre.getText());
			peregrino.setApellido(apellidos.getText());
			peregrino.setFechaNac(fechaNac.getValue());
			peregrino.setNacionalidad(nacionalidad.getValue());

			peregrinoService.update(peregrino);

			Alert alertExport = new Alert(AlertType.INFORMATION);
			alertExport.setTitle("Datos actualizados correctamente.");
			alertExport.setHeaderText(null);
			alertExport.setContentText("Usuario: " + peregrino.getNombre() + " " + peregrino.getApellidos()
					+ " actualizado correctamente ");
			alertExport.showAndWait();
		}
	}

	@FXML
	private void exportarCarnetPeregrino() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exportar carnet");
		alert.setHeaderText("¿Seguro que quiere importar su carnet?");
		alert.setContentText(peregrino.getCarnet().toString());
		Optional<ButtonType> action = alert.showAndWait();
		if (action.get() == ButtonType.OK) {
			VistaUtils.ExportarCarnet(peregrino);
			generarInforme();
			Alert alertExport = new Alert(AlertType.INFORMATION);
			alertExport.setTitle("Carnet importado correctamente.");
			alertExport.setHeaderText(null);
			alertExport.setContentText(
					"Usuario: " + peregrino.getNombre() + " " + peregrino.getApellidos() + " creado correctamente ");
			alertExport.showAndWait();
		} else {
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exportar carnet");
			alert.setHeaderText("Ha cancelado la exportacion");
			alert.setContentText("No se ha exportado su carnet");
			alert.showAndWait();
		}

	}

	@FXML
	private void volver() {
		stagemanager.switchScene(FxmlView.INICIO);
	}

	private void llenarNaciones() {
		List<String> naciones = leerNaciones();
		nacionalidad.getItems().clear();
		nacionalidad.getItems().addAll(naciones);
	}

	private List<String> leerNaciones() {
		File naciones = new File("src/main/resources/readOnly/paises.xml");
		List<String> listNaciones = new ArrayList<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder constructorDocumento = dbf.newDocumentBuilder();
			Document documento = constructorDocumento.parse(naciones);

			NodeList listaPais = documento.getElementsByTagName("pais");
			@SuppressWarnings("unused")
			Element pais, id, nombre;

			int indicePais = 0;

			while (indicePais < listaPais.getLength()) {
				pais = (Element) listaPais.item(indicePais);

				id = (Element) pais.getElementsByTagName("id").item(0);
				String nombreNacion = pais.getElementsByTagName("nombre").item(0).getTextContent();

				listNaciones.add(nombreNacion);

				indicePais++;
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listNaciones;
	}

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

	@FXML
	private void ayuda() {
		try {
			WebView webView = new WebView();

			String url = getClass().getResource("/help/html/Peregrino.html").toExternalForm();
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

	@SuppressWarnings("unused")
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		usuarioSesion = InicioController.sesion.getUsuario();

		peregrino = peregrinoService.findByUsuario(usuarioSesion);

		nombre.setText(peregrino.getNombre());
		apellidos.setText(peregrino.getApellidos());
		usuario.setText(usuarioSesion.getNombre());
		email.setText(usuarioSesion.getEmail());
		fechaNac.setValue(peregrino.getFechaNac());
		nacionalidad.setValue(peregrino.getNacionalidad());

		llenarNaciones();

	}

	public void ayudaF1(KeyEvent event) {
		if (event.getCode().toString().equals("F1")) {
			ayuda();
		}
	}

	public void generarInforme() {
		Connection conexion = null;
		try {
			InputStream logo = getClass().getResourceAsStream("/img/Logo.jpg");

			URL url = getClass().getResource("/reportTemplate/Carnet.jasper");
			if (url == null) {
				System.err.println("No se encontró el archivo Carnet.jasper");
				return;
			}
			JasperReport reporte = (JasperReport) JRLoader.loadObject(url);

			Long idPeregrino = peregrino.getId();
			System.out.println("Valor de PEREGRINO_ID: " + idPeregrino);

			Map<String, Object> parametros = new HashMap<>();
			parametros.put("id", idPeregrino);
			parametros.put("Logo", logo);

			DataSource ds = getDataSource();
			conexion = ds.getConnection();

			JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexion);

			String rutaSalida = "src/main/resources/carnets/" + peregrino.getNombre().replace(" ", "")
					+ peregrino.getApellidos().replace(" ", "") + "_peregrino.pdf";

			JasperExportManager.exportReportToPdfFile(print, rutaSalida);

			System.out.println("Informe generado correctamente en: " + rutaSalida);

			abrirPDF(rutaSalida);

		} catch (JRException | SQLException e) {
			System.out.println("Error");
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					System.out.println("Error");
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
			System.err.println("Error al abrir el archivo PDF ");
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
