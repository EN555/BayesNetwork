package ex0;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class Queries1 {
	Network net;
	private int numOfMul=0;
	private int numOfAdd=0;
	
	
	public Queries1(Network net) {
		this.net = net;
	}
	public LinkedList<String> generatePermutations(LinkedList<LinkedList<String>> lists, LinkedList<String> result, int depth, String current) {
	    if (depth == lists.size()) {
	        result.add(current);
	        return result;
	    }
	    
	    for (int i = 0; i < lists.get(depth).size(); i++) {
	        generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i)+ " ");
	    }
	    return result;
	}
	
	/**
	 * function get String like that P(B=true|J=true,M=true),1
	 * @param var
	 * @return their probability using bayes rules
	 */
	public String parseProb(String var) {
		
		Double prob= 0.0;	
		String strVar = var.replaceAll("[^A-Za-z]"," ");
		String name = strVar.split(" ")[1];  		//return only the name
		String query = strVar.split(" ")[1] +" " +strVar.split(" ")[2];
		Node node = this.net.getNode(name);			//the main node
		String [] spl = strVar.split(" ");
		
		
	//check if the query already exist
			
		String allQuery ="";			//mean P(A= ..  | B= ^ ^..) the condition it's from the (|)
		for(int i= 1 ; i< spl.length; i++) {
			if(i < spl.length) {
				allQuery+= spl[i]+ " ";
			}
		}
		
		allQuery = allQuery.substring(0, allQuery.length()-1);	//return -1 if he didn't find else return the probability	
		Double check = isExist(convertCpt(node.cpt), allQuery);
		if(check != -1) { 
			String res = String.valueOf(check);
			while(res.length() < 7) res += "0";
			return res+"," + numOfAdd + "," + numOfMul;
		}
		//collect all the hidden nodes to list
		
		LinkedList<String> dependNodeList = new LinkedList<String>();		//all the depended node
		for(int i= 3 ; i< spl.length ; i+=2) {
			dependNodeList.add(spl[i]);
		}
		
		LinkedList<Node> hiddenNode= new LinkedList<Node>();
		hiddenNode = lackNode(dependNodeList);						//return all the hidden nodes
		
		// create the permutation of all the hidden nodes
		
		LinkedList<String> resLack = new LinkedList<String>();		//enter too the query
		resLack = generatePerNode(hiddenNode);
		String evidence= "";				//create string that hold all the evidence variable
		for(int j =3 ; j<spl.length ; j++) {
			evidence+= spl[j]+" ";
		}				
		evidence = evidence.substring(0 , evidence.length()-1);
		ListIterator<String> iterable= resLack.listIterator();
		LinkedList<String> permutation = new LinkedList<String>();		//the res of all the permutation that need to calculate
		ListIterator<String> list = permutation.listIterator();
		while(iterable.hasNext()) {
			list.add(iterable.next()+ evidence);
		}
		
		//divide the permutation to two lists all that contains to the numerator and all the rest
		
		LinkedList<String> numerator = new LinkedList<String>();
		ListIterator<String> iteration = permutation.listIterator();
		ListIterator<String> insert = numerator.listIterator();

		while(iteration.hasNext()) {
			String str= iteration.next();
			if(str.contains(query)) {
				insert.add(str);
			}
		}
		LinkedList<String> denominator = new LinkedList<String>();
		iteration = permutation.listIterator();
		while(iteration.hasNext()) {
			String dep= iteration.next();
			if(!dep.contains(query)) {
				denominator.add(dep);
			}
		}																//we got the denominator and numerator of the equation
		Double deno=calCond(denominator); 
		Double nemo=calCond(numerator);
		Double result = nemo/(nemo+deno);
		String cal= String.valueOf(result);
		numOfAdd++;
		if(cal.length() >=7) {			//check the size of the return
			cal = cal.substring(0,7);
		}
		return cal +"," + numOfAdd + ","+ numOfMul;		//????
		
		
	}
	/**
	 * check if this query already exist in the cpt
	 * @param cpt convert cpt
	 * @param query
	 * @return if it's exist it's return the probability else return -1
	 */
	public Double isExist(CPT cpt, String query) {		
		Double ex = -1.0;
		int size= 0;
		
		String splitQuery [] = query.split(" ");
		LinkedList<String> list = new LinkedList<String>();
		for(int i=0 ; i< splitQuery.length ; i+=2) {
			list.add(splitQuery[i] + " " + splitQuery[i+1]);
		}
		
		ListIterator<String> iterList = list.listIterator();
	
		for(int i =1 ; i < cpt.depth && ex == -1.0 ;i++) {
			while(iterList.hasNext()) {
				if(((String)cpt.mat[i][0]).contains(iterList.next())){
					size++;
				}
			}
			if(size == list.size()) {
				ex = (Double)cpt.mat[i][1];
			}
			size= 0;
			iterList = list.listIterator();
		}
		return ex;
	}
