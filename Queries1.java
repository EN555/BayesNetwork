package ex0;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Queries1 {
	
	Network net;
	private int numOfMul=0;
	private int numOfAdd=0;
	
	
	public Queries1(Network net) {
		this.net = net;
	}
	
	/**
	 * 
	 * @param lists - get list of list of strings and return all the combination in list
	 * @param result - all the permutation of the list of lists
	 * @param depth
	 * @param current
	 * @return
	 */
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
		
		String strVar = var.replaceAll("[^A-Za-z]"," ");
		String name = strVar.split(" ")[1];  		//return only the name
		String query = strVar.split(" ")[1] +" " +strVar.split(" ")[2];
		Node node = this.net.getNode(name);			//the main node
		String [] spl = strVar.split(" ");
		
		
	//check if the query already exist
			
		String allQuery ="";			//mean P(A= ..  | B= ^ ^..) 
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
		hiddenNode = lackNode(dependNodeList);						//return all the hidden nodes with the query node
		
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
		while(iterable.hasNext()) {							//add the evidence parameters
			list.add(iterable.next()+ evidence);
		}
		
		//divide the permutation to two lists all that contains to the numerator and all the rest
		
		LinkedList<String> numerator = new LinkedList<String>();
		LinkedList<String> denominator = new LinkedList<String>();
		ListIterator<String> iteration = permutation.listIterator();
		ListIterator<String> insert = numerator.listIterator();

		//insert all the n-tuples that contain the query to the list
		
		while(iteration.hasNext()) {	
			String str= iteration.next();
			if(str.contains(query)) {
				insert.add(str);
			}
		}
	
		//insert all the n-tuples that not contain the query
		
		iteration = permutation.listIterator();
		while(iteration.hasNext()) {
			String dep= iteration.next();
			if(!dep.contains(query)) {
				denominator.add(dep);
			}
		}				
		
		//we got the denominator and numerator of the equation and we need to normalize the numerator with the sum of the numerator and denominator 
	
		Double deno=calCond(denominator); 
		Double nemo=calCond(numerator);
		Double result = nemo/(nemo+deno);
		numOfAdd++;			//add one addition for the normalize operation
		
		return String.format("%.5f", result) +"," + numOfAdd + ","+ numOfMul;		
		
		
	}
	/**
	 * check if this query already exist in the cpt
	 * @param cpt - convert cpt
	 * @param query
	 * @return if it's exist it's return the probability else return -1
	 */
	public Double isExist(CPT cpt, String query) {		// String query mean A true B go C stay .. (e.g. 2-tuple <node , state>)
		
		Double probability = -1.0;
		int size= 0;
		
		//create linked list of 2-tuple <node , state>
		
		String splitQuery [] = query.split(" ");		
		LinkedList<String> list = new LinkedList<String>();
		for(int i=0 ; i< splitQuery.length ; i+=2) {
			list.add(splitQuery[i] + " " + splitQuery[i+1]);
		}

		ListIterator<String> iterList = list.listIterator();
		
		//search it in the convert cpt 
		
		for(int i =1 ; i < cpt.depth && probability == -1.0 ;i++) {
			
			while(iterList.hasNext()) {
			
				if(((String)cpt.mat[i][0]).contains(iterList.next())){
					size++;
				}
			}
			
			if(size == list.size()) {		//it's represent if all the 2-tuples contain at this row
				probability = (Double)cpt.mat[i][1];
			}
			size= 0;						//reset the size
			iterList = list.listIterator();
		}
		return probability;
	}

	/**
	 * you give all the lack node and it return list with all their permutation as 2-tuple of <node , state>
	 */
	public LinkedList<String> generatePerNode(LinkedList<Node> list){			//return all the permutation of lack nodes
		
		LinkedList<LinkedList<String>> str  =new LinkedList<LinkedList<String>>();			//to restore all the permutation per node
	
		ListIterator<LinkedList<String>> iter = str.listIterator();
	
		ListIterator<Node> it = list.listIterator();
	
		while(it.hasNext()) {	//create list of list that every list contain node and all his variables as 2-tuples
			
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
		res = generatePermutations(str, res, 0, "");	//generate the permutation of all the nodes and their states
	
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
		
		parents = child.getParents();			//get the parents of the node
		
		ListIterator<Node> iter= parents.listIterator();
		
		LinkedList<String> proba =new LinkedList<String>();		//keep the parent and state of the specific node
		
		//move on all the condition and look for the parents of the specific node and keep him and his state at linked list
		
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
	
		//move on all his parent and his parents and look for the his probability at the cpt
		
		while(it.hasNext()) {
			
			worth+= it.next()+ " ";				//worth mean at who he depended (e.g. A go B stay C ...)		
			}	
		if(!worth.equals("")) {
			
			worth = worth.substring(0 , worth.length()-1);
			
		}
		
		int dep = child.cpt.depth;
		
		//divide to two cases if he have parents or not
		
		if(worth.equals("")) {		//havn't parent
			
			for(int j=1 ; j < child.cpt.varCurr.size()+1 ; j++) {
				
				if(child.cpt.mat[0][j].equals(state)) {			
					prob =(Double)(child.cpt.mat[1][j]);		//it's only in 1 row because that he havn't parents
					}
			}
			}
		else {		//have parents
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
				
			if(prob== 0)			//check if we got the probability of 0
				return 1.0;
			
		return prob;
	}
	
	/**
	 * 
	 * @param par - string of one of the cpt states (e.g. P(A|B^C^D)
	 * @param check - string of the parent that need to check if they exist at specific row 
	 * @return
	 */
	public boolean contain(String par , String check) {
		
		boolean in=true;		//initial that the check is in 
		
		if(check.equals(""))	//havn't parents
			return false;
		
		//if have her parents
		
		String sp [] = check.split(" ");
		
		for(int i=0; i < sp.length ; i+=2) {
			if(!par.contains(sp[i] +" " + sp[i+1])) 
				in=false;
		}
		
		return in;
	}
	
	/**
	 * @param get linked list with all the string that need to calculate 
	 * mean p(A)*p(B|A^B)*...p(C) + p(A)*p(B|!A^B)*...p(!C)*... + ...
	 * @return the probability of the query
	 */
	public Double calCond(LinkedList<String> str) {
		
		Double prob= 0.0;			// sum the the probability of all the equation (e.g. p(A)*p(B|A^B)*...p(C) + p(A)*p(B|!A^B)*...p(!C)*... + ... )
		
		Double mul=1.0;				//sum the the probability of simple equation (e.g. p(A)*p(B|A^B)*...p(C))
		
		ListIterator<String> iter= str.listIterator();
		int count = 0;
		
		//move on all the equations
		
		while(iter.hasNext()){		
			
			String equation= iter.next();
			String [] split= equation.split(" ");
				
			for(int i= 0 ; i< split.length ; i+=2) {		//move each char (e.g. p(A).. P(B|C) ) and look for her parents
					
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
		return 	prob;
	}
	
	/**
	 * given list with all the exist node and return all the lack node
	 * @param exist
	 * @return
	 */
	public LinkedList<Node> lackNode(LinkedList<String> exist){			//look for all the nodes that didn't added them
		
		LinkedList<Node> lack = new LinkedList<Node>(); 
		
		LinkedList<Node> nodes = new LinkedList<Node>(this.net.getNodeCollection());		//the current nodes
		
		ListIterator<Node> iterAll = nodes.listIterator();
	
		while(iterAll.hasNext()) {		//check if the node exist in list of all the nodes of the net
			Node check = iterAll.next();
				if(!exist.contains(check.name)) {
					lack.add(check);
			}
		}
		
		return lack;
	}
	
	/**
	 * take regular cpt and represent him as matrix of n*2
	 * first column represent combination of variable of nodes as n-tuples and the second column represent the probability 
	 */
	public CPT convertCpt(CPT cpt) {
	
		CPT convCpt = new CPT();
		int dep = (cpt.depth-1)*cpt.varCurr.size()+1;		//calculate the depth of the new cpt 
		convCpt.mat = new Object[dep][2];
	
		//first option is that the matrix have only 2 rows (e.g the minimum rows of cpt)
		
		if(cpt.depth==2) {
			
			//update at the new cpt all the parameters
			
			convCpt.curr = cpt.curr;
			convCpt.depth = dep;
			convCpt.varPar = cpt.varPar;
			convCpt.varCurr = cpt.varCurr;
			convCpt.width=2;
			
			for(int i =1 ; i< dep ; i++) {		//copy all the variables of the current cpt
				convCpt.mat[i][0] = cpt.curr.getName() + " " + (String)cpt.mat[0][i];
			}
			
			for(int i =1 ; i< dep ; i++) {		//copy all the probability of the previous cpt  
				convCpt.mat[i][1] = cpt.mat[1][i];
			}
		}
		
		//the second option (e.g. the depth of the cpt greater than 2)
		
		else {
			
			convCpt.curr = cpt.curr;
			convCpt.depth = dep;
			convCpt.varPar = cpt.varPar;
			convCpt.varCurr = cpt.varCurr;
			convCpt.width=2;
			
			int num= cpt.varCurr.size();
			int var = 1; 
			int counter=1;
		
			while((1+num) >var) {			//add 1 to num because that the cpt have an empty cell in mat[0][0] = null
				
				//create all the combination of the specific node (e.g. create all the rows of the new matrix)
				
				for(int i =1 ; i< cpt.depth ; i++) {			
					if((String)cpt.mat[0][var]==null) {				//check if have only one column in the matrix 
						convCpt.mat[counter][0] =(String)cpt.mat[i][0];
				}		
					else {
						
						//check if have an empty space 
						
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
			
			//copy all the probabilities to the new matrix
			
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
