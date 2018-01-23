package Servidor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import hilos.ClientHandler;
import usuarios.Usuario;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

import datos.BD;
import java.awt.Font;

public class DServer extends JFrame{
	private BD bd;
	
	public DServer() {
		bd=new BD();

		
		inicializarVentana();
	
	}
	
	

	
	
	
	
	public String accion;
	private static ServerSocket servidor;  															//Socket por defecto del servidor
	private static Socket cliente; 		  															//Socket para el cliente
	private static Logger loggerServer;
	public static int puertoDef = 1050;
	
	
	private JTextArea textArea;

	private JPanel panel;
	
	public static final int MaxClientes = 5; 														//Numero maximo de clientes conectados a la vez
	private ClientHandler[] hilosClientes = new ClientHandler[MaxClientes]; 							//Array de hilos de los clientes
	

	public void inicializarVentana () {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		this.setBounds(600,300,600,400);
		
		 panel = new JPanel();
		 textArea = new JTextArea();
		
		textArea.setForeground(Color.WHITE);
		textArea.setCaretColor(Color.WHITE);
		
		 ((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		textArea.setBackground(Color.BLACK);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Verdana", Font.BOLD, 18));
	
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		
		
	//Accion que reconoce las lineas	
		
		Action leerAcciones= new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				 try {
					    int offset=textArea.getLineOfOffset(textArea.getCaretPosition());
					    int comienzo=textArea.getLineStartOffset(offset);
					    int fin=textArea.getLineEndOffset(offset);

					    DServer.this.accion=textArea.getText(comienzo, (fin-comienzo));                
					  } catch (BadLocationException ex) {
					    System.out.println(ex.getMessage());
					    
				
				
			}
			
			compararLinea(DServer.this.accion,textArea);
				textArea.append("\n");
			
		};
		
				
		};
		//Anyadimos las acciones al mapa de acciones con sus claves
		
		
		
		
		
		textArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "leerAcciones");
		textArea.getActionMap().put("leerAcciones",leerAcciones);
		
		
		getContentPane().add(scrollPane, BorderLayout.CENTER);		
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		
		this.setVisible(true);
		
		startServer(servidor,textArea);
	}
	

	public void compararLinea (String linea,JTextArea ta) {
		
		switch (linea) {
		case "exit":
			
			try {
			
				for (int i = 0; i < MaxClientes; i++) {
			          if (hilosClientes[i] != null) {
			            hilosClientes[i].interrupt();
			          }
			        }
				servidor.close();
				System.exit(0);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		case "su" :
			ta.append("\n Introduzca el usuario de admin...");
			
		}
		
	}
	
	public void  startServer(ServerSocket server,JTextArea ta) {
		
		
		//Con 3 errores de conexion cerrar el server (esperar un tiempo entre vuelta y vuelta --> 5 segundos
		int fallo=0;
		
		while(fallo<=2) {
			try {
			int i=0;
			ta.append("Esperando clientes...\n");
			
			loggerServer.log(Level.FINE, "esperando al cliente...");
			cliente = servidor.accept(); 
			
			//Servidor acepta la conexion del cliente
				
			
			for (i = 0; i < MaxClientes; i++) {
		          if (hilosClientes[i] == null) {
		            (hilosClientes[i] = new ClientHandler(bd,cliente, hilosClientes,textArea)).start();
		            hilosClientes[i].setName("hilo_"+i);
		           ta.append("conexion con cliente nuevo \n");
		            break;
		          }
		        }
			
			
			
			if (i == MaxClientes) {
		          ta.append("Servidor ocupado. \n");
		          cliente.close();
		        }
			
			
			
		      } catch (IOException e) {
		        System.out.println("Error en la conexion, reconectando...");
		        fallo++;
		      }
			
		
		    }
		System.out.println("Conexion con el servidor cerrada");
	}
	

	
	public static void main(String [] args) {
		
		
		
		Socket cliente = null;
		int puerto;
		puerto=Integer.valueOf(args[1]).intValue();
		loggerServer = Logger.getLogger("Servidor.DServer"); 									//Logger para comprobar el funcionamiento del servidor

		 try {
		servidor = new ServerSocket(puerto);
		}
		catch(IOException e) {
			loggerServer.log(Level.SEVERE, e.getMessage());
		}
		
		DServer ds = new DServer();
		
		
	
		  }
}