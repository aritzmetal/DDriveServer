package excepciones;

public class PassIncorrectaException extends Exception{
	String text = "Contraseņa incorrecta";
	public PassIncorrectaException() {}
	
	
	public String getText() {
		return text;
	}


	
}
