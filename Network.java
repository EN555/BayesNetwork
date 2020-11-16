package ex0;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Network {

	public HashMap<String, Node> map;
	
	public Network() {			//initial network
		
		this.map = new HashMap<String, Node>();			//contain all the nodes of the network
	}
	public Node addNode(String node)
	{
		StringTokenizer str = new StringTokenizer(node, " ");	
		str.nextToken();	//skip on "parents" word
		String name = str.nextToken();
		Node vertex = new Node(name, this);
		this.map.put(name, vertex);			//search in the map will execute according to the name of the node
		return vertex;
	}
	
	public HashMap<String, Node> getMap(){
		return this.map;
	}

}