package datos;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import excepciones.BaneadoException;
import usuarios.Usuario;


public class BD {
	private static String key = "92AE31A79FEEB2A3"; // llave
	private static String iv = "0123456789ABCDEF";// Vector de encriptación
	
	
	private Connection con;
	private static Statement stmt;
	
	//Atributos del tree dentro de la base
	private static JTree tree;
	private static DefaultMutableTreeNode raiz;
	private static DefaultTreeModel modeloArbol;
	
	public BD(){
		tree = new JTree();
		
		conectar();
	}
	
	
	public static DefaultMutableTreeNode getRaiz() {
		return raiz;
	}


	public static void setRaiz(DefaultMutableTreeNode raiz) {
		BD.raiz = raiz;
		modeloArbol = new DefaultTreeModel( raiz );
		tree.setModel( modeloArbol );
	}
	
	
	public static JTree getTree() {
		System.out.println(tree.getModel().getRoot());
		return tree;
	}



	/**
	 * Metodo que crea una sentencia para acceder a la base de datos 
	 */
	public void crearSentencia()
	{
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que permite conectarse a la base de datos
	 */

	public void conectar()
	{
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			con= DriverManager.getConnection("jdbc:ucanaccess://C:/Users/aritz/workspace/DDriveServer/src/datos/Usuarios.accdb");
			crearSentencia();
		}catch(Exception e)
		{
			System.out.println("No se ha podido conectar a la base de datos");
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que cierra una sentencia 
	 */
	public void cerrarSentencia()
	{
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que permite desconectarse de la base de datos
	 */
	public void desconectar()
	{
		try {
			cerrarSentencia();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void crearUsuario (Usuario us){
		try {
		//	pass = Encriptado.encriptar(this.key, this.iv, pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "INSERT INTO usuarios(DNI,nombre,password,pregunta_seguridad,seleccion_seguridad,baneado) VALUES('"
		+us.getDNI()+"','"
		+us.getNombre()+"','"
		+us.getPass()+"','"
		+us.getPreguntaSeguridad()+"',"
		+Integer.parseInt(us.getSelecSeguridad())+","
		+us.getBaneado()+")";
		
		try {
			stmt.executeUpdate(query);
			//------------------------------------>Crear la carpeta pertinente del usuario
			new File("./src/folders/"+us.getNombre()).mkdir();
//------------------------------------->
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**

	 *  devuelve @param Object seleccion con el dato de la columna que le pidamos.

	 */
	
	public Object obtenerSeleccion (String nom,String colum) {
		Object seleccion = 0;
		String query = "SELECT * FROM usuarios WHERE nombre='"+nom+"'";
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				seleccion = rs.getObject(colum);
				}
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  seleccion;
		
	}
	
	
	public int seleccionarUsuario (String nom, String pass) throws BaneadoException{
		
		//Crear un usuario para enviarlo por server??
		Usuario us = null;
		
		int comp= 0;
		String query = "SELECT * FROM usuarios WHERE nombre='"+nom+"'";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(rs.next()) {
				try {
					String nombre = rs.getString("nombre");
				//	String contrasenya = Encriptado.desencriptar(this.key, this.iv, rs.getString("password"));
					String contrasenya = rs.getString("password");
					
					String baneado = rs.getString("baneado");
					
					if(!baneado.equals("si")) {
						
					
					if(nombre.equals(nom)) {
							comp= 1;
							
						if(contrasenya.equals(pass)) {
							comp= 2;
						}
					}
					}else {
						rs.close();
						throw new BaneadoException(us);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comp;
	}
	
	public void cambiarPass(String user,String password) {
		try {
		//	password = Encriptado.encriptar(key, iv, password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String query = "UPDATE usuarios SET password='"+password+"' WHERE nombre='"+user+"'";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void actualizarArchivo (Usuario us,File f) {
		
		String query1 = "SELECT * FROM Archivos WHERE Propietario='"+us.getNombre()+"'AND Nombre='"+f.getName()+"' AND Ruta='"+f.getPath()+"'";
		
		
		try {
			ResultSet rs = stmt.executeQuery(query1);
		
		
		String query2 = "INSERT INTO Archivos(Nombre,Ruta,Propietario) VALUES('"
				+f.getName()+"','"
				+f.getPath()+"','"
				+us.getNombre()+
				"')";
		
		
			if(!rs.next()) {
			stmt.executeUpdate(query2);
			System.out.println("Insercion exitosa");
			}else {
				System.out.println("Valor existente");
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	// CODIGO DEL TREE

	public static DefaultMutableTreeNode anyadeNodoHijo( String texto, DefaultMutableTreeNode padre ,Usuario us) {
		System.out.println("Anyadiendo nodo hijo " +texto);
		DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode( texto );
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					if (padre==null) {
						raiz.add( nuevo );
						modeloArbol.nodesWereInserted( raiz, new int[] { raiz.getChildCount()-1 } );
					} else {
						padre.add( nuevo );
						modeloArbol.nodesWereInserted( padre, new int[] { padre.getChildCount()-1 } );
					}
					//tree.setES( new TreePath(nuevo.getPath()), true );
					/*
					TreePath[] tp = (TreePath[]) nuevo.getPath();
					String camino = tp.toString().replaceAll("\\]| |\\[|", "").replaceAll(",", File.separator);
					File f = new File(camino);	
					actualizarArchivo(us,f);
					*/
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nuevo;
	}
	
	// ---------------------------------
	// Recorrido de ficheros
	// ---------------------------------
	
	/** Visualiza el espacio en disco ocupado por cada carpeta del directorio indicado
	 * @param dirIni	Directorio a explorar. Si no es una carpeta correcta el método no hace nada.
	 */
	public static void generarJTree( String dirIni ,Usuario us) {
		File ini = new File( dirIni );
		if (!ini.exists() || !ini.isDirectory()) return;
		recorridoUnidad( ini, 0, raiz ,us);
		modeloArbol.nodeChanged( raiz );  // Lanza evento de modificación en el modelo
	}

	// Método recursivo
	private static void recorridoUnidad( File dir, int nivel, DefaultMutableTreeNode padre ,Usuario us) {
		if (dir==null) return;  // Caso base
		DefaultMutableTreeNode nodoNuevo = null;
		File[] files = null;
		if (dir.isDirectory()) {  // Tratamiento recursivo de directorio
			files = dir.listFiles();
			nodoNuevo = anyadeNodoHijo( dir.getName(), padre ,us);
			if (files!=null) {
				ArrayList<File> al = new ArrayList<>( Arrays.asList( files ));
				final DefaultMutableTreeNode nodoEnCurso = nodoNuevo;
				for (File o2 : al) {
					recorridoUnidad( o2, nivel+1, nodoNuevo ,us);
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							modeloArbol.nodeChanged( nodoEnCurso );  // Lanza evento de modificación en el modelo
						}
					});
				}
			}
		} else {  // Tratamiento de fichero
			nodoNuevo = anyadeNodoHijo( dir.getName(), padre ,us);
			final DefaultMutableTreeNode nodoEnCurso = nodoNuevo;
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					modeloArbol.nodeChanged( nodoEnCurso );  // Lanza evento de modificación en el modelo
				}
			});
		}
	}
	
	
	public void actualizarBD(Usuario user,String root) {
		File f = new File(root);
		if(!f.getName().contains(".")) {
			System.out.println("Encontrado directorio: "+ f.getName());
			for(File subfile: f.listFiles()) {
				if(!subfile.getName().contains(".") && subfile.listFiles().length>0) {
					
					actualizarBD(user,subfile.getPath());
				}else {
					actualizarArchivo(user, subfile);
				
					
				}
			}
		}else {
			//actualizarBD(user,f.getPath());
			System.out.println("No es directorio "+ f.getPath());
		}
		
	}
		
}
