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
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Usuario;
import com.ramon.tarea3AD2024base.services.EstanciaService;
import com.ramon.tarea3AD2024base.services.PeregrinoService;
import com.ramon.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
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

/**
 * Controlador de la ventana del Peregrino que tiene funciones para la
 * exportación de su carnet y la modificación de sus datos.
 * 
 * @author Ramon
 * @since 01-01-2025
 */

@Controller
public class PeregrinoController implements Initializable {

	@FXML
	public TextField nombre;
	@FXML
	public TextField apellidos;
	@FXML
	public TextField usuario;
	@FXML
	public TextField email;
	@FXML
	public DatePicker fechaNac;
	@FXML
	public ComboBox<String> nacionalidad;
	@FXML
	public Button actualizar;
	@FXML
	public Button reiniciar;

	@FXML
	private TableView<Estancia> estancias;
	@FXML
	private TableColumn<Estancia, Long> idEstancia;
	@FXML
	private TableColumn<Estancia, LocalDate> fecha;
	@FXML
	private TableColumn<Estancia, Boolean> vip;

	private ObservableList<Estancia> estanciaList = FXCollections.observableArrayList();

	@Lazy
	@Autowired
	private StageManager stagemanager;
	@Autowired
	public PeregrinoService peregrinoService;
	@Autowired
	private EstanciaService estanciaService;

	public Usuario usuarioSesion;

	public Peregrino peregrino;

	/**
	 * Muestra una alerta de confirmación para permitir la edición de los datos del
	 * peregrino. Si el usuario confirma la acción, los campos se habilitan y se
	 * resaltan visualmente. Si el usuario cancela, los datos se restauran a su
	 * estado original y los campos permanecen deshabilitados.
	 */
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

	/**
	 * Restaura los datos del peregrino a su estado original antes de la edición.
	 */
	@FXML
	private void reiniciar() {
		nombre.setText(peregrino.getNombre());
		apellidos.setText(peregrino.getApellidos());
		usuario.setText(usuarioSesion.getNombre());
		email.setText(usuarioSesion.getEmail());
		fechaNac.setValue(peregrino.getFechaNac());
		nacionalidad.setValue(peregrino.getNacionalidad());
	}

