import java.util.*;
import java.io.*;
import java.sql.*;

public class GestorPedidos {
    private Connection conexionBD;
    private PedidoDAO pedidoDAO;

    public GestorPedidos() {
        try {
            this.conexionBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tienda", "root", "admin123");

            this.pedidoDAO = new PedidoDAO(conexionBD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void procesarPedido(String nombreCliente, String emailCliente,
            List<String> nombresProductos,
            List<Double> preciosProductos,
            List<Integer> cantidades,
            String tipoCliente) {
        if (!ValidarCliente.validarNombre(nombreCliente)) {
            System.out.println("Error: nombre de cliente invalido");
            return;
        }

        if (!ValidarCliente.validarEmail(emailCliente)) {
            System.out.println("Error: email invalido");
            return;
        }
        double subtotal = 0;
        for (int i = 0; i < nombresProductos.size(); i++) {
            subtotal += preciosProductos.get(i) * cantidades.get(i);
        }
        EstrategiaDescuento estrategia;

        switch (tipoCliente) {
            case "VIP":
                estrategia = new DescuentoVIP();
                break;

            case "FRECUENTE":
                estrategia = new DescuentoFrecuente();
                break;

            case "REGULAR":
                estrategia = new DescuentoRegular();
                break;

            default:
                estrategia = new DescuentoNuevo();
                break;
        }

        double descuento = estrategia.calcularDescuento(subtotal);
        double impuesto = (subtotal - descuento) * 0.12;
        double total = subtotal - descuento + impuesto;
        pedidoDAO.guardarPedido(nombreCliente, total);
        try {
            FileWriter writer = new FileWriter("factura_" + nombreCliente + ".txt");
            writer.write("FACTURA\n");
            writer.write("Cliente: " + nombreCliente + "\n");
            for (int i = 0; i < nombresProductos.size(); i++) {
                writer.write(nombresProductos.get(i) + " x" + cantidades.get(i)
                        + " = $" + (preciosProductos.get(i) * cantidades.get(i)) + "\n");
            }
            writer.write("Subtotal: $" + subtotal + "\n");
            writer.write("Descuento: $" + descuento + "\n");
            writer.write("Impuesto: $" + impuesto + "\n");
            writer.write("TOTAL: $" + total + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error al generar la factura: " + e.getMessage());
        }
        System.out.println("Enviando correo a " + emailCliente + "...");
        System.out.println("Asunto: Confirmacion de pedido");
        System.out.println("Cuerpo: Estimado " + nombreCliente + ", su pedido por $"
                + total + " ha sido procesado.");
        System.out.println("[LOG] Pedido procesado para " + nombreCliente
                + " - Total: " + total);
    }

    public void cancelarPedido(String nombreCliente, String emailCliente, int idPedido) {
        if (!ValidarCliente.validarNombre(nombreCliente)) {
            System.out.println("Error: nombre de cliente invalido");
            return;
        }

        if (!ValidarCliente.validarEmail(emailCliente)) {
            System.out.println("Error: email invalido");
            return;
        }
        pedidoDAO.cancelarPedido(idPedido);
        System.out.println("Enviando correo a " + emailCliente + "...");
        System.out.println("Asunto: Cancelacion de pedido");
        System.out.println("Cuerpo: Estimado " + nombreCliente + ", su pedido #"
                + idPedido + " ha sido cancelado.");
    }
}