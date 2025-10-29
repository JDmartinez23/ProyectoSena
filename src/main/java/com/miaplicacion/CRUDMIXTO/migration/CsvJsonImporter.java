package com.miaplicacion.CRUDMIXTO.migration;

import com.miaplicacion.CRUDMIXTO.entity.Empleado;
import com.miaplicacion.CRUDMIXTO.repository.EmpleadoRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

@Component
public class CsvJsonImporter {

    private final EmpleadoRepository empleadoRepository;

    public CsvJsonImporter(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public void importarEmpleadosDesdeCsv(String ruta) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/" + ruta))) {
            String[] line;
            reader.readNext(); // saltar cabecera
            while ((line = reader.readNext()) != null) {
                Empleado emp = new Empleado();
                emp.setNombre(line[0]);
                emp.setCargo(line[1]);
                emp.setSalario(Double.parseDouble(line[2]));
                emp.setEmail(line[3]);
                empleadoRepository.save(emp);
            }
        }
    }

    public void importarEmpleadosDesdeJson(String ruta) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Empleado[] empleados = mapper.readValue(
                new java.io.File("src/main/resources/" + ruta), Empleado[].class);
        for (Empleado e : empleados) {
            empleadoRepository.save(e);
        }
    }
}
