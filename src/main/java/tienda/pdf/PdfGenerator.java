package tienda.pdf;

import tienda.models.Carrito;
import tienda.models.Descuento;
import tienda.service.TicketService;

public interface PdfGenerator {
    void generateTicket(String rutaSalida, String nombreCliente, String emailCliente,
                        Carrito carrito, Descuento descuentoAplicado, TicketService.Totales totales) throws Exception;
}
