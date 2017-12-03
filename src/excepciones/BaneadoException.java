package excepciones;

import java.io.*;

import usuarios.Usuario;

public class BaneadoException extends Exception {

	private Usuario us;

		public BaneadoException(Usuario us) {
			this.us = us;
			
	}

		public Usuario getUs() {
			return us;
		}		
		
		public void mostrarException(){

			System.out.println("-----------------ERROR!!-------------");
			System.out.println("---------------EL USUARIO:-----------");
			System.out.println("------------NOMBRE: "+us.getNombre());
			System.out.println("---------SE ENCUENTRA BANEADO--------");
			System.out.println("-------------------------------------");
			System.out.println("----CONTACTA CON EL ADMINISTRADOR----");
			System.out.println("--------PARA MAS INFORMACION---------");
		}

}
