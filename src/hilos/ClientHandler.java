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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import datos.Mensaje;
import datos.BD;
import excepciones.BaneadoException;
import usuarios.Usuario;

import java.net.ServerSocket;



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
		while(opc!=1) {
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
						
						BD.setRaiz(new DefaultMutableTreeNode("Archivos"));
						 BD.generarJTree("src/folders/"+us.getNombre()+"/",us);
						
						 os.writeObject(BD.getTree().getModel());
					
						  ta.append("Usuario: " + us.getNombre()
					      + " Logeado satsifactoriamente \n");
						  
						 AdminHandler ah = new AdminHandler(msg.getUs(), bd, ta, os, is);
						 Thread adminThread = new Thread(ah);
						 adminThread.start();
						 while(adminThread.isAlive()) {
							ClientHandler.sleep(1500);
						 }
						  opc=1;
						  break;
					case 0:
						os.writeObject(new Mensaje(us, "ERR"));
						ta.append("Proceso de login incorrecto del usuario "+ us.getNombre()+"\n");
						break;
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
	
		os.flush();
		
		  is.close();
		os.close();
		  
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
    
    
    	}
  	}
