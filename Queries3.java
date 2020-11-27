package ex0;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

public class Queries3 extends Queries2{
	
	private LinkedList<CptNodes> list;		//holds all the cpt as CptNodes
	private int numOfmultiplication = 0;
	
	public Queries3(Network net) {
		super(net);
	}
	
	@Override
	public String prob(String s) {
		
		Double prob= 0.0; 
		String state= s.replaceAll("[^A-Za-z]"," ").split(" ")[2];	//the state of the query
		String node= s.replaceAll("[^A-Za-z]"," ").split(" ")[1];	//the node of the query
		
		//check if this condition exist - I assume that have valid input
		
		if(getExistProbability(this.net.getNode(node).getCpt(), s) != -1)		
				return getExistProbability(this.net.getNode(node).getCpt(), s) +","+ numOfAdd+ "," + numOfMul; 	//if exist simply return her from the cpt
		
		
		// get all the hidden variables organize according to ABC ascii and all their CPT
		
		LinkedList<Node> hidden = getHiddenVariables(s);	
		HashMap<Node,CPT> cpt = getCptHidden(hidden);			//return all the cpt that contain hidden variable an return them convert according to the abc
		HashMap<Node, String> evidence = new HashMap<Node, String>();
		evidence= extractEvidence(s);
		
		// check if one of the nodes isn't ancsetor of the query or not contain at the evidence
		
		String []	QueryEvidence =  s.replaceAll("[^A-Za-z]"," ").split(" ");	//and "p" at the first index
		HashSet<Node> QuerAndEv = new HashSet<Node>();
		
		for(int i=1 ; i < QueryEvidence.length ; i+=2) {
			QuerAndEv.add(this.net.getNode(QueryEvidence[i]));
		}	
		LinkedList<Node> QueryAncestors = evidenceAndQueryAncestors(QuerAndEv);		//use at hashset
		
		LinkedList<Node> removeNode = new LinkedList<Node>();
			for(Node it : cpt.keySet()) {
				if(!QueryAncestors.contains(it))
							removeNode.add(it);
			}
			
		ListIterator<Node> iterDel = removeNode.listIterator();
		while(iterDel.hasNext()) {
			Node l1 = iterDel.next();
			cpt.remove(l1);					//remove all the independent nodes
			hidden.remove(l1);
		}
		
		// remove all the evidence state
		

		LinkedList<CPT> organize= new LinkedList<CPT>(removeEvidence(cpt ,evidence));		// all the convert cpt without evidence variable and independent variables 
		
		this.list = convToCptNode(organize);
		
		
		// return the best order to do elimination
		
		LinkedList<Node> optimalOrder = getOptimalPermutationOfNodes(hidden);
		LinkedList<LinkedList<CPT>> listOflistCpt =	CreateListOFlistsCpt(optimalOrder, organize);   //we can get empty list of list
		
		// join and margin 

		
		ListIterator<LinkedList<CPT>> iterlistOFlist = listOflistCpt.listIterator();
		ListIterator<Node> iterHidden = optimalOrder.listIterator();
		CPT helper = null;
					
		while(iterlistOFlist.hasNext()) {
						
			LinkedList<CPT> innerList = iterlistOFlist.next();
					
			helper = MinMultiplicaionMediatorMain(innerList , helper);

			Node nodeNext = iterHidden.next();
					
			helper = MarginCpt(helper, nodeNext);		
		}
				
					
		//check if the query variable depend on one of the evidence variable like that p(A=true | A=true ...) and check if this query depend only on evidence nodes
						
			if(!cpt.keySet().contains(this.net.getNode(node))) {
							
				if(helper == null) {		//if the result is null of all the previous calculation
								
								
			helper = removeEvidenceMediator(convertCpt(this.net.getNode(node).cpt) , evidence);
					if(helper == null)
						return 1+ "," +numOfAdd +"," + numOfMul; 
				}
							
				else {
			
					CPT CleanCpt =removeEvidenceMediator(convertCpt(this.net.getNode(node).cpt) , evidence);
					helper =  joinCpt(helper , CleanCpt);
					if(helper.depth == 2)
							return 1+ "," +numOfAdd +"," + numOfMul; 
							}
							}
							
					if(cpt.keySet().contains(this.net.getNode(node)) && helper == null) {
									return 1+ "," +numOfAdd +"," + numOfMul; 
					}
							
							
		// take the probability of the state and normalize her
						
		Double  numerator = 0.0;
		Double denominator = 0.0;
     	int counter= 0;
					
		for(int i =1 ; i < helper.depth ; i++) {
			String in = (String)helper.mat[i][0];
				if(isContainState(in ,node + " " + state) ){
						numerator = (Double)helper.mat[i][1];
					}
				if(counter != 0) {
					numOfAdd++;
								}
				denominator+= (Double)helper.mat[i][1];
				counter++;
					}
					
			String cons = String.valueOf(numerator/denominator); 
			String rem ="," +numOfAdd +"," + numOfMul; 
						
			if(cons.length() > 7) 	//return only five digits after decimal point
					return cons.substring(0 , 7) + rem;
			return 	cons +rem;
					}
				
	
//	public LinkedList<Node> optimalOrderList(LinkedList<LinkedList<Node>> listOfList){
//		
//		LinkedList<Node> optimal = new LinkedList<Node>();
//		
//		
//		return optimal;
//	}
//	
	/**
	 * do join and margin for all the list of list of simple order
	 * @param list
	 * @return
	 */
//	public int numJoinMarg(LinkedList<LinkedList<CptNodes>> list) {
//		int operation = 0;
//		
//		
//		
//		return operation;
//	}
	/**
	 * get list of cpt and return join cpt
	 * @param list
	 * @return
	 */
//	public CptNodes joinList(LinkedList<CptNodes> list) {
//		
//		
//	}
	
