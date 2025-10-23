package com.miaplicacion.CRUDMIXTO.service;

import com.miaplicacion.CRUDMIXTO.entity.Proyecto;
import com.miaplicacion.CRUDMIXTO.repository.ProyectoRepository;
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
public class ProyectoService {

    private final ProyectoRepository repository;

    public ProyectoService(ProyectoRepository repository) {
        this.repository = repository;
    }

    // ✅ CRUD básico
    public List<Proyecto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Proyecto> buscarPorId(String id) {
        return repository.findById(id);
    }

    public Proyecto guardar(Proyecto proyecto) {
        return repository.save(proyecto);
    }

    public void eliminar(String id) {
        repository.deleteById(id);
    }

    // ✅ Buscar por nombre
    public List<Proyecto> buscarPorNombre(String q) {
        return repository.findByNombreContainingIgnoreCase(q);
    }

    // ✅ Exportar a Excel (Apache POI)
    public ByteArrayInputStream exportarExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Proyectos");

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Nombre");
            header.createCell(2).setCellValue("Descripción");
            header.createCell(3).setCellValue("Estado");

            // Datos
            List<Proyecto> proyectos = listarTodos();
            int rowIdx = 1;
            for (Proyecto p : proyectos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNombre());
                row.createCell(2).setCellValue(p.getDescripcion());
                row.createCell(3).setCellValue(p.getEstado() != null ? p.getEstado() : "Pendiente");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error al exportar proyectos a Excel: " + e.getMessage(), e);
        }
    }

    // ✅ Exportar a PDF (iText5)
    public ByteArrayInputStream exportarPdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 5, 2});

            // Cabecera
            String[] headers = {"ID", "Nombre", "Descripción", "Estado"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Datos
            for (Proyecto p : listarTodos()) {
                table.addCell(p.getId());
                table.addCell(p.getNombre());
                table.addCell(p.getDescripcion());
                table.addCell(p.getEstado() != null ? p.getEstado() : "Pendiente");
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Lista de Proyectos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph(" "));
            document.add(table);
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error al exportar proyectos a PDF: " + e.getMessage(), e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
