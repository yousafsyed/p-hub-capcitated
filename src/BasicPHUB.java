import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BasicPHUB {
	public Node[] nodes = new Node[] {};

    public float concentratorNumber;

    public float concentratorCapacity;

    public float maxCost = 0;
    
    BasicPHUB(String path) {
    	try {
			this.readInstance(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void readInstance(String path) throws FileNotFoundException, IOException {

		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
			int lineNum = 0;
		    for(String line; (line = br.readLine()) != null; ) {
		
		    	String[] lineArry = line.split("\\s+");
		    	if(lineArry.length > 1) {
			        if(lineNum == 0) {
				          this.concentratorNumber= Float.valueOf(lineArry[2]);
		                  this.concentratorCapacity = Float.valueOf(lineArry[3]);
				        }else {
				        	
				        	     System.out.println("x:"+lineArry[2]+" y:"+lineArry[3]+ " demand "+lineArry[4]+" capacity"+this.concentratorCapacity);
				        
				        	 	float[] coordinates = {Float.valueOf(lineArry[2]), Float.valueOf(lineArry[3])};
			                    float demand = Float.valueOf(lineArry[4]);
			                    float concentratorCapacity = this.concentratorCapacity;
			                    Node node = new Node(coordinates, demand,"client",concentratorCapacity,lineNum+1);
			                    this.nodes = push(this.nodes,node);
			                    //System.out.print(this.nodes);
				        }
				        lineNum++;
		    	}

		    }
		    for (int i=0; this.nodes.length>i; i++) {
		    	Node node = this.nodes[i];
		    	 for (int j=0; this.nodes.length>j; j++) {
		    		 Node destinationNode = this.nodes[j];
		    		 float distance = node.distance(destinationNode);
		    		 if(distance > this.maxCost) {
		    			 this.maxCost = distance;
		    		 }
		    	 }
		    }
		}
	}
	
	
	private boolean solucion_factible() {
		Arrays.sort(this.nodes);
		Node[] solucionNodes = this.nodes;
		int sum = 0;
		for(int i = 0; this.concentratorNumber > i ; i++) {
			solucionNodes = Arrays.copyOf(solucionNodes, solucionNodes.length-1);
		}
		for(int i = 0; solucionNodes.length > i ; i++) {
			sum += solucionNodes[i].demand;
		}
		
		sum = (int) (sum/this.concentratorNumber); 
		System.out.println(sum);
		if(sum > this.concentratorCapacity) {
			return false;
		}else {
			return true;
		}
		
	}
	
	public void generar_solucion_aleatoria() {
		if(this.solucion_factible()) {
			Node[] nodesList = this.nodes;
			Node[] concentradores = sample(nodesList);
			Node[] candidates = instersect(nodesList, concentradores);
			
			for (int i=0; concentradores.length>i; i++) {
				concentradores[i].type = "concentratdor" ;
			}
			
			for (int i=0; candidates.length>i; i++) {
				candidates[i].type = "client" ;
			}
			

			int conectados = 0;
			int sin_conectar = 0;
			for (int i=0; concentradores.length>i; i++) {
				Node concentrador = concentradores[i];
				for (int j=0; candidates.length>j; j++) {
					Node candidate = candidates[j];
					
					if(!candidate.esta_conectado() && candidate.se_puede_conectar(concentrador)) {
						candidate.conectar_a(concentrador);
						concentrador.conectar_a(candidate);
						
						if (candidate.conectado_a(concentrador)) {
							conectados++;
						}else {
							sin_conectar++;
						}
						
					}else {
						sin_conectar++;
					}
				}
			}
			
			System.out.println(candidates.length);
			System.out.println(concentradores.length);
			Node[] solucion = union(candidates,concentradores);
					
			int coste = funcion_objetivo(solucion);
			
			System.out.println(Arrays.toString(solucion));
		    System.out.println("Cost:"+coste);
			
		}else {
			System.out.println("Solucion not factible");
		}
	}
	
	public Node[] union(Node[] array1, Node[] array2) {
		ArrayList<Node> set1 = new ArrayList<Node>(Arrays.asList(array1));
		ArrayList<Node> set2 = new ArrayList<Node>(Arrays.asList(array2));
        Set<Node> set = new HashSet<Node>();

        set.addAll(set1);
        set.addAll(set2);
        ArrayList<Node> union = new ArrayList<Node>(set);
        return union.toArray(new Node[union.size()]);
    }
	
	public Node[] instersect(Node[] nodesList, Node[] concentradores) {
		ArrayList<Node> nodeList = new ArrayList<Node>(Arrays.asList(nodesList));
		ArrayList<Node> concentrator = new ArrayList<Node>(Arrays.asList(concentradores));
		
		ArrayList<Node> intersection = new ArrayList<Node>();

	        for (Node t : nodeList) {
	            if(!concentrator.contains(t)) {
	            	intersection.add(t);
	            	System.out.print(t.id+" ");
	            }
	            
	            
	        }
	        System.out.println("");
	        Collections.shuffle(intersection);
	        return intersection.toArray(new Node[intersection.size()]);
	}
	
	public Node[] sample(Node[] nodesList) {
				ArrayList<Node> node_array = new ArrayList<Node>(Arrays.asList(nodesList));
				int k = (int) this.concentratorNumber; 
				ArrayList<Node> result = new ArrayList<Node>();
				Random x = new Random();
				int index;
				for (int i = 0; i<k; i++) {
				    index = (int)(x.nextDouble()*node_array.size());
				    result.add(node_array.get(index));
				    //System.out.println(node_array.get(index).id);
				    node_array.remove(index);
				};
				return result.toArray(new Node[result.size()]);
	}
	
	
	private static Node[] push(Node[] array, Node value) {
		/*ArrayList<Node> nodeArray = new ArrayList<Node>(Arrays.asList(array));
		nodeArray.add(value);
	    return nodeArray.toArray(new Node[nodeArray.size()]);*/
		final int N = array.length;
		array = Arrays.copyOf(array, N + 1);
		array[N] = value;
	    return array;
	}
	
	
	private int funcion_objetivo(Node[] solucion) {


		double suma = 0;
		float desconectados = 0;
		double valor_por_desconexiones = this.maxCost;
		double penalizacion = 0;

		for(int i = 0; solucion.length>i; i++)
		{
			Node nodo =solucion[i];
			String type = nodo.type;	
			String c = "client";
			
			if (type == c)
			{
				Node[] destinoArry = nodo.connected;
				
				if(destinoArry.length != 0)
				{
					double dis = nodo.distance(destinoArry[0]);
					suma += dis;
				}
				else
				{
					desconectados++;
				}
			}	
		}
		
		if(desconectados > 0)
		{
			penalizacion = valor_por_desconexiones * desconectados;
			suma += penalizacion / 2;
		}
		
		return (int) suma;
	}
}