	public LinkedList<CptNodes> joinCoupleMinMultiplicationCpt(LinkedList<CptNodes> list) {
		
		CptNodes arrA1 [] = new CptNodes[list.size()]; 
		ListIterator<CptNodes> iterCpt = list.listIterator();
		
		for(int i = 0; i < list.size() ; i++) {
			CptNodes curr= iterCpt.next();
			arrA1[i] =  curr;
		}
		
		int min= Integer.MAX_VALUE;
		int A1 = 0 ;
		int A2 = 0;
	
		for (int i = 0; i < arrA1.length; i++) {
			for (int j = 0; j < arrA1.length; j++) {
				if(CptNodes.NumOFmultipleCoupleCpt(arrA1[i] , arrA1[j]) < min && i != j) {		
					min = CptNodes.NumOFmultipleCoupleCpt(arrA1[i] , arrA1[j]);
					A1 = i;
					A2 = j;
				}
			}
		}
		
		LinkedList<CptNodes> opt = new LinkedList<CptNodes>();
		opt.add(arrA1[A1]);
		opt.add(arrA1[A2]);
		
		return opt;
	}
	
	public CptNodes MinMultiplicaionMediatorMain(LinkedList<CptNodes> list , CptNodes margin) {	
		
		CptNodes join = new CptNodes(this.net);
		
		if(list.isEmpty())	
			return margin;
		
		if(margin != null) {
		list.add(margin);
		}
		
		if(list.size() != 1) {
		int counter = 0;
		LinkedList<CptNodes> curr= list;
			while(counter < list.size()) {
				LinkedList<CptNodes> get = joinCoupleMinMultiplicationCpt(curr);
				curr.removeAll(get);
				ListIterator<CptNodes> iter= get.listIterator();
				CptNodes a = iter.next();
				CptNodes b = iter.next();
				join = new CptNodes(this.net , a, b);		//create new cpt
				this.numOfmultiplication += CptNodes.NumOFmultipleCoupleCpt(a, b);			//update the number of line
				curr.add(join);
				counter++;
			}
		}
		
	else {
		join = list.getFirst();
	}
		return join;
	}
	
	/**
	 * get list of order nodes
	 * and create list of lists of cpt
	 * @param list
	 * @return the minimum number of line that create
	 */
	public int numOfmultiplicationList(LinkedList<LinkedList<CptNodes>> list , LinkedList<Node> orderElimination) {		//get list with the order of the elimination and return the number of line that create
		
		ListIterator<LinkedList<CptNodes>> iterlistOFlist = list.listIterator();
		ListIterator<Node> iterHidden = orderElimination.listIterator();
		
		CptNodes helper = null;
		
		while(iterlistOFlist.hasNext()) {
			
			LinkedList<CptNodes> innerList = iterlistOFlist.next();
		
			helper = MinMultiplicaionMediatorMain(innerList , helper);

			Node nodeNext = iterHidden.next();
		
			helper.removeNode(nodeNext);		
		}
		
		
		return this.numOfmultiplication;
		
	}
	
