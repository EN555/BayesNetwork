package ex0;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;

public class Queries2 {

	Network net;
	private int numOfMul=0;
	private int numOfAdd=0;
	
	
	public Queries2(Network net) {
		this.net = net;
	}
	public LinkedList<CPT> prob(String s) {
		Double prob= 0.0;
	//get all the hidden variables
	LinkedList<Node> hidden = getHiddenVariables(s);	
	//enter all the cpt that connect to contain the hidden variables organized ABC (e.g Ascii)	
	HashMap<Node,CPT> cpt = getCptHidden(hidden);
	//delete all the evidence cpt that unusable and their state as values
	HashMap<Node, String> evidence = new HashMap<Node, String>();
	evidence= extractEvidence(s);
	//move on all the cpt  and remove evidence
	for(Map.Entry node : evidence.entrySet()) 
	{	
		for(Node ver : cpt.keySet()) {	//move on all the vertex
			LinkedList<Node> par=  ver.getParents();
			Node check = (Node)ver;
			if(((Node)node.getKey()).getName().equals(ver.getName()) || ver.getParents().contains((Node)node.getKey())) {		///need to remove all the columns without one
			
				if(((Node)node.getKey()).getName().equals(ver.getName())) {
				CPT up=	new CPT();
				up = replaceUpdateCpt(ver,(String)node.getValue());	
				cpt.put(ver, up);
					}
				else {		//if the cange in the inner column (e.g first column)
				CPT up = UpdateCptVariables(ver ,(Node)node.getKey(),(String)node.getValue());	 
				cpt.put(ver, up);
				}
			}
		}
	}
	LinkedList<CPT> organize = new LinkedList<CPT>(cpt.values());		// all the cpt organized ABC Ascii
	LinkedList<Node> evidenceName = new LinkedList<Node>(evidence.keySet());	//all the evidence nodes
	ListIterator<CPT> iterAll = organize.listIterator();
	ListIterator<Node> iterEvidence = evidenceName.listIterator();
	//////convert all the cpt/////
	LinkedList<CPT> organizeConv = new LinkedList<CPT>();
	while(iterAll.hasNext()) {
		organizeConv.add(convertCpt(iterAll.next()));
	}
	//////// join ////////
	ListIterator<CPT> iterConv =  organizeConv.listIterator();
	if(organize.size() > 2) {
		CPT margin = iterConv.next();	//keep all the previous
		while(iterEvidence.hasNext()) {
			Node ev = iterEvidence.next();
			while(iterConv.hasNext()) {	
			CPT cp = iterConv.next();
				if(ev.getName().equals(cp.curr.getName()) || cp.curr.getParents().contains(ev.getName()));
				margin = joinCpt(margin , cp);
			}
			margin = MarginCpt(margin , ev);
		}	
		}
		else {
			///marginilzation
		}
	///need to multiple at the query and normalize
		return organize;
	}
	public CPT joinCpt(CPT one ,CPT two) {
		CPT join = new CPT();
		LinkedList<Node> intersection = intersection(one,two);
		CPT big;
		CPT small;
		if(one.depth >two.depth) {
			big = one;
			small =two;
		}
		else{
			big = two;
			small =one;
		}
		LinkedList<Node> reminder = relativeReminder(big,small);
		// the new join cpt
		int sum=1;
		Iterator<Node> iter =reminder.iterator();
		while(iter.hasNext()) {
		sum*=iter.next().currVar.size();	
		}
		join.mat = new Object[big.depth*sum][2];
		//find the intersection
		int locJoin = 1;
		for(int i =1 ; i <small.depth ; i++) {			//???
			for (int j = 0; j < big.depth ; j++) {
			//	String interCheck =
			//	String adder =
			}
		}
		
		
		return join;
		
	}
	public LinkedList<Node> intersection(CPT a , CPT b){
		LinkedList<Node> intersection = new LinkedList<Node>();
		LinkedList<Node> a_node = new LinkedList<Node>(a.curr.parents);
		LinkedList<Node> b_node = new LinkedList<Node>(b.curr.parents);
				a_node.add(a.curr);
				b_node.add(b.curr);
				//find the intersection
		ListIterator<Node> iterA_node = a_node.listIterator();
		while(iterA_node.hasNext()) {
			Node inter =iterA_node.next();
		if(b_node.contains(inter)) {
			intersection.add(inter);
		}
		}
		return intersection;
	}
	
