package excepciones;

public class PassIncorrectaException extends Exception{
	String text = "Contrase�a incorrecta";
	public PassIncorrectaException() {}
	
	
	public String getText() {
		return text;
	}


	
}
