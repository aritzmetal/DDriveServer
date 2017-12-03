package hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JTextArea;

import datos.Mensaje;
import datos.BD;
import excepciones.BaneadoException;
import usuarios.Usuario;

public class CambioHandler implements Runnable{

	
	private BD bd;
	private JTextArea ta;
	private	Usuario usuario;
	private ObjectOutputStream os;
	private ObjectInputStream is;


	public CambioHandler(Usuario us,BD bd, JTextArea ta, ObjectOutputStream os, ObjectInputStream is) {
		super();
		this.bd = bd;
		this.ta = ta;
		this.os = os;
		this.is = is;
		this.usuario=us;
	}


	@Override
	public void run() {
		Mensaje msg =null;
		
		ta.append("Comenzando el proceso de cambio de contraseña");

		int i = 0;
		try {
			i = bd.seleccionarUsuario(usuario.getNombre(), usuario.getPass());
		} catch (BaneadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Seleccion "+i);
		if (i==1) {
			int seleccion = (int) bd.obtenerSeleccion(usuario.getNombre(), "seleccion_seguridad");
			
			try {
				os.writeObject(new Integer (seleccion));
				
				int ok =0;
				
				
				msg= (Mensaje) is.readObject();	
				if(msg.getMess().equals("NOPOPUP")) {
					ta.append("\nProceso cancelado");
					Thread.currentThread().interrupt();
				}
				String seguridad = msg.getUs().getPreguntaSeguridad();
				
				if(seguridad.equals(bd.obtenerSeleccion(msg.getUs().getNombre(), "pregunta_seguridad")) && !msg.getMess().equals("NOPOPUP")) {
					bd.cambiarPass(msg.getUs().getNombre(), msg.getUs().getPass());
					ta.append("\n cambio de contrase\u00f1a correcto");
					
				}
				
						
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				os.writeObject(new Integer(5));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ta.append("\nSaliendo del hilo cambio");
		Thread.currentThread().interrupt();
	}
	
	

}
