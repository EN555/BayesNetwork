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
	public LinkedList<Node> parents;
	public LinkedList<String> currVar;		//it's only the current variables
	CPT cpt;
	Network net;
	
	public Node(String name, Network net)
	{
		this.name = name;
		this.parents = new LinkedList<Node>();;
		this.currVar = new LinkedList<String>();;
		this.cpt = new CPT(this);		///
		this.net = net;
		
	}
		
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
	
	public void addPar(String parVar) {		///create the variables at lists
		if(parVar.contains("none")) {
		this.parents = new LinkedList<Node>();
		this.cpt.setVarPar(new LinkedList<LinkedList<String>>());
		return ;
		}
		this.parents = new LinkedList<Node>();
		StringTokenizer str = new StringTokenizer(parVar, ",// //:");
		str.nextToken();	//skip on "parents" word
		
		while(str.hasMoreTokens())
		{
			String vertex = str.nextToken();
			Node ver = this.net.getMap().get(vertex);
			this.parents.add(ver);
		}
		
		LinkedList<LinkedList<String>> list = new LinkedList<LinkedList<String>>();
	    ListIterator<Node> it = this.parents.listIterator();
		while(it.hasNext()) {				//added all the lists of the parents
			list.add(it.next().getCurrVar());
		}
		
		///update the CPT
		this.cpt.setVarPar(list);
	}
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



}
