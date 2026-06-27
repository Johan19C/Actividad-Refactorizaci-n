package servicio;
import java.util.*;

import dao.PedidoDAO;
import estrategia.DescuentoFrecuente;
import estrategia.DescuentoNuevo;
import estrategia.DescuentoRegular;
import estrategia.DescuentoVIP;
import estrategia.EstrategiaDescuento;
import util.ConexionBD;
import util.ValidarCliente;

import java.sql.*;

public class GestorPedidos {

    private PedidoDAO pedidoDAO;
    private GeneradorFactura generadorFactura;
    private ServicioCorreo servicioCorreo;

    public GestorPedidos() {
        try {
            Connection conexionBD = ConexionBD.getConnection();
            this.pedidoDAO = new PedidoDAO(conexionBD);
            this.generadorFactura = new GeneradorFactura();
            this.servicioCorreo = new ServicioCorreo();

        } catch (Exception e) {
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

        generadorFactura.generarFactura(
                nombreCliente,
                nombresProductos,
                preciosProductos,
                cantidades,
                subtotal,
                descuento,
                impuesto,
                total);

        servicioCorreo.enviarConfirmacion(nombreCliente, emailCliente, total);
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
        servicioCorreo.enviarCancelacion(nombreCliente, emailCliente, idPedido);
    }
}