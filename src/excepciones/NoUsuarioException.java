package excepciones;

public class NoUsuarioException extends Exception{
	
	private String nombre;
	
	public NoUsuarioException(String nombre) {
		this.nombre=nombre;
			}
	
	
	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void mostrarNombre(){
		System.out.println("El nombre: "+this.getNombre()+" o la contrase\u00f1a son erroneos");
	}

}
