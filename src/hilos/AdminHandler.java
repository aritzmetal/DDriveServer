package hilos;

import java.awt.TextArea;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import datos.Mensaje;
import datos.BD;
import excepciones.BaneadoException;
import usuarios.Usuario;

public class AdminHandler implements Runnable{

	
	private BD bd;
	private JTree tree;
	private	Usuario us;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private JTextArea ta;


	public AdminHandler(Usuario us,BD bd,JTextArea ta, ObjectOutputStream os, ObjectInputStream is) {
		super();
		this.bd = bd;
		this.tree = tree;
		this.os = os;
		this.is = is;
		this.us=us;
		this.ta=ta;
	}


	@Override
	public void run() {
		Mensaje msg =null;
		
		ta.append("Entrando en el hilo de administracion \n");
		while(true) {
		try {
			synchronized(is) {
			msg = (Mensaje) is.readObject();	
			}
			if(msg.getMess().equals("subir")) {
			
				//Recibimos el path donde queremos el archivo nuevo
				String pathNuevo = (String) is.readObject();
				pathNuevo = "src/folders/"+pathNuevo;
			
			//Recibimos la oleada de bytes
				String[] caminos = pathNuevo.split("/");
				
			File f = new File(pathNuevo);
			byte [] archivoNuevo = (byte[]) is.readObject();
			Files.write(f.toPath(), archivoNuevo);
			
			bd.actualizarBD(us, "src/folders/"+us.getNombre());
			}
			
			if(msg.getMess().equals("SHUT"))
				break;
				

		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
	
	
	
	

		}
		
		ta.append("Saliendo del hilo de administracion \n");
}	
}

