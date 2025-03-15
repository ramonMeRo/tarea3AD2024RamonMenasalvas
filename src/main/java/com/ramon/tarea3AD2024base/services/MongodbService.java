package com.ramon.tarea3AD2024base.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ramon.tarea3AD2024base.modelo.Carnet;
import com.ramon.tarea3AD2024base.modelo.Estancia;
import com.ramon.tarea3AD2024base.modelo.Peregrino;
import com.ramon.tarea3AD2024base.modelo.Visita;
import com.ramon.tarea3AD2024base.repositorios.PeregrinoRepository;

@Service
public class MongodbService {
	@Autowired
	private PeregrinoRepository peregrinoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public void generarBackupCarnets() {
		List<Peregrino> peregrinos = peregrinoRepository.findAll();

		List<Map<String, Object>> carnets = new ArrayList<>();

		for (Peregrino p : peregrinos) {
			Carnet carnet = p.getCarnet();
			Map<String, Object> carnetMap = new HashMap<>();
			carnetMap.put("id", carnet.getId());
			carnetMap.put("fechaExp", carnet.getFechaExp().toString());
			carnetMap.put("distancia", carnet.getDistancia());
			carnetMap.put("nVips", carnet.getnVips());

			Map<String, Object> peregrinoMap = new HashMap<>();
			peregrinoMap.put("nombre", p.getNombre());
			peregrinoMap.put("apellidos", p.getApellidos());
			peregrinoMap.put("nacionalidad", p.getNacionalidad());

			carnetMap.put("peregrino", peregrinoMap);
			carnetMap.put("expedidoEn", carnet.getParadaInicial().getNombre());

			List<Map<String, Object>> paradasList = new ArrayList<>();
			for (Visita visita : p.getParadasVisitadas()) {
				Map<String, Object> paradaMap = new HashMap<>();
				paradaMap.put("nombre", visita.getParada().getNombre());
				paradaMap.put("region", visita.getParada().getRegion());
				paradasList.add(paradaMap);
			}
			carnetMap.put("paradasVisitadas", paradasList);

			List<Map<String, Object>> estanciasList = new ArrayList<>();
			for (Estancia estancia : p.getListaEstancias()) {
				Map<String, Object> estanciaMap = new HashMap<>();
				estanciaMap.put("id", estancia.getId());
				estanciaMap.put("fecha", estancia.getFecha().toString());
				estanciaMap.put("parada", estancia.getParada().getNombre());
				estanciaMap.put("vip", estancia.isVip());
				estanciasList.add(estanciaMap);
			}
			carnetMap.put("estancias", estanciasList);

			carnets.add(carnetMap);
		}

		String nombreBackup = "backupcarnets_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));

		Map<String, Object> backup = new HashMap<>();
		backup.put("nombre", nombreBackup);
		backup.put("fecha", LocalDateTime.now().toString());
		backup.put("carnets", carnets);

		mongoTemplate.save(backup, "backup_carnets");
	}
}
