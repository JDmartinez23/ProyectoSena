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
            System.out.println("⚠️ Archivo SQL no encontrado: " + ruta);
            return;
        }

        String sql = Files.readString(path);
        String[] inserts = sql.split(";");
        for (String insert : inserts) {
            if (insert.trim().toLowerCase().startsWith("insert")) {
                Proyecto p = new Proyecto();
                // ejemplo: INSERT INTO proyectos (nombre, descripcion) VALUES ('Proyecto 1', 'Descripción 1');
                String valores = insert.substring(insert.indexOf("VALUES") + 6)
                        .replaceAll("[()']", "").replace(";", "").trim();
                String[] campos = valores.split(",");
                if (campos.length >= 2) {
                    p.setNombre(campos[0].trim());
                    p.setDescripcion(campos[1].trim());
                }
                proyectoRepository.save(p);
            }
        }
    }

    private void importarDesdeExcel(String ruta) throws IOException {
        try (FileInputStream fis = new FileInputStream(ruta);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // saltar cabecera
                Proyecto p = new Proyecto();
                p.setNombre(row.getCell(0).getStringCellValue());
                p.setDescripcion(row.getCell(1).getStringCellValue());
                proyectoRepository.save(p);
            }
        }
    }
}