	/**
	 * create list of list of all the permutation of all the variable
	 * @param hiddenVar
	 * @return	the optimal organization of multiplication
	 */
	public LinkedList<Node> getOptimalPermutationOfNodes(LinkedList<Node> hiddenVar){
		
		ListIterator<Node> iterNode = hiddenVar.listIterator();		//get all hidden nodes
		String nodeStr = "";
		
		while(iterNode.hasNext()) {
			nodeStr += iterNode.next().getName();	//create string from the names of the hidden nodes
		}
		
		LinkedList<String> allPermutationString = permute(new LinkedList<String>() ,nodeStr, 0, nodeStr.length()-1);	//create all the permutation of the string
		ListIterator<String> iterAllpermutationString = allPermutationString.listIterator();
		
		LinkedList<Node> optimalOrder = new LinkedList<Node>();
		
		int num = Integer.MAX_VALUE;
		
		//convert the permutations strings to list of list of nodes
		
		while(iterAllpermutationString.hasNext()) {
			this.numOfmultiplication = 0; 
			LinkedList<Node> list = generateListfromString(iterAllpermutationString.next());	//order of the elimination of the nodes
			LinkedList<LinkedList<CptNodes>> cptList = createlistOfList(list);	//list of list of the cpt
			
			if(numOfmultiplicationList(cptList , list) < num)
					optimalOrder = list;
		}
		
		return optimalOrder;
	}
	
	public LinkedList<LinkedList<CptNodes>> createlistOfList(LinkedList<Node> list){
			
		LinkedList<CptNodes> cp = this.list;
		ListIterator<CptNodes> iterConv = cp.listIterator();		//create iterators for the cpt
		
		//create list of lists according to the order of the elimination
		
		ListIterator<Node> iterHidden = list.listIterator();		//create iterator for the nodes
	
		LinkedList<LinkedList<CptNodes>> listOflistCpt =new LinkedList<LinkedList<CptNodes>>();
		
		LinkedList<CptNodes> allKeep  = new LinkedList<CptNodes>();
		
		while(iterHidden.hasNext()) {
			Node node = iterHidden.next();	//get hidden node
			
			LinkedList<CptNodes> temporary = new LinkedList<CptNodes>();
			
			while(iterConv.hasNext()) {		//move on all the cpt 
				CptNodes temp = iterConv.next();
					
					if(temp.isContain(node) && !allKeep.contains(temp)) {
						temporary.add(temp);
						allKeep.add(temp);
				}
			}
					listOflistCpt.add(temporary);
					iterConv = cp.listIterator();		//reset to convertion iterator
			}
				
		
		return listOflistCpt;
	}
	/**
	 * the function get string and list by the characteres of the string 
	 */
	public LinkedList<Node> generateListfromString(String strNode){
		
		LinkedList<Node> list = new LinkedList<Node>();
		String [] split = strNode.split("");
		
		for(int i= 0 ;i < strNode.length() ; i++) {
			list.add(this.net.getNode(split[i]));
		}		
		return list;
	}
	
	 private LinkedList<String> permute(LinkedList<String> list ,String str, int l, int r) 
	    { 
	        if (l == r) {
	        	list.add(str);
	         return list;
	        }
	     
	           for (int i = l; i <= r; i++) 
	            { 
	                str = swap(str,l,i); 
	                permute(list ,str, l+1, r); 
	                str = swap(str,l,i); 
	            } 
	        
	        return list;
	    }
	 
	 public String swap(String a, int i, int j) 
	    { 
	        char temp; 
	        char[] charArray = a.toCharArray(); 
	        temp = charArray[i] ; 
	        charArray[i] = charArray[j]; 
	        charArray[j] = temp; 
	        return String.valueOf(charArray); 
	    } 
	  
	 public LinkedList<CptNodes> convToCptNode(LinkedList<CPT> list) {
			
			LinkedList<CptNodes> convlist = new LinkedList<CptNodes>();
			ListIterator<CPT> iterCp = list.listIterator();
			
			while(iterCp.hasNext()) {
			CPT next = iterCp.next();
			convlist.add(new CptNodes(next, this.net));	
				
			}

			return convlist;
		}
			

}