	public LinkedList<Node> relativeReminder(CPT big , CPT small){		///???
		LinkedList<Node> reminder = new LinkedList<Node>();
		LinkedList<Node> a_node = new LinkedList<Node>(small.curr.parents);
		LinkedList<Node> b_node = new LinkedList<Node>(big.curr.parents);
				a_node.add(small.curr);
				b_node.add(big.curr);
				//find the reminder
		ListIterator<Node> iterA_node = a_node.listIterator();
		while(iterA_node.hasNext()) {
			Node inter =iterA_node.next();
		if(!b_node.contains(inter)) {
			reminder.add(inter);
		}
		}
		return reminder;
	}
	public CPT convertCpt(CPT cpt) {
		CPT convCpt = new CPT();
		int dep = (cpt.depth-1)*cpt.varCurr.size()+1;
		convCpt.mat = new Object[dep][2];
		if(cpt.depth==2) {
			convCpt.curr = cpt.curr;
			convCpt.depth = dep;
			convCpt.varPar = cpt.varPar;
			convCpt.varCurr = cpt.varCurr;
			convCpt.width=2;
			for(int i =1 ; i< dep ; i++) {
				convCpt.mat[i][0] = cpt.curr.getName() + " " + (String)cpt.mat[0][i];
			}
			for(int i =1 ; i< dep ; i++) {
				convCpt.mat[i][1] = cpt.mat[1][i];
			}
		}
		else {
			convCpt.curr = cpt.curr;
			convCpt.depth = dep;
			convCpt.varPar = cpt.varPar;
			convCpt.varCurr = cpt.varCurr;
			convCpt.width=2;
			int num= cpt.varCurr.size();
			int var = 1; 
			int counter=1;
			while((1+num) >var) {
			for(int i =1 ; i< cpt.depth ; i++) {
				convCpt.mat[counter][0] =(String)cpt.mat[i][0] +  cpt.curr.getName() + " " + (String)cpt.mat[0][var] ;
			counter++;
			}
			var++;
			}
			//copy all the columns
			int row=1;
			for(int i =1 ; i < cpt.varCurr.size()+1 ; i++) {
				for(int j = 1 ; j < cpt.depth ; j++) {
					convCpt.mat[row][1] = cpt.mat[j][i];
					row++;
				}
			}				
		}
		return convCpt;
	}
	
	public CPT MarginCpt(CPT one , Node marg) {
		CPT margin = new CPT();
		
		
		
		
		return margin;
		
	}
	public CPT UpdateCptVariables(Node improve ,Node curr ,String state) {
	CPT cpt = new CPT();
	CPT needUp = this.net.getNode(improve.getName()).getCpt();
	String check = curr.getName() + " " + state;
	//keep all the rows that not contain the evidence state
	Queue<Integer> queue = new LinkedList<Integer>();
	
	for(int i = 1 ;i < needUp.depth ; i++) {
		if(((String)(needUp.mat[i][0])).contains(check)) {
			queue.add(i);
		}
	}
	initCpt(cpt, queue.size()+1, needUp.varCurr.size()+1);
	cpt.depth = queue.size()+1;
	cpt.width = needUp.varCurr.size()+1;
	cpt.setCurrVar(needUp.varCurr);
	cpt.curr = improve;
	//insert all the relevant places to the 

	for (int i = 1; i < needUp.varCurr.size() +1; i++) {
		cpt.mat[0][i] = (String)(needUp.mat[0][i]);
	}
		int row=1;
		while(!queue.isEmpty()) {
		int need =queue.poll();
			for (int i = 0; i < needUp.varCurr.size()+1; i++) {
			 cpt.mat[row][i]= needUp.mat[need][i];	
		}
			row++;
	}
		
	return cpt;	
		
	}
	public CPT initCpt(CPT cpt , int rows , int columns) {
		cpt.mat= new Object[rows][columns];
		for(int i=0 ; i <rows ; i++) {
			for(int j=0 ; j<columns ; j++) {
				cpt.mat[i][j]=0.0;
			}
		}
		return cpt;
	}
	/**
	 * this method take care at each node that contain this state at the first row 
	 * @param node
	 * @param state
	 * @return
	 */
	public CPT replaceUpdateCpt(Node node ,String state) {
		
		//need to split between two cases if it's state of current node or it's in the dependence condition of the node
		CPT curr = this.net.getNode(node.name).getCpt();
		CPT update = new CPT();
		int ColumnState=0;
		for(int c=1 ; c< node.getCurrVar().size()+1 ; c++) {
			if(((String)(curr.mat[0][c])).equals(state)){
				ColumnState= c;
				break;
			}
		}
		update.mat = new Object[curr.depth][2];		//it's always two because that in the evidence have only one option
		update.depth = curr.depth;
		update.curr= node;
		update.width=2;
		LinkedList<String> varUp= new LinkedList<String>();
		varUp.add(state);
		update.setCurrVar(varUp);
		update.mat[0][1] =state;
			
		for (int i = 1; i < curr.depth; i++) {	//update Strings
			update.mat[i][0]= curr.mat[i][0];
		}
		
		for (int i = 1; i < curr.depth; i++) { 	//update the values of the state
			update.mat[i][1]=	curr.mat[i][ColumnState];
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
			allVertex = this.net.getNodeCollection().iterator();
}		
		return cpt;		//return organize cpt according to ABC 
	}
	
//	/**
//	 * delete all the known states of the factors that we don't need them
//	 * @param cpt
//	 * @return
//	 */
//	public LinkedList<CPT> deleteEvidence(LinkedList<CPT> cpt){
//		LinkedList<CPT> list = new LinkedList<CPT>();
//	
//		
//		
//		
//		return list;
//	}
//	
	public CPT join(CPT a, CPT b) {
		CPT res= new CPT();
		
		
		
		
		return res;
	}

	
}
