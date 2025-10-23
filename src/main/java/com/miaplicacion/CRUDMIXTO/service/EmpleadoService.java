package com.miaplicacion.CRUDMIXTO.service;

import com.miaplicacion.CRUDMIXTO.entity.Empleado;
import com.miaplicacion.CRUDMIXTO.repository.EmpleadoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    // ✅ CRUD básico
    public List<Empleado> listarTodos() {
        return repo.findAll();
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Empleado guardar(Empleado e) {
        return repo.save(e);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ✅ Buscar por nombre o cargo
    public List<Empleado> buscarPorNombreOCargo(String q) {
        return repo.findByNombreContainingIgnoreCaseOrCargoContainingIgnoreCase(q, q);
    }

    // ✅ Exportar a Excel (Apache POI)
    public ByteArrayInputStream exportarExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Empleados");

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Cargo");
            header.createCell(4).setCellValue("Salario");

            // Datos
            List<Empleado> empleados = listarTodos();
            int rowIdx = 1;
            for (Empleado e : empleados) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getNombre());
                row.createCell(2).setCellValue(e.getEmail());
                row.createCell(3).setCellValue(e.getCargo());
                row.createCell(4).setCellValue(e.getSalario() != null ? e.getSalario() : 0);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error al exportar a Excel: " + e.getMessage(), e);
        }
    }

    // ✅ Exportar a PDF (iText)
    public ByteArrayInputStream exportarPdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 4, 3, 2});

            // Cabecera
            String[] headers = {"ID", "Nombre", "Email", "Cargo", "Salario"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Datos
            for (Empleado e : listarTodos()) {
                table.addCell(String.valueOf(e.getId()));
                table.addCell(e.getNombre());
                table.addCell(e.getEmail());
                table.addCell(e.getCargo());
                table.addCell(e.getSalario() != null ? e.getSalario().toString() : "0");
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Lista de Empleados", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));
            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al exportar a PDF: " + e.getMessage(), e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
