package excepciones;

import javax.swing.JTree;

public class ErrorSistemaException extends Exception {

	public ErrorSistemaException(String nombre,JTree tree) throws NodoRaizException {
		if(!tree.getSelectionPath().toString().equals(nombre)){
		System.out.println("El sistema no permite que hagas eso con la carpeta: "+nombre);
		}else{
			throw new NodoRaizException(nombre);
		}
	}

}
