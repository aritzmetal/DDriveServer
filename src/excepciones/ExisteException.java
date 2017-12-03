package excepciones;

import usuarios.Usuario;

public class ExisteException extends Exception {
	private String us;
	public ExisteException(String us) {
			this.us=us;
	}
	
	public String getUs() {
		return us;
	}

	
	

}
