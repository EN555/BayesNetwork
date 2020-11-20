package ex0;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class Queries2 {

	Network net;
	private int numOfMul=0;
	private int numOfAdd=0;
	
	
	public Queries2(Network net) {
		this.net = net;
	}
	public Collection<CPT> prob(String s) {
		Double prob= 0.0;
	//get all the hidden variables
	LinkedList<Node> hidden = getHiddenVariables(s);	
	//enter all the cpt that connect to contain the hidden variables organized ABC (e.g Ascii)	
	HashMap<Node,CPT> cpt = getCptHidden(hidden);
	//delete all the evidence cpt that unusable and their state as valus
	HashMap<Node, String> evidence = new HashMap<Node, String>();
	evidence= extractEvidence(s);
	//move on all the cpt  and remove evidence
	for(Map.Entry node : evidence.entrySet()) 
	{
		CPT up =replaceUpdateCpt((Node)node.getKey(), (String)node.getValue());	 
		cpt.put((Node)node.getKey(), up);
	}
	
		return cpt.values();
	}
	
	public CPT replaceUpdateCpt(Node node ,String state) {
		
		CPT curr = this.net.getNode(node.name).getCpt();
		CPT update = new CPT();
		int ColumnState=0;
		for(int c=1 ; c< node.getCurrVar().size()+1 ; c++) {
			if(((String)(curr.mat[0][c])).equals(state)){
				ColumnState= c;
				break;
			}
		}
		update.init(); 		//put zeros at all the matrix
		update.mat[0][1] =state;
			
		for (int i = 1; i < curr.depth; i++) {	//update Strings
			update.mat[i][0]= curr.mat[i][0];
		}
		
		for (int i = 1; i < curr.depth; i++) { 	//update the values of the state
			update.mat[i][ColumnState]=	curr.mat[i][ColumnState];
		}
		
		return update;
	}
	/**
	 * get string and extract all the evidence and put them in 
	 * hashmap that contain key- Node and value - status
	 * @param s
	 * @return
	 */
	public HashMap<Node, String> extractEvidence(String s){
		
		String ex= s.replaceAll("[^A-Za-z]"," ");
		String  [] extr =ex.split(" ");
		HashMap<Node, String> list = new HashMap<Node, String>();

		for(int i= 3 ; i< extr.length ; i+=2) {
			list.put(this.net.getNode(extr[i]), extr[i+1]);
		}

		return list;		//return key-node and value- state of the node
	}
	/**
	 * find all the hidden variables and organise them according to the ABC
	 */
	public LinkedList<Node> getHiddenVariables(String prob){
	
	String procces = prob.replaceAll("[^A-Za-z]"," ");
	String [] all =procces.split(" ");
	LinkedList<Node> exsit = new LinkedList<Node>();
	
	for(int i=1 ; i< all.length ; i+=2) {
		exsit.add(this.net.getNode(all[i]));
	}
	
	Iterator<Node> allVertex = this.net.getNodeCollection().iterator();
	LinkedList<Node> hidden = new LinkedList<Node>();
	
	while(allVertex.hasNext()) {
		Node l1 = allVertex.next();
		if(!exsit.contains(l1)) {
			hidden.add(l1);
		}
	}
	//now organise them according to the ABC
	Comparator<Node> compare= new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	hidden.sort(compare);
		return hidden;
	}
	/**
	 * return all the depended cpt at this nodes
	 * @return
	 */
	public HashMap<Node, CPT> getCptHidden(LinkedList<Node> hidden){
		
		ListIterator<Node> iterHidden = hidden.listIterator();	//all the hidden nodes
		Iterator<Node> allVertex = this.net.getNodeCollection().iterator();	//all the vertex 
		HashMap<Node,CPT> cpt =new HashMap<Node ,CPT>();
		LinkedList<Node> visited = new LinkedList<Node>();
		
		while(iterHidden.hasNext()) {	//move on the hidden variables
			Node l1= iterHidden.next();
		
			while(allVertex.hasNext()) {	//move on all the parents and check who contain this hidden variable
				Node l2 =allVertex.next();
			
				if(!visited.contains(l2)) {
					LinkedList<Node> parents = l2.getParents();	
				
					if(parents.contains(l1) || l1.getName().equals(l2.getName())) {
							cpt.put(l2, l2.getCpt());
							visited.add(l2);
			}
		}	
	}	
}		
		return cpt;		//return organize cpt according to ABC 
	}
	
	/**
	 * delete all the known states of the factors that we don't need them
	 * @param cpt
	 * @return
	 */
	public LinkedList<CPT> deleteEvidence(LinkedList<CPT> cpt){
		LinkedList<CPT> list = new LinkedList<CPT>();
	
		
		
		
		return list;
	}
	
	public CPT join(CPT a, CPT b) {
		CPT res= new CPT();
		
		
		
		
		return res;
	}

	
}
