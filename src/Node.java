import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node>{
	public int countId = 0;

    public int id;

    public float[] coordinates;

    public float reserve;

    public float serviceCapacity = 0;

    public float demand = 1;

    public float concentratorId;

    public String type = "client"; 
    
    public Node[] connected = new Node[] {};

	public Node(float[] coordinates, float demand, String type, float concentratorCapacity, int id) {
		this.coordinates = coordinates;
		this.demand = demand;
		this.type = type;
		this.serviceCapacity = concentratorCapacity;
		this.reserve = concentratorCapacity;
		this.id = id;
	}

	public float distance(Node destinationNode) {
		float[] coordenadasPropias = this.coordinates;
		float[] coordenadasVecino = destinationNode.coordinates;
		
			double vecinoX;
			double vecinoY;
			double propiaX;
			double propiaY;
			double resultado;
			
			vecinoX = coordenadasVecino[0];
			vecinoY = coordenadasVecino[1];
			propiaX = coordenadasPropias[0];
			propiaY = coordenadasPropias[1];
			
			resultado = Math.sqrt(((vecinoX-propiaX) * (vecinoX-propiaX)) + ((vecinoY - propiaY) * (vecinoY - propiaY)));
			
			return (int) resultado;
		
	}
	
	public int compareTo(Node compareNode) {
		float compareQuantity = ((Node) compareNode).demand; 
		return (int) (this.demand - compareQuantity);
	}	
	

	public boolean esta_conectado() {
		Node[] conexion = this.connected;
		
		if(conexion == null)
		{
			
			return false;
		}
		
		if(conexion.length > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean se_puede_conectar(Node otro) {
		String tipo_concentrador = "concentrador"; 
		String mi_tipo;
		String otro_tipo;

		double demanda;
		double reserva;

		
		mi_tipo = this.type;
		otro_tipo = otro.type;
		
		if(mi_tipo == otro_tipo)
		{
			return false;
		}
		else
		{
			if(mi_tipo == tipo_concentrador)
			{
				reserva = this.reserve;
				demanda = otro.demand;
			}
			else
			{
				demanda = this.demand;
				reserva = otro.reserve;
			}
			
			if(reserva >= demanda)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public void conectar_a(Node otro) {

		Node[] connected;
		double reserva;
		String mi_tipo;
		String tipo_otro;
		String tipo_cliente;

		connected = this.connected;
		reserva = this.reserve;
		
		mi_tipo = this.type;
		tipo_otro = otro.type;
		tipo_cliente = "cliente";
		
		if(mi_tipo == tipo_otro)
		{
			//throw new Exception("No se puede conectar dos nodos iguales\n");
		}
		
		if(mi_tipo == tipo_cliente)
		{
			this.desconectar();
			connected = push(connected, otro);
			
			this.concentratorId = otro.id;
			this.connected = connected;
		}
		else
		{
			boolean ya_existe =this.conectado_a(otro);
			
			if(ya_existe == false)
			{
				connected = push(connected, otro);
				reserva -= this.demand;
				this.connected= connected;
				this.reserve= (float) reserva;
			}
		}
	}
	
	private static Node[] push(Node[] array, Node value) {
		final int N = array.length;
		array = Arrays.copyOf(array, N + 1);
		array[N] = value;
	    return array;
	}
	
	public void desconectar() {
		this.connected = null;
		this.concentratorId = 0;
	}
	
	
	public boolean conectado_a(Node other) {

		if( other.connected.length == 0) {
			return false;
		}else {
			boolean resultado = false;
			for(int i = 0; other.connected.length>i;i++) {
				Node nodo = other.connected[i];
				if(this.includes(nodo)) {
					resultado= true;
				}else {
					resultado= false;
				}
			}
			return resultado;
		}
	}
	
	public boolean includes(Node node) {
		for(int i = 0; this.connected.length>i;i++) {
			Node current = this.connected[i];
			if(current.id == node.id) {
				return true;
			}
		}
		return false;
		
	}

}
