package com.miaplicacion.CRUDMIXTO.migration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaplicacion.CRUDMIXTO.entity.Empleado;
import com.miaplicacion.CRUDMIXTO.repository.EmpleadoRepository;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Component
public class CsvJsonImporter {

    private final EmpleadoRepository empleadoRepository;

    public CsvJsonImporter(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void importarEmpleados() {
        try {
            importarDesdeCsv("src/main/resources/data/empleados.csv");
            importarDesdeJson("src/main/resources/data/empleados.json");
        } catch (Exception e) {
            throw new RuntimeException("Error al importar empleados: " + e.getMessage(), e);
        }
    }

    private void importarDesdeCsv(String ruta) throws IOException {
        Path path = Paths.get(ruta);
        if (!Files.exists(path)) {
            System.out.println("⚠️ Archivo CSV no encontrado en: " + ruta);
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            reader.readLine(); // saltar cabecera
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 4) continue;

                Empleado empleado = new Empleado();
                empleado.setNombre(data[0].trim());
                empleado.setCargo(data[1].trim());
                empleado.setSalario(Double.parseDouble(data[2].trim()));
                empleado.setEmail(data[3].trim());

                // evitar duplicados por email
                if (empleadoRepository.findByEmail(empleado.getEmail()).isEmpty()) {
                    empleadoRepository.save(empleado);
                }
            }
        }
        System.out.println("✅ Empleados importados desde CSV correctamente.");
    }

    private void importarDesdeJson(String ruta) throws IOException {
        Path path = Paths.get(ruta);
        if (!Files.exists(path)) {
            System.out.println("⚠️ Archivo JSON no encontrado en: " + ruta);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Empleado> empleados = mapper.readValue(path.toFile(), new TypeReference<List<Empleado>>() {});

        for (Empleado emp : empleados) {
            if (emp.getEmail() != null && empleadoRepository.findByEmail(emp.getEmail()).isEmpty()) {
                empleadoRepository.save(emp);
            }
        }
        System.out.println("✅ Empleados importados desde JSON correctamente.");
    }
}
