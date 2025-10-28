package com.miaplicacion.CRUDMIXTO.migration;

import com.miaplicacion.CRUDMIXTO.entity.Proyecto;
import com.miaplicacion.CRUDMIXTO.repository.ProyectoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

@Component
public class SqlExcelImporter {

    private final ProyectoRepository proyectoRepository;

    public SqlExcelImporter(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public void importarProyectos() {
        try {
            importarDesdeSql("src/main/resources/data/proyectos.sql");
            importarDesdeExcel("src/main/resources/data/proyectos_20251023_152658.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("Error al importar proyectos: " + e.getMessage(), e);
        }
    }

    private void importarDesdeSql(String ruta) throws IOException {
        Path path = Paths.get(ruta);
        if (!Files.exists(path)) {
            System.out.println("⚠️ Archivo SQL no encontrado en: " + ruta);
            return;
        }

        List<String> lineas = Files.readAllLines(path);
        for (String linea : lineas) {
            if (linea.trim().toUpperCase().startsWith("INSERT")) {
                String[] valores = linea.split("\\(")[1].split("\\)")[0].split(",");
                if (valores.length >= 3) {
                    Proyecto proyecto = new Proyecto();
                    proyecto.setNombre(valores[0].replace("'", "").trim());
                    proyecto.setDescripcion(valores[1].replace("'", "").trim());
                    proyecto.setEstado(valores[2].replace("'", "").trim());
                    proyectoRepository.save(proyecto);
                }
            }
        }
        System.out.println("✅ Proyectos importados desde SQL correctamente.");
    }

    private void importarDesdeExcel(String ruta) throws IOException {
        Path path = Paths.get(ruta);
        if (!Files.exists(path)) {
            System.out.println("⚠️ Archivo Excel no encontrado en: " + ruta);
            return;
        }

        try (FileInputStream fis = new FileInputStream(ruta);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // saltar cabecera

                Proyecto p = new Proyecto();
                p.setNombre(row.getCell(0).getStringCellValue());
                p.setDescripcion(row.getCell(1).getStringCellValue());
                if (row.getCell(2) != null)
                    p.setEstado(row.getCell(2).getStringCellValue());

                proyectoRepository.save(p);
            }
        }
        System.out.println("✅ Proyectos importados desde Excel correctamente.");
    }
}
