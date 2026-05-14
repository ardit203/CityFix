package finki.ukim.backend.reporting_and_export_import.service.domain.impl;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.reporting_and_export_import.service.domain.RequestExportService;
import finki.ukim.backend.request_management.service.domain.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

@Service
@AllArgsConstructor
public class RequestExportServiceImpl implements RequestExportService {
    private final RequestService requestService;

    @Override
    @Transactional(readOnly = true)
    public byte[] exportRequestAsPdf(Long requestId, User currentUser) {
        Request request = requestService.findById(requestId, currentUser);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            Paragraph title = new Paragraph("CityFix Request Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            document.add(new Paragraph("Request Information", sectionFont));
            document.add(createRequestInfoTable(request));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Description", sectionFont));
            document.add(new Paragraph(safe(request.getDescription()), normalFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Summary", sectionFont));
            document.add(new Paragraph(safe(request.getSummary()), normalFont));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Location", sectionFont));
            document.add(new Paragraph(formatLocation(request), normalFont));

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export request as PDF", e);
        }
    }

    private PdfPTable createRequestInfoTable(Request request) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        addRow(table, "ID", String.valueOf(request.getId()));
        addRow(table, "Title", safe(request.getTitle()));
        addRow(table, "Status", enumName(request.getStatus()));
        addRow(table, "Routing Status", enumName(request.getRoutingStatus()));
        addRow(table, "Priority", enumName(request.getPriority()));
        addRow(table, "Category", request.getCategory() != null ? request.getCategory().getName() : "None");
        addRow(table, "Department", request.getDepartment() != null ? request.getDepartment().getName() : "None");
        addRow(table, "Municipality", request.getMunicipality() != null ? request.getMunicipality().getName() : "None");
        addRow(table, "Citizen", request.getUser() != null ? request.getUser().getUsername() : "None");

        return table;
    }

    private void addRow(PdfPTable table, String label, String value) {
        table.addCell(label);
        table.addCell(safe(value));
    }

    private String formatLocation(Request request) {
        if (request.getLocation() == null) {
            return "No location provided";
        }

        return "Latitude: " + request.getLocation().getLatitude()
                + "\nLongitude: " + request.getLocation().getLongitude();
    }

    private String enumName(Enum<?> value) {
        return value != null ? value.name() : "None";
    }

    private String safe(String value) {
        return value != null && !value.isBlank() ? value : "None";
    }
}
