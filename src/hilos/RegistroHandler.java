package hilos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JTextArea;

import datos.Mensaje;
import datos.BD;
import excepciones.BaneadoException;
import usuarios.Usuario;


public class RegistroHandler implements Runnable {

	private BD bd;
	private JTextArea ta;
	
	private ObjectOutputStream os;
	private ObjectInputStream is;
	
	
	public RegistroHandler(ObjectOutputStream os, ObjectInputStream is,BD bd,JTextArea ta) {

		this.bd=bd;
		this.os=os;
		this.is =is;
		this.ta=ta;
		
		
	}

	@Override
	public void run() {
		
		Mensaje msg =null;
		Usuario us=null;
		int resul=1;
		
		ta.append("Comenzando un registro del Usuario \n");
			while(resul!=0) {
			try {
				synchronized(is) {
				msg = (Mensaje) is.readObject(); 													//Esperar lectura del primer objeto
				}
				
				if(msg.getMess().equals("CLOSE"))
					break;
				
				us = msg.getUs();
				 resul = bd.seleccionarUsuario(us.getNombre(),us.getPass());
				 
				os.writeObject(new Mensaje(us,String.valueOf(resul) ));   					//Escribir primer resultado
				
				synchronized(is) {
				msg= (Mensaje) is.readObject();	
				//Recibir segunda respuesta
					}
				
				if(resul==0) {
					
					us= msg.getUs();
					bd.crearUsuario(us);
					break;
				}else {
					ta.append("Error en el registro");
				}
			
				
				
			
				
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BaneadoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
			}
			ta.append("Cerrando hilo registro... \n");
			
			
			Thread.currentThread().interrupt();
			
		
	}

}
