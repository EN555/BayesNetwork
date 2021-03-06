package ex0;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Network {

	public HashMap<String, Node> map;
	
	public Network() {			//initial network
		
		this.map = new HashMap<String, Node>();			//contain all the nodes of the network
	}
	/**
	 * create node
	 * @param name of the node
	 * @return this node
	 */
	public Node addNode(String node)
	{
		Node vertex = new Node(node, this);
		this.map.put(node, vertex);			//search in the map will execute according to the name of the node
		return vertex;
	}
	
	public HashMap<String, Node> getMap(){
		return this.map;
	}
	public Collection<Node> getNodeCollection(){
		return this.map.values();
	}
	public Node getNode(String name){
		return this.map.get(name);
	}

}
