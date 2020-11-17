package ex0;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class Queries {
	Network net;
	
	public Queries(Network net) {
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
	
	// P(B=true|J=true,M=true),1
	public Double parseProb(String var) {
		Double prob= 0.0;
		int algoNum = Integer.parseInt(var.replaceAll("[^0-9]", ""));		//the number of the algo to execute
		//extract the name of the node
		String strVar = var.replaceAll("[^A-Za-z]"," ");
		String name = strVar.split(" ")[1];  //return only the name
		String depend = strVar.split(" ")[1] +" " +strVar.split(" ")[2];
		Node node = this.net.getNode(name);		//the main node
		String [] spl = strVar.split(" ");
		LinkedList<String> ll = new LinkedList<String>();
		for(int i= 3 ; i< spl.length ; i+=2) {
			ll.add(spl[i]);
		}
		LinkedList<Node> lackNode= new LinkedList<Node>();
		lackNode = lackNode(ll);
		LinkedList<String> resLack = new LinkedList<String>();
		resLack = generatePerNode(lackNode);
		String s= "";
		for(int j =3 ; j<spl.length ; j++) {
			s+= spl[j]+" ";
		}
		ListIterator<String> iterable= resLack.listIterator();
		LinkedList<String> permutation = new LinkedList<String>();		////the res of all the permutation that need to calculate
		ListIterator<String> list = permutation.listIterator();
		while(iterable.hasNext()) {
			list.add(iterable.next()+ s);
		}
		LinkedList<String> numerator = new LinkedList<String>();
		ListIterator<String> iteration = permutation.listIterator();
		ListIterator<String> insert = numerator.listIterator();

		while(iteration.hasNext()) {
			String str= iteration.next();
			if(str.contains(depend)) {
				insert.add(str);
			}
		}
		LinkedList<String> denominator = new LinkedList<String>();
		iteration = permutation.listIterator();
		while(iteration.hasNext()) {
			String dep= iteration.next();
			if(!dep.contains(depend)) {
				denominator.add(dep);
			}
		}																//we got the denominator and numerator of the equation
		Double deno=calCond(denominator); 
		Double nemo=calCond(numerator);
		return nemo/(deno+nemo);
		
		
	}
	/**
	 *
	 * @param node - get the node
	 * @return linkedlist with the name of the node and his variables
	 * at every index of the linkedlist we get name + one variable
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
	
	public Double findParent(String node, String state ,String [] cond) {			//??????????
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
		worth+= it.next()+ " ";			
		}		
			int dep = child.cpt.depth;
			if(worth.equals("")) {
				for(int j=1 ; j < child.cpt.varCurr.size()+1 ; j++) {
					if(child.cpt.mat[0][j].equals(state)) {
						prob =(Double)(child.cpt.mat[1][j]);		//double
					}
			}
			}
			for(int i=1 ; i < dep ; i++) {
				if(child.cpt.mat[i][0].equals(worth)) {
				for(int j=1 ; j < child.cpt.varCurr.size()+1 ; j++) {
					if(child.cpt.mat[0][j].equals(state)) {
						prob =(Double)(child.cpt.mat[i][j]);			//double
					}

		}
				}
		}
		return prob;
	}
	public Double calCond(LinkedList<String> str) {
		Double prob= 0.0;
		Double mul=1.0;
		ListIterator<String> iter= str.listIterator();
		while(iter.hasNext()){
		String s= iter.next();
		String [] split= s.split(" ");
		for(int i= 0 ; i< split.length ; i+=2) {
		mul *= findParent(split[i],split[i+1] , split);	
		}
		prob+=mul;
		mul=1.0;
		}
		return prob;
	}
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
//	public String toString() {
//		String s ="";	
//		ListIterator<String> iter = this.str.listIterator();
//			while(iter.hasNext()) {
//				s+= iter.next();
//				s+= "\n";
//			}
//			return s;
//	}
}
