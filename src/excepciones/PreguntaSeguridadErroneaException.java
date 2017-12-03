package excepciones;

public class PreguntaSeguridadErroneaException {
	String preguntaSeg;
	
	public PreguntaSeguridadErroneaException(String preguntaSeg) {
		this.preguntaSeg= preguntaSeg;
	}

	public String getPreguntaSeg() {
		return "Error!! no coincide la pregunta"+ preguntaSeg;
	}

	
	

}