	/**
	 * Valida y actualiza los datos del peregrino si cumplen con los requisitos.
	 * Muestra una alerta informando del éxito de la actualización.
	 */
	@FXML
	public void actualizarPeregrino() {
		if (valida("Nombre", nombre.getText(), "^[a-zA-Z\\s]+$")
				&& valida("Apellidos", apellidos.getText(), "^[a-zA-Z\\s]+$")
				&& getFechaNac().getValue().isBefore(LocalDate.now())) {
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

	/**
	 * Exporta el carnet del peregrino actual.
	 * 
	 * Este método muestra una alerta de confirmación preguntando si el usuario
	 * desea exportar su carnet. Si el usuario acepta, se llama al método
	 * {@code VistaUtils.ExportarCarnet(peregrino)} para exportar el carnet y luego
	 * se genera un informe con {@code generarInforme()}.
	 * 
	 * Si el usuario cancela la exportación, se muestra un mensaje de cancelación.
	 */
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

	/**
	 * Redirige a la pantalla de inicio.
	 */
	@FXML
	private void volver() {
		stagemanager.switchScene(FxmlView.INICIO);
	}

	/**
	 * Llena el comboBox de nacionalidades con la lista de países extraída del
	 * archivo XML.
	 * 
	 * Este método obtiene la lista de países a través del método
	 * {@code leerNaciones()} y la añade al ComboBox {@code nacionalidad}, limpiando
	 * previamente los datos existentes.
	 */
	private void llenarNaciones() {
		List<String> naciones = leerNaciones();
		nacionalidad.getItems().clear();
		nacionalidad.getItems().addAll(naciones);
	}

	/**
	 * Lee un archivo XML que contiene una lista de países y devuelve sus nombres en
	 * una lista.
	 * 
	 * El archivo XML se encuentra en la ruta
	 * {@code src/main/resources/readOnly/paises.xml}. Se utiliza un
	 * {@code DocumentBuilder} para parsear el XML y extraer los nombres de los
	 * países.
	 *
	 * @return Lista de nombres de países extraídos del archivo XML.
	 */
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

	/**
	 * Valida si un valor dado cumple con un patrón especificado.
	 *
	 * Se utiliza una expresión regular para validar el formato del valor. Si el
	 * valor no es válido, se muestra una alerta indicando el problema.
	 *
	 * @param campo  El nombre del campo que se está validando.
	 * @param valor  El valor ingresado que debe ser validado.
	 * @param patron La expresión regular que define el formato válido del campo.
	 * @return true si el valor cumple con el patrón, false en caso contrario.
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
	 * Muestra la ventana de ayuda con el manual de usuario.
	 */
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

	/**
	 * Muestra una alerta de validación para indicar que un campo está vacío o tiene
	 * un valor inválido.
	 * 
	 * Si el campo está vacío, se muestra un mensaje indicando que debe completarse.
	 * Si el campo tiene un valor incorrecto, se muestra un mensaje solicitando un
	 * valor válido.
	 *
	 * @param campo El nombre del campo que se está validando.
	 * @param vacio true si el campo está vacío, false si tiene un valor incorrecto.
	 */
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
	 * Inicializa el controlador y carga los datos del peregrino en la interfaz de
	 * usuario.
	 * 
	 * Se recupera la sesión del usuario desde el controlador de inicio, se cargan
	 * los datos del peregrino asociado y se rellenan los campos de la interfaz con
	 * la información obtenida. También se configuran las columnas de la tabla de
	 * estancias y se cargan los detalles de estancias del peregrino.
	 *
	 * @param location  La ubicación utilizada para resolver rutas relativas de
	 *                  objetos raíz.
	 * @param resources Los recursos utilizados para localizar el objeto raíz.
	 */

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

		setColumnProperties();

		loadEstanciaDetails();

	}

	/**
	 * Configura las propiedades de las columnas en la tabla de estancias.
	 * 
	 * Se establecen los valores de las columnas para que muestren la información
	 * correspondiente de cada estancia, incluyendo el identificador, la fecha y el
	 * estado VIP.
	 */
	private void setColumnProperties() {
		idEstancia.setCellValueFactory(new PropertyValueFactory<>("id"));
		fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		vip.setCellValueFactory(new PropertyValueFactory<>("vip"));
	}

	/**
	 * Carga y muestra los detalles de las estancias del peregrino en la tabla de la
	 * interfaz.
	 *
	 * Se limpia la lista de estancias, se obtienen todas las estancias asociadas al
	 * peregrino actual y se añaden a la tabla de la interfaz gráfica.
	 */
	private void loadEstanciaDetails() {
		estanciaList.clear();
		estanciaList.addAll(estanciaService.findByPeregrino(peregrino));

		estancias.setItems(estanciaList);
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
	 * Genera un informe en formato PDF para un carnet de peregrino utilizando
	 * JasperReports.
	 * 
	 * Se obtiene la plantilla del reporte, se establecen los parámetros necesarios
	 * y se genera un archivo PDF con la información del peregrino. Luego, se
	 * intenta abrir el archivo generado.
	 * 
	 * @throws JRException  Si ocurre un error durante la generación del reporte.
	 * @throws SQLException Si hay un problema al conectar con la base de datos.
	 */
	public void generarInforme() {
		Connection conexion = null;
		try {
			InputStream logo = getClass().getResourceAsStream("/img/Logo.jpg");

			URL url = getClass().getResource("/reportTemplate/Carnet.jasper");
			if (url == null) {
				return;
			}
			JasperReport reporte = (JasperReport) JRLoader.loadObject(url);

			Long idPeregrino = peregrino.getId();

			Map<String, Object> parametros = new HashMap<>();
			parametros.put("id", idPeregrino);
			parametros.put("Logo", logo);

			DataSource ds = getDataSource();
			conexion = ds.getConnection();

			JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexion);

			String rutaSalida = "src/main/resources/carnets/" + peregrino.getNombre().replace(" ", "")
					+ peregrino.getApellidos().replace(" ", "") + "_peregrino.pdf";

			JasperExportManager.exportReportToPdfFile(print, rutaSalida);

			abrirPDF(rutaSalida);

		} catch (JRException | SQLException e) {
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	/**
	 * Abre un archivo PDF utilizando el visor de PDF predeterminado del sistema
	 * operativo.
	 * 
	 * Si el archivo no existe, se muestra un mensaje de error en la consola.
	 *
	 * @param rutaSalida La ruta del archivo PDF que se desea abrir.
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
			System.err.println("Error al abrir el archivo PDF ");
		}
	}

	/**
	 * Obtiene una conexión con la base de datos MySQL.
	 * 
	 * Se configura un `DriverManagerDataSource` con las credenciales y la URL de la
	 * base de datos.
	 *
	 * @return Un objeto `DataSource` configurado para la conexión con la base de
	 *         datos.
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
	 * Obtiene el DatePicker que representa la fecha de nacimiento del peregrino.
	 *
	 * @return Un objeto {@link DatePicker} con la fecha de nacimiento seleccionada.
	 */
	public DatePicker getFechaNac() {
		return fechaNac;
	}

	/**
	 * Establece el DatePicker con la fecha de nacimiento del peregrino.
	 *
	 * @param fechaNac Un objeto {@link DatePicker} que representa la fecha de
	 *                 nacimiento.
	 */
	public void setFechaNac(DatePicker fechaNac) {
		this.fechaNac = fechaNac;
	}

}