/**
 * organise the cond (e.g ...| B go A stay) according to the parents 
 * and return A stay B go 
 * @return
 */
	public Double findNum(Node node ,String dep ,String cond) { //dep = C run | cond = A go B stay 
		 Double num = 0.0;
		 LinkedList<Node> parents = node.getParents();
		 String str ="";
		 ListIterator<Node> iter = parents.listIterator();
		 String [] condition = cond.split(" ");
		 String spec="";
		 
		 while(iter.hasNext()) {		
			 String name = iter.next().getName();
			 for(int v=0 ; v <condition.length ; v++) {
				 if(condition[v].equals(name)) {
				 spec+= name+ " "+ condition[v+1]+" ";
				 }
			 }
		 }
		 String state = dep.split(" ")[1];
		 if(node.getCpt().depth == 2) {
			 return -1.0;
		 }
		 else {
		 for(int i =1 ; i< node.getCpt().depth; i++) {
			if(contain((String)node.cpt.mat[i][0], spec)) {
			for(int j =1; j <node.getCurrVar().size()+1 ; j++) {
				if(((String)node.cpt.mat[0][j]).equals(state)) {
					return (Double)node.cpt.mat[i][j];
					}
				}			
			}
		}
		 }
		return -1.0;			//if not found
	}
	/**
	 * you give all the lack node and it return list with all their permutation
	 */
	public LinkedList<String> generatePerNode(LinkedList<Node> list){			//return all the permutation of lack nodes
		
		LinkedList<LinkedList<String>> str  =new LinkedList<LinkedList<String>>();			//to restore all the permutation per node
		ListIterator<LinkedList<String>> iter = str.listIterator();
		ListIterator<Node> it = list.listIterator();
		while(it.hasNext()) {
			Node ll =it.next();
			LinkedList<String> currVar = ll.getCurrVar();			//all the variables of the node
			ListIterator<String> iterVar = currVar.listIterator();
			LinkedList<String> Name_Var = new LinkedList<String>();
			while(iterVar.hasNext()) {
				Name_Var.add(ll.name + " " + iterVar.next());
			}
			str.add(Name_Var);
			
		}
		LinkedList<String> res = new LinkedList<String>();
		res = generatePermutations(str, res, 0, "");
	
		return res;			//linked list with all the permutation of the lack nodes
	}
	/**
	 *
	 * @param node - get the node
	 * @return linkedlist with the name of the node and his variables
	 * at every index of the linkedlist we get name + one variable
	 */
	public Double findParent(String node, String state ,String [] cond) {		
		Double prob = 1.0;
		LinkedList<Node> parents = new LinkedList<Node>();
		Node child =this.net.getNode(node);
		parents = child.getParents();
		ListIterator<Node> iter= parents.listIterator();
		LinkedList<String> proba =new LinkedList<String>();
		while(iter.hasNext()) {
		String par = iter.next().getName();	
		for(int i=0; i<cond.length ; i++) {
			if(cond[i].equals(par)) {
			proba.add(par +" "+ cond[i+1]);
			}
			}		
		}
		ListIterator<String> it =proba.listIterator();
		String worth ="";
		while(it.hasNext()) {
		worth+= it.next()+ " ";				//worth mean at who he depended		
		}
		if(!worth.equals("")) {
		worth = worth.substring(0 , worth.length()-1);
		}
			int dep = child.cpt.depth;
			if(worth.equals("")) {
				for(int j=1 ; j < child.cpt.varCurr.size()+1 ; j++) {
					if(child.cpt.mat[0][j].equals(state)) {
						prob =(Double)(child.cpt.mat[1][j]);		//double
					}
			}
			}
			else {
			for(int i=1 ; i < dep ; i++) {
					if(contain((String)child.cpt.mat[i][0] ,worth )) {
				for(int j=1 ; j < child.cpt.varCurr.size()+1 ; j++) {
					if(child.cpt.mat[0][j].equals(state)) {
						prob =(Double)(child.cpt.mat[i][j]);			//double
					}
		}
				}
					}
		}
			if(prob== 0.0)			//check if we got the probability of 0
				return 1.0;
		return prob;
	}
	
	public boolean contain(String par , String check) {
		boolean in=true;
		if(check.equals(""))
			return false;
		String sp [] = check.split(" ");
		for(int i=0; i < sp.length ; i+=2) {
		if(!par.contains(sp[i] +" " + sp[i+1])) 
				in=false;
		}
		return in;
	}
	public Double calCond(LinkedList<String> str) {
		Double prob= 0.0;
		Double mul=1.0;
		ListIterator<String> iter= str.listIterator();
		int count = 0;
		while(iter.hasNext()){
		String s= iter.next();
		String [] split= s.split(" ");
		for(int i= 0 ; i< split.length ; i+=2) {
		if(i==0) {
			mul = findParent(split[i],split[i+1] , split);			//at the first time it's only put it in without multiplication
		}
		else {
			mul *= findParent(split[i],split[i+1] , split);
			numOfMul++;
		}
		}
		if(count == 0) {											//if it's the first time only put it without to use at add operation
			prob = mul;
		}
		else {
			prob +=mul;
			numOfAdd++;
		}
		mul=1.0;
		count++;
		}
		return prob;
	}
	/**
	 * given list with all the exist node and return all the lack node
	 * @param exist
	 * @return
	 */
	public LinkedList<Node> lackNode(LinkedList<String> exist){			//look for all the nodes that didn't added them
		LinkedList<Node> lack = new LinkedList<Node>(); 
		LinkedList<Node> nodes = new LinkedList<Node>(this.net.getNodeCollection());
		ListIterator<Node> iterAll = nodes.listIterator();
		while(iterAll.hasNext()) {
			Node check = iterAll.next();
			if(!exist.contains(check.name)) {
				lack.add(check);
			}
		}
		return lack;
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
				if((String)cpt.mat[0][var]==null) {
					convCpt.mat[counter][0] =(String)cpt.mat[i][0];
				}
				else {
					String s= (String)cpt.mat[i][0];
					if(s.substring(s.length()-1 ,s.length()).equals(" ")) {
						convCpt.mat[counter][0] =(String)cpt.mat[i][0]+  cpt.curr.getName() +" "+ (String)cpt.mat[0][var] ;
					}
					else {
						convCpt.mat[counter][0] =(String)cpt.mat[i][0]+ " "+ cpt.curr.getName() +" "+ (String)cpt.mat[0][var] ;
					}
					}
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

}
