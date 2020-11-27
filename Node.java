package ex0;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class Node {
	String name;
	public LinkedList<Node> parents;		//pointers to the parents
	public LinkedList<String> currVar;		//it's only the current variables
	CPT cpt;
	Network net;
	
	public Node(String name, Network net)
	{
		this.name = name;
		this.parents = new LinkedList<Node>();;
		this.currVar = new LinkedList<String>();;
		this.cpt = new CPT(this);		// create cpt for the node
		this.net = net;
		
	}
	/**
	 * add the variables to the current LinkedList and update at the CPT class 
	 * @param currVar
	 */
		
	public void addCurr(String currVar) {
		
		this.currVar = new LinkedList<String>();		//initial currVar
		StringTokenizer str = new StringTokenizer(currVar, ",// //:");
		str.nextElement(); 			//to delete "value"
		while(str.hasMoreTokens())
		{
			this.currVar.add(str.nextToken()); //add all the current element to the cuurent list
		}
		
		this.cpt.setCurrVar(this.currVar);	//update the currVar at CPT
	}
/**	
 * create the list of the parents node
 * and create linkeslist of linkedlist of all the strings of the fathers
 * @param parVar
 */
	public void addPar(String parVar) {		///create the variables at lists
		
		if(parVar.contains("none")) {		//if this node havn't parent
		this.cpt.setVarPar(new LinkedList<LinkedList<String>>());
		return ;
		}

		StringTokenizer str = new StringTokenizer(parVar, ",// //:");
		str.nextToken();	//skip on "parents" word
		
		while(str.hasMoreTokens())
		{
			String vertex = str.nextToken();
			Node ver = this.net.getMap().get(vertex);
			this.parents.add(ver);
		}
		
		LinkedList<LinkedList<String>> list = new LinkedList<LinkedList<String>>();		//create all the linkedlist of the Variables of the fathers
	    ListIterator<Node> it = this.parents.listIterator();
	
	    while(it.hasNext()) {				//added all the lists of the parents
	    LinkedList<String> parSighn =new LinkedList<String>();
	   	ListIterator<String> iter =parSighn.listIterator();
	   	Node par= it.next();
	   	ListIterator<String> iterPar= par.getCurrVar().listIterator();
	    	while(iterPar.hasNext()) {
	    	iter.add(par.getName() + " " +iterPar.next());	
	    	}
	    	
	    	list.add(parSighn);				//it.next().getCurrVar());
		}
		
		///update the CPT
		this.cpt.setVarPar(list);
	}
	/**
	 * take the event and enter them to the CPT
	 * @param s
	 */
	public void addProb(String s) {							//divide the string into three parts
		String co = s.split("=")[0];		//extract the substring that contain the condition
		StringTokenizer ou = new StringTokenizer(co ,",");
		String condition="";				//only the condition of the condition
		while(ou.hasMoreTokens()) {
			condition+= ou.nextToken()+ " ";
		}
		String part = s.substring(1+co.length());		//create array with all the depended variables
		String str = part.replaceAll("[^A-Za-z]"," ");
		String [] str_var = str.split(" ");
		LinkedList<String> link = new LinkedList<String>();
		int loc1= 0;
		
		while(loc1 < str_var.length) {
			if(!str_var[loc1].equals("")) {
				link.add(str_var[loc1]);
			}
			++loc1;
		}
		String str_var_new [] = new String[link.size()];
		ListIterator<String> iter = link.listIterator();
		int col = 0;
		while(iter.hasNext()) {
			str_var_new[col++] = iter.next();
		}
		String[] numbers = s.replaceAll("[^0-9.]+", " ").trim().split(" ");		//extract all the numbers
		Double [] doub = new Double[numbers.length];
		int loc= 0;
		for(String it : numbers) 
			doub[loc++] = Double.parseDouble(it);
		this.cpt.setProb(condition, str_var_new, doub);
		
	}
	public LinkedList<String> getCurrVar(){
		return this.currVar;
	}
	public CPT getCpt() {
		return this.cpt;
	}
	public String getName() {
		return this.name;
	}
	public LinkedList<Node> getParents(){
		return this.parents;
	}
	

}
