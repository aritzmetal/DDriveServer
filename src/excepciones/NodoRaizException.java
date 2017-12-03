package excepciones;

public class NodoRaizException extends Exception {
		String nombre;
	public NodoRaizException(String nombre) {
		this.nombre = nombre;
	}

	public void creaFicheroRaiz (){
		System.out.println("No se pueden crea ficheros en nodo raiz  "+nombre);
	}
	
	public void eliminaFicheroRaiz(){
		System.out.println("No se puede eliminar el nodo raiz  "+nombre);
	}
}
