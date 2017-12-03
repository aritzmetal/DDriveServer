package datos;

import java.io.Serializable;

import usuarios.Usuario;

public class Mensaje implements Serializable {
	
	private static final long serialVersionUID = 2L;
	
	private Usuario us;
	private String mess;

	public Mensaje() {
	}

	public Mensaje(Usuario us, String mess) {
		super();
		this.us = us;
		this.mess = mess;
	}

	public Usuario getUs() {
		return us;
	}

	public void setUs(Usuario us) {
		this.us = us;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	@Override
	public String toString() {
		return "Mensaje [Usuario=" + us + ", mensaje=" + mess + "]";
	}
	

}
