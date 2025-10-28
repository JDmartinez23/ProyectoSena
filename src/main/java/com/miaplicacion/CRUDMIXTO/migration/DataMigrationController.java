package com.miaplicacion.CRUDMIXTO.migration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataMigrationController {

    private final CsvJsonImporter csvJsonImporter;
    private final SqlExcelImporter sqlExcelImporter;

    public DataMigrationController(CsvJsonImporter csvJsonImporter, SqlExcelImporter sqlExcelImporter) {
        this.csvJsonImporter = csvJsonImporter;
        this.sqlExcelImporter = sqlExcelImporter;
    }

    @GetMapping("/migrar")
    public String migrarDatos() {
        csvJsonImporter.importarEmpleados();
        sqlExcelImporter.importarProyectos();
        return "✅ Migración completada con éxito (Empleados + Proyectos)";
    }
}
