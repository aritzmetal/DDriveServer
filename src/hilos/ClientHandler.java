package hilos;
import usuarios.Usuario;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

import datos.Mensaje;
import datos.BD;
import excepciones.BaneadoException;
import usuarios.Usuario;

import java.net.ServerSocket;


/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. The thread broadcast the incoming messages to all clients and
 * routes the private message to the particular client. When a client leaves the
 * chat room this thread informs also all the clients about that and terminates.
 */
public class ClientHandler extends Thread {

  private String clientName = null;
  private BD bd;
  

  
  private Socket clientSocket = null;
  private final ClientHandler[] threads;
  private int clientesMaximos;
  private JTextArea ta;

  public ClientHandler(BD bd,Socket clientSocket, ClientHandler[] threads,JTextArea ta) {
    this.clientSocket = clientSocket;
    this.threads = threads;
    this.ta = ta;
    clientesMaximos = threads.length;
    this.bd=bd;
   
  }
  
	public int realizarLogin(String nom,String pass) throws BaneadoException {
		int login=0;
		try {
		 login = bd.seleccionarUsuario(nom, pass);
		}catch (BaneadoException e) {
			e.printStackTrace();
		}
		if (login==2) {
			ta.append("Usuario "+nom+ " y contraseña correctos, servicio en marcha\n");
			
		
		} else {
			ta.append("Usuario o contraseña erroneos \n Prueba a registrarte... \n");
				
		}
		return login;
	}

  public void run() {
	 boolean running = true;
    int maxClientsCount = this.clientesMaximos;
    ClientHandler[] threads = this.threads;

    
    try {
        /*
         *Imput y outputstreams para el handler y sus derivados
         */
    	 ObjectInputStream is;
    	 ObjectOutputStream os;
    	 
   		 is = new ObjectInputStream(clientSocket.getInputStream());
  		 os =new ObjectOutputStream(clientSocket.getOutputStream());
 
  		 int num=0;
			    for (int i = 0; i < maxClientsCount; i++) {
			      if (threads[i] != null) {
			        num++;
			        
			      }
			    }  
		if(!(num<=maxClientsCount)) {
			this.interrupt();
		}
		
		
		Mensaje msg = null;
  		Usuario us=null;
  		int opc =0;
		while(opc!=2) {
		try {
			synchronized(is) {
			msg = (Mensaje) is.readObject();	
			}
			if(msg.getMess().equals("REG")) {
				RegistroHandler rh = new RegistroHandler(os, is,bd,ta);
				Thread registroThread =new Thread(rh);
				registroThread.start();
				//Esperar que el registro acabe
				while(registroThread.isAlive()) {
					ClientHandler.sleep(1500);
				}

				//msg = (Mensaje) is.readObject();		
				}
			
			//Lee el objeto del socket
			if(msg.getMess().equals("envio")) {
			us = msg.getUs();
			
			ta.append("Entrando en login\n");
			 opc = realizarLogin(us.getNombre(), us.getPass());
			 ta.append("Login realizado\n");
					switch (opc) {
					case 2:
						os.writeObject(new Mensaje(us, "OK"));
						bd.actualizarBD(us, "src/folders/"+us.getNombre());
						ta.append("Usuario logeado correctamente \n");
						  ta.append("Bienvenido" + us.getNombre()
					      + " to our chat room.\nTo leave enter /quit in a new line.");
						  
						break;
					default:
						os.writeObject(new Mensaje(us, "ERR"));
						ta.append("Proceso de login incorrecto del usuario "+ us.getNombre()+"\n");
					}
			}
			
			if(msg.getMess().equals("CAMBIO")) {
				CambioHandler ch = new CambioHandler(msg.getUs(),bd, ta,os, is);
				Thread cambioThread = new Thread(ch);
				cambioThread.start();
				while(cambioThread.isAlive()) {
					ClientHandler.sleep(1500);
				}
				
				
				
			}
			
			if(msg.getMess().equals("CLOSE")) {
				ta.append("\n Proceso login cancelado");
				break;
					
			}
			
				
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
  
		  
		ta.append("\n Cerrando hilo login"); 
		

		  is.close();
		os.close();
		  
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
    
    
    	}
  	}
