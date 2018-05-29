
public class Start {
	
	 public static void main(String[] args) {
		 	BasicPHUB phub = new BasicPHUB("/Users/yousaf/master/Practica1-Metaheuristica/instancias/phub_50_5_1.txt");
		 	for(int i=1;1000>=i;i++) {
		 		System.out.println("Itr: "+i);
		 		phub.generar_solucion_aleatoria();
		 	}
	        
	 }

}
