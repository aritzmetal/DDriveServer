package excepciones;

public class PassIncorrectaException extends Exception{
	String text = "Contraseña incorrecta";
	public PassIncorrectaException() {}
	
	
	public String getText() {
		return text;
	}


	
}
