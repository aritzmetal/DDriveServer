package usuarios;

import java.io.*;
import java.util.ArrayList;

import excepciones.BaneadoException;

public class Usuario implements Serializable {

	//Para que la serializacion no de problemas
	 private static final long serialVersionUID = 1L;
			 
	private static String key = "92AE31A79FEEB2A3"; // llave
	private static String iv = "0123456789ABCDEF";// Vector de encriptación

	private String DNI;
	private String nombre;
	private String pass;
	private String preguntaSeguridad;
	private String selecSeguridad;
	private String baneado;

	public Usuario() {
		this.DNI = "111";
		this.preguntaSeguridad = null;
		this.selecSeguridad = "0";
		this.baneado = "no";
	}
	
	public Usuario(String nombre,String pass,String pregunta_seguridad) {
		this.DNI = "111";
		setNombre(nombre);
		setPass(pass);
		this.preguntaSeguridad =pregunta_seguridad;
		this.selecSeguridad = "0";
		this.baneado = "no";

}
	public Usuario(String nombre, String pass) {
		this.DNI = "111";
		setNombre(nombre);
		setPass(pass);
		this.preguntaSeguridad = null;
		this.selecSeguridad = "0";
		this.baneado = "no";
		
	}
	
	public Usuario(String nombre,String pass, String preguntaSeguridad, String selecSeguridad) {
		this.DNI="111A";
		this.nombre=nombre;
		this.pass=pass;
		this.preguntaSeguridad=preguntaSeguridad;
		this.selecSeguridad=selecSeguridad;
		this.baneado="no";
				
	}

	public String getDNI() {
		return DNI;
	}

	public void setDNI(String DNI) {
		this.DNI = DNI;
	}

	public String getNombre() {

		return nombre;

	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// -------------------------------------->Puede que tengamos que modificarlos
	
	
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	// ------------------------------------->

	public String getPreguntaSeguridad() {
		return preguntaSeguridad;
	}

	public void setPreguntaSeguridad(String preguntaSeguridad) {
		this.preguntaSeguridad = preguntaSeguridad;
	}

	public String getSelecSeguridad() {
		return selecSeguridad;
	}

	public void setSelecSeguridad(String selecSeguridad) {
		this.selecSeguridad = selecSeguridad;

	}

	public String getBaneado() {
		return baneado;
	}

	public void setBaneado(String baneado) {
		this.baneado = baneado;
	}

	@Override
	public String toString() {
		return "DNI: " + this.DNI + "\nNOMBRE: " + this.nombre + "\nPREGUNTA SEGURDAD:" + this.preguntaSeguridad
				+ "\nSELECCION SEGURIDAD: " + this.selecSeguridad + "\nBANEADO: " + this.baneado;
	}

	// ----------------------------------------------------------------------->>En
	// nueva pestanya
	public void modificarPass(String passNueva, String preg_seguridad) {
		if (this.preguntaSeguridad.equals(preg_seguridad)) {
			setPass(passNueva);
		} else {
			System.out.println("Pregunta de seguridad erronea");
		}

	};

	public void modificarNombre(String nombreNuevo, String preg_seguridad) {
		if (this.preguntaSeguridad.equals(preg_seguridad)) {
			setNombre(nombreNuevo);
		} else {
			System.out.println("Pregunta de seguridad erronea");
		}
	}
	// ----------------------------------------------------------------------->>

/*	public void escribirEnFichero() {
		String nom = "./src/usuarios/usuarios.txt";

		try {
			File f = new File(nom);

			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bfw = new BufferedWriter(fw);

			bfw.write(this.nombre + "\n");
			bfw.write(this.DNI + "\n");

			try {
				bfw.write(Encriptado.encriptar(key, iv, this.pass) + "\n");
				System.out.println(Encriptado.encriptar(key, iv, this.pass) + " Pass encriptada");
			} catch (Exception e) {

				e.printStackTrace();
			}

			bfw.write(this.preguntaSeguridad + "\n");
			bfw.write(this.selecSeguridad + "\n");
			bfw.write(this.baneado + "\n");

			bfw.flush();
			bfw.close();
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}*/
	
	
	/*
	 * public static void main(String[] args) {
	 * 
	 * ArrayList<Usuario> ar = new ArrayList<Usuario>();
	 * 
	 * Usuario user= new Usuario(); user.setDNI("222");
	 * user.setNombre("cacague"); user.pass="pass123"; user.escribirEnFichero();
	 * 
	 * volcarUsuarios(ar);
	 * 
	 * for(Usuario us: ar){ System.out.println(us.getNombre());
	 * System.out.println(us.pass); } }
	 * 
	 */
	
	public String encriptar(String linea){
		String enc=null;
		try {
		//	enc=Encriptado.encriptar(key, iv, linea);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enc;
	}
	
	public String desenencriptar(String linea){
		String enc=null;
		try {
		//	enc=Encriptado.desencriptar(key, iv, linea);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enc;
	}
	


/*	public static void volcarUsuarios(ArrayList<Usuario> ar) {
		String nom = "./src/usuarios/usuarios.txt";
		try {
			File f = new File(nom);

			FileReader fr = new FileReader(f);
			BufferedReader bfr = new BufferedReader(fr);

			String linea = "";
			while (linea != null) {
				Usuario user = new Usuario();

				linea = bfr.readLine();
				user.setNombre(linea);

				linea = bfr.readLine();
				user.setDNI(linea);

				linea = bfr.readLine();
				if (linea != null) {
					try {
						linea = Encriptado.desencriptar(key, iv, linea);
						user.pass = linea;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				;

				linea = bfr.readLine();
				user.setPreguntaSeguridad(linea);

				linea = bfr.readLine();
				user.setSelecSeguridad(linea);

				linea = bfr.readLine();
				user.setBaneado(linea);

				ar.add(user);

			}
			ar.remove(ar.size() - 1);
			bfr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
