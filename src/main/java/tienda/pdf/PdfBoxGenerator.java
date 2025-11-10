package tienda.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import tienda.models.Carrito;
import tienda.models.Descuento;
import tienda.models.ItemCarrito;
import tienda.service.TicketService;
import tienda.util.FormatUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfBoxGenerator implements PdfGenerator {

    @Override
    public void generateTicket(String rutaSalida, String nombreCliente, String emailCliente,
                               Carrito carrito, Descuento descuentoAplicado, TicketService.Totales totales) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                var fontB = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float left = 50f;
                float y = page.getMediaBox().getHeight() - 60;

                // Header
                cs.beginText();
                cs.setFont(fontB, 16);
                cs.newLineAtOffset(left, y);
                cs.showText("TIENDA - Ticket de compra");
                cs.endText();
                y -= 20;

                cs.beginText();
                cs.setFont(font, 10);
                cs.newLineAtOffset(left, y);
                cs.showText("Cliente: " + nombreCliente + "    Email: " + emailCliente);
                cs.endText();
                y -= 14;

                cs.beginText();
                cs.setFont(font, 10);
                cs.newLineAtOffset(left, y);
                cs.showText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                cs.endText();
                y -= 18;

                // headers
                cs.beginText(); cs.setFont(fontB, 10); cs.newLineAtOffset(left, y); cs.showText("Producto"); cs.endText();
                cs.beginText(); cs.setFont(fontB, 10); cs.newLineAtOffset(360, y); cs.showText("Cant"); cs.endText();
                cs.beginText(); cs.setFont(fontB, 10); cs.newLineAtOffset(420, y); cs.showText("Subtotal"); cs.endText();
                y -= 12;

                FormatUtil fu = new FormatUtil();
                for (ItemCarrito it : carrito.getItems()) {
                    cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(left, y); cs.showText(it.getProducto().getNombre()); cs.endText();
                    cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(360, y); cs.showText(String.valueOf(it.getCantidad())); cs.endText();
                    cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(420, y); cs.showText(fu.moneda(it.getSubtotal())); cs.endText();
                    y -= 12;
                }

                y -= 8;
                cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(left, y); cs.showText("Subtotal:"); cs.endText();
                cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(120, y); cs.showText(fu.moneda(totales.subtotal)); cs.endText();
                y -= 12;

                cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(left, y); cs.showText("Descuento:"); cs.endText();
                String d = (descuentoAplicado!=null? fu.moneda(descuentoAplicado.getMonto()) + " ["+ descuentoAplicado.getEtiqueta()+ "]" : fu.moneda(0));
                cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(120, y); cs.showText(d); cs.endText();
                y -= 12;

                cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(left, y); cs.showText("IVA (16%):"); cs.endText();
                cs.beginText(); cs.setFont(font, 10); cs.newLineAtOffset(120, y); cs.showText(fu.moneda(totales.iva)); cs.endText();
                y -= 12;

                cs.beginText(); cs.setFont(fontB, 12); cs.newLineAtOffset(left, y); cs.showText("TOTAL:"); cs.endText();
                cs.beginText(); cs.setFont(fontB, 12); cs.newLineAtOffset(120, y); cs.showText(fu.moneda(totales.total)); cs.endText();
            }

            doc.save(rutaSalida);
        }
    }
}
