package servicio;
public class ServicioCorreo {

    public void enviarConfirmacion(String nombreCliente, String emailCliente, double total) {
        System.out.println("Enviando correo a " + emailCliente + "...");
        System.out.println("Asunto: Confirmacion de pedido");
        System.out.println("Cuerpo: Estimado " + nombreCliente
                + ", su pedido por $" + total + " ha sido procesado.");
    }

    public void enviarCancelacion(String nombreCliente, String emailCliente, int idPedido) {
        System.out.println("Enviando correo a " + emailCliente + "...");
        System.out.println("Asunto: Cancelacion de pedido");
        System.out.println("Cuerpo: Estimado " + nombreCliente
                + ", su pedido #" + idPedido + " ha sido cancelado.");
    }
}