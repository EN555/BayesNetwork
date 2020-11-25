package ex0;

import java.awt.List;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Queries2 {

	Network net;
	private int numOfMul=0;
	private int numOfAdd=0;
	
	
	public Queries2(Network net) {
		this.net = net;
	}
	public String prob(String s) {
		Double prob= 0.0; 
	String state= s.replaceAll("[^A-Za-z]"," ").split(" ")[2];	//the state of the query
	String str= s.replaceAll("[^A-Za-z]"," ").split(" ")[1];	//the node of the query
	
	if(getExistProbability(this.net.getNode(str).getCpt(), s) != -1)
			return getExistProbability(this.net.getNode(str).getCpt(), s) +","+ numOfAdd+ "," + numOfMul;
	
	String []	QueryEvidence =  s.replaceAll("[^A-Za-z]"," ").split(" ");
	LinkedList<Node> QuerEv = new LinkedList<Node>();
	for(int i=1 ; i < QueryEvidence.length ; i+=2) {
		QuerEv.add(this.net.getNode(QueryEvidence[i]));
	}
	
	//get all the hidden variables
	LinkedList<Node> hidden = getHiddenVariables(s);	
	HashMap<Node,CPT> cpt = getCptHidden(hidden);
	HashMap<Node, String> evidence = new HashMap<Node, String>();
	evidence= extractEvidence(s);
	
	//check if one of the nodes isn't ancsetor of the query or not contain at the evidence
	
	
	LinkedList<Node> QueryAncestors = evidenceAndQueryAncestors(QuerEv);
	
	LinkedList<Node> removeNode = new LinkedList<Node>();
		for(Node it : cpt.keySet()) {
			if(!QueryAncestors.contains(it))
						removeNode.add(it);
		}
	ListIterator<Node> iterDel = removeNode.listIterator();
	while(iterDel.hasNext()) {
		Node l1 = iterDel.next();
		cpt.remove(l1);
		hidden.remove(l1);
	}
		
	LinkedList<CPT> organize= new LinkedList<CPT>(removeEvidence(cpt ,evidence));		// all the cpt organized ABC Ascii
	
	LinkedList<Node> evidenceName = new LinkedList<Node>(evidence.keySet());	//all the evidence nodes
	
	ListIterator<CPT> iterAll = organize.listIterator();
	
	//////convert all the cpt/////
	LinkedList<CPT> organizeConv = new LinkedList<CPT>();
	while(iterAll.hasNext()) {
		organizeConv.add(convertCpt(iterAll.next()));	
	}
	
	ListIterator<CPT> iterConv =  organizeConv.listIterator();		//check if have cpt with one by one size
	LinkedList<LinkedList<CPT>> listOflistCpt =	CreateListOFlistsCpt(hidden, organizeConv);

	//////// join && marginilization ////////
	
	ListIterator<LinkedList<CPT>> iterlistOFlist = listOflistCpt.listIterator();
	ListIterator<Node> iterHidden = hidden.listIterator();
	CPT helper = null;
	
	while(iterlistOFlist.hasNext()) {
		
		LinkedList<CPT> innerList = iterlistOFlist.next();
		CPT res = MinMultiplicaionMediatorMain(innerList , helper);
		Node node = iterHidden.next();
		
		res = MarginCpt(res, node);
		
		if(res != null && res.depth > 2) {
			helper = res;
		}
		
	}	
		//need to multiple at the query and normalize
		Double  numerator = 0.0;
		Double denominator = 0.0;
		int counter= 0;
		if(!organize.contains(this.net.getNode(str).cpt)) {
		CPT last=  joinCpt(helper , convertCpt(this.net.getNode(str).cpt));			
		for(int i =1 ; i < last.depth ; i++) {
				if(((String)last.mat[i][0]).contains(state)){
					numerator = (Double)last.mat[i][1];
				}
				if(counter != 0) {
				numOfAdd++;
				}
				denominator+= (Double)last.mat[i][1];
				counter++;
			}
		}
			else {
				for(int i =1 ; i < helper.depth ; i++) {
					if(((String)helper.mat[i][0]).contains(state)){
						numerator = (Double)helper.mat[i][1];
						}
					if(counter != 0) {
						numOfAdd++;
					}
						denominator+= (Double)helper.mat[i][1];
						counter++;
			}
		}
	
		String res = String.valueOf(numerator/denominator); 
		String rem ="," +numOfAdd +"," + numOfMul; 
		
		if(res.length() > 7) 	//return only five digits after decimal point
			return res.substring(0 , 7) + rem;
		return 	res +rem;
	}
	
	/**
	 * 
	 * @param cpt - get cpt 
	 * @param str - get the query as is
	 * @return - if the probability exist return the probability else return -1
	 */
	public Double getExistProbability(CPT cpt , String str) {
		Double probability= -1.0;
		int size = 0;
		CPT cp = convertCpt(cpt);
		String []	allVar =  str.replaceAll("[^A-Za-z]"," ").split(" ");
		LinkedList<String> allVarList = new LinkedList<String>();
		
		for(int i=1 ; i < allVar.length ; i+=2) {
			allVarList.add(allVar[i]+ " " + allVar[i+1]);
		}
		
		ListIterator<String> iterAllVarList = allVarList.listIterator();
		
		for(int j=1; j < cp.depth && probability == -1 ; j++) {	
		
			while(iterAllVarList.hasNext()) {
				String temp= iterAllVarList.next();
				if(((String)cp.mat[j][0]).contains(temp)) {
					 size++;
				 }
			}
			
			iterAllVarList = allVarList.listIterator();	
			
			if(size ==  allVarList.size())
				probability = (Double)cp.mat[j][1];
			
			size=0;
		}
		
		return probability;
	}
	/**
	 * the function get all the evidence of function and the query of the function
	 * @return linkedlist of all the ancestors
	 */
	public LinkedList<Node> evidenceAndQueryAncestors(LinkedList<Node> nodes){
		ListIterator<Node> iterNodes = nodes.listIterator();
		LinkedList<Node> allAnces = new LinkedList<Node>();
		
		while(iterNodes.hasNext()) {
			Node node = iterNodes.next();
			LinkedList<Node> temp =	isAnsecstor(node, new LinkedList<Node>());
			if(!allAnces.contains(node)) {
			allAnces.add(node);
			}
			ListIterator<Node> iterable = temp.listIterator();
			while(iterable.hasNext()) {
				Node tp = iterable.next();
				if(!allAnces.contains(tp)) {
				allAnces.add(tp);
				}
			}
			iterable = temp.listIterator();
		}
		return  allAnces;
	}
	/**
	 * recursive function to find all the ancestors of node
	 * @param node
	 * @param list
	 * @return
	 */
	public  LinkedList<Node> isAnsecstor(Node node, LinkedList<Node> list) {
		if(node.parents.isEmpty()) { 
			return list;
		}
		ListIterator<Node> iter = node.parents.listIterator();
		while(iter.hasNext()) {
			Node temp = iter.next();
			list.add(temp);
			isAnsecstor(temp, list);
		}	
		return list;
	}
	
	/**
	 * method that get all the hidden nodes and all the convertion of the cpt 
	 * @param hidden - all the hidden nodes
	 * @param convCpt - all the convert cpt
	 * @return linkedlist of linkeslist organize according to the Ascii 
	 */
	public LinkedList<LinkedList<CPT>> CreateListOFlistsCpt(LinkedList<Node> hidden , LinkedList<CPT> convCpt){
	
		LinkedList<CPT> Conv = convCpt;
		ListIterator<CPT> iterConv = Conv.listIterator();		//create iterators for the input
		ListIterator<Node> iterHidden = hidden.listIterator();
	
		LinkedList<LinkedList<CPT>> listOflistCpt =new LinkedList<LinkedList<CPT>>();
		ListIterator<LinkedList<CPT>> iterListOfList = listOflistCpt.listIterator(); 
		
		LinkedList<CPT> allKeep  = new LinkedList<CPT>();
		
		while(iterHidden.hasNext()) {
			Node node = iterHidden.next();	//get hidden node
			
			LinkedList<CPT> temporary = new LinkedList<CPT>();
			
			while(iterConv.hasNext()) {		//move on all the cpt 
				CPT temp = iterConv.next();
					
					if(((String)temp.mat[1][0]).contains(node.getName()) && temp.depth > 2 && !allKeep.contains(temp)) {
						temporary.add(temp);
						allKeep.add(temp);
				}
			}
					listOflistCpt.add(temporary);
					iterConv = convCpt.listIterator();		//reset to convertion iter
			}
		return listOflistCpt;
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
	
	public Collection<CPT> removeEvidence(HashMap<Node,CPT> allHiddenCpt , HashMap<Node, String> evidence){
		
		HashMap<Node,CPT> cpt = allHiddenCpt;
		
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
					else {		//if the change in the inner column (e.g first column)
					CPT up = UpdateCptVariables(ver ,(Node)node.getKey(),(String)node.getValue());	 
					cpt.put(ver, up);
					}
				}
			}
		}
		
		return cpt.values();
	}
	public CPT MinMultiplicaionMediatorMain(LinkedList<CPT> list , CPT margin) {	
		
		CPT join = new CPT();
		
		if(list.isEmpty())
			return null;
		
		if(margin != null) {
		list.add(margin);
		}
		
		if(list.size() != 1) {
		int counter = 0;
		LinkedList<CPT> curr= list;
			while(counter < list.size()) {
				LinkedList<CPT> get = joinCoupleMinMultiplicaion(curr);
				curr.removeAll(get);
				ListIterator<CPT> iter= get.listIterator();
				join = joinCpt(iter.next(), iter.next());
				curr.add(join);
				counter++;
			}
		}
		
	else {
		ListIterator<CPT> iterable = list.listIterator();
		join = iterable.next();
	}
		return join;
	}
	public LinkedList<CPT> joinCoupleMinMultiplicaion(LinkedList<CPT> list) {
		
		CPT arrA1 [] = new CPT[list.size()]; 
		ListIterator<CPT> iterCpt = list.listIterator();
		
		for(int i = 0; i < list.size() ; i++) {
			CPT curr= iterCpt.next();
			arrA1[i] =  curr;
		}
		
		int min= Integer.MAX_VALUE;
		int A1 = 0 ;
		int A2 = 0;
		for (int i = 0; i < arrA1.length; i++) {
			for (int j = 0; j < arrA1.length; j++) {
				if(getNumOfMultiplications(arrA1[i] , arrA1[j]) < min && i != j) {		//first case the new couple lower than previous
					min = getNumOfMultiplications(arrA1[i] , arrA1[j]);
					A1 = i;
					A2 = j;
				}
				if((i != A1 || j != A2 ) && getNumOfMultiplications(arrA1[i] , arrA1[j]) == min && i != j) {				//second case the new couple worth to the previous
					
					if((calaculateAsciiOfCptVar(arrA1[i]) + calaculateAsciiOfCptVar(arrA1[j])) < (calaculateAsciiOfCptVar(arrA1[A1])+calaculateAsciiOfCptVar(arrA1[A2]))){
						A1 = i;
						A2 = j;
					}
			
				}
			}
		}
		
		LinkedList<CPT> opt = new LinkedList<CPT>();
		opt.add(arrA1[A1]);
		opt.add(arrA1[A2]);
		return opt;
	}
	
	public int calaculateAsciiOfCptVar(CPT a) {
		int Ascii= 0;
		String [] row= ((String)a.mat[1][0]).split(" ");
		for(int i=0 ; i < row.length ; i+=2) {
			Ascii += (int)row[i].charAt(0);		
		}
		return Ascii;
	}

	/**
	 * i assume that the function get factors without evidence
	 * function that calculate the number of multiplication
	 * @param a
	 * @param b
	 * @return
	 */
	public int getNumOfMultiplications(CPT a,CPT b) {
		int mul=1;
		//only calculate the new matrix
		String strA1 = (String)a.mat[1][0];
		String strB1 = (String)b.mat[1][0];
		
		String splitA1 [] = strA1.split(" ");
		String splitB1 [] = strB1.split(" ");
		
		LinkedList<Node> nodes = new LinkedList<Node>();
		
		for(int i=0 ; i < splitA1.length ; i+=2) {
			nodes.add(this.net.getNode(splitA1[i]));
		}
		
		for(int j=0 ; j < splitB1.length ; j+=2) {
			if(!nodes.contains(this.net.getNode(splitB1[j]))) {
				nodes.add(this.net.getNode(splitB1[j]));
			}	
		}
		ListIterator<Node> iter =nodes.listIterator();
		
		while(iter.hasNext()) {
			mul *= iter.next().getCurrVar().size();
		}
	
		return mul;
	}
	
	public LinkedList<CPT> organize(LinkedList<CPT> cpt ,LinkedList<Node> hidden){		//mistake
		LinkedList<CPT> res= new LinkedList<CPT>();
		LinkedList<CPT> hidd= 	cpt;
		ListIterator<CPT> iterCPT = cpt.listIterator();
		ListIterator<Node> iterNode = hidden.listIterator();
		while(iterNode.hasNext()) {
			Node l1= iterNode.next();
			while(iterCPT.hasNext()) {
				CPT curr= iterCPT.next();
				if((((String)(curr.mat[1][0])).contains(l1.getName())) && !res.contains(curr)){
					res.add(curr);
				}
			}
			iterCPT = cpt.listIterator();
		}
		
		return res;
	}
	public CPT MarginCpt(CPT one , Node marg) {
		
		if(one == null)
			return null;
		
		if(!((String)(one.mat[1][0])).contains(marg.getName()))	//check if have what to margin 
			return null;
		
		CPT margin = new CPT();
		LinkedList<String> var =marg.currVar;
		ListIterator<String> iter = var.listIterator();
		String s="";
		
		margin.mat = new Object[((one.depth-1)/marg.currVar.size())+1][2];
		margin.depth=(one.depth-1)/var.size()+1;
		margin.width=2;
		//
		int loc= 1;
		LinkedList<String> variables= new LinkedList<String>();
		Double sum= 0.0;
		for(int i =1 ; loc < margin.depth ; i++) {
			String [] spl = ((String)(one.mat[i][0])).split(" ");
			String find="";
			for(int index =0 ; index < spl.length ; index+=2) {	//create the position that we start with
				if(!spl[index].equals(marg.getName())) {
					variables.add(spl[index] +" "+ spl[index+1]);
					find += spl[index] +" "+ spl[index+1]+ " ";
				}
			}
			find =find.substring(0, find.length()-1);
			ListIterator<String> it = variables.listIterator();
			int counter=0;
			boolean correct= true;
			boolean isit= isExist(margin, variables);
			if(!isExist(margin, variables)) {		//check if calculate this position
			for(int j =1 ; j < one.depth ; j++) {	//look for this position
				while(it.hasNext()) {
					String v= it.next();
						if(!((String)(one.mat[j][0])).contains(v)) {		//the second check it for check if we once count this fount
						correct=false;
							}
						}
						if(correct) {
							sum+= (Double)one.mat[j][1];
							if(counter != 0) {
								numOfAdd++;		
							}
							counter++;
									}	
							correct= true;
							it = variables.listIterator();
			}
					
			margin.mat[loc][0] = find;
			margin.mat[loc][1] = sum;
			loc++;
			sum=0.0;
			}
			variables.removeAll(variables);
		}
		
		return margin;
		
	}
	
	public boolean isExist(CPT cpt, LinkedList<String> prod) {
		LinkedList<String> list = prod;
		ListIterator<String> iter= list.listIterator();

		boolean founded= false;	//true - he exist , false - not exist
		int loc=1;
		int size=0;
		while(cpt.mat[loc][0] != null && !founded) {
			while(iter.hasNext()) {
				if(((String)(cpt.mat[loc][0])).contains(iter.next())) {
					size++;
				}
			}
			iter= list.listIterator();	
			loc++;
			if(size== list.size())
				founded=true;
			size=0;
		}
		return founded;
	}
	
	public CPT joinCpt(CPT one ,CPT two) {
		CPT join = new CPT();
		LinkedList<Node> intersection = intersection(one,two);
		LinkedList<Node> reminder = relativeReminder(one , intersection);
	
		// the new join cpt
	
		int sum= 1;
		Iterator<Node> iter =reminder.iterator();
		ListIterator<Node> iterator = reminder.listIterator();
		while(iterator.hasNext()) {
			Node node= iterator.next();
			if(node != null && !node.getCurrVar().isEmpty()) {
			sum *= node.getCurrVar().size();	
			}
		}
		int dep =sum*(two.depth-1)+1;
		join.mat = new Object[dep][2];
		join.depth = dep ;
		join.width =2;
		
		
		if(!intersection.isEmpty()) {	//if one and two have intersection
		int locJoin = 1;
		for(int i =1 ; i <one.depth ; i++) {			//find the intersection in every row 
			LinkedList<String> interCheck = intesectionString(intersection ,(String)one.mat[i][0]);
			String remindString= reminderString(reminder , (String)one.mat[i][0]);
			LinkedList<Integer> queue = indexContain(two, interCheck);
			ListIterator<Integer> iterQueue = queue.listIterator();
			
			while(iterQueue.hasNext()) {
				int place= iterQueue.next();
				{
					if(!reminder.isEmpty()) {
			join.mat[locJoin][0] = (String)two.mat[place][0] + " " +remindString;
			if((Double)one.mat[i][1] != 0) {
			join.mat[locJoin][1] = (Double)two.mat[place][1]*(Double)one.mat[i][1];
			numOfMul++;
			locJoin++;
			}
						}
					else {
						if((Double)one.mat[i][1] != 0) {
						join.mat[locJoin][0] = (String)two.mat[place][0];
						join.mat[locJoin][1] = (Double)two.mat[place][1]*(Double)one.mat[i][1];
						numOfMul++;
						locJoin++;
							}
						}
					}
				}	
			}
		}
		else {		//if one and two havn't intersection
			int loc=1;
				for(int i =1 ; i <one.depth ; i++) {
					for(int j = 1 ; j <two.depth ;j++) {
					String remindString = reminderString(reminder , (String)one.mat[i][0]);
					join.mat[loc][0] = (String)two.mat[j][0]+" "+ remindString;
					join.mat[loc][1] = (Double)two.mat[j][1]*(Double)one.mat[i][1];
					numOfMul++;
					loc++;
				}
			}
		}
		return join;
		
	}
	/**
	 * method that check if the CPT contain the string
	 * @param list
	 * @param s
	 * @return boolean
	 * 
	 */
	public LinkedList<Integer> indexContain(CPT big , LinkedList<String> list) {
		int loc=0;
		int size=0;
		LinkedList<Integer> queue =new LinkedList<Integer>();
		ListIterator<String> iterCp =list.listIterator();
		for(int i=1 ; i<big.depth ; i++) {
			while(iterCp.hasNext()) {
				if(((String)(big.mat[i][0])).contains(iterCp.next())) {
					size++;
					loc=i;
				}
			}
			iterCp =list.listIterator();
			if(size == list.size()) {
				queue.add(loc);
			}
			size=0;
		}
		return queue;
	}
	public LinkedList<String> intesectionString(LinkedList<Node> list ,String s) {
		
		LinkedList<String> res= new LinkedList<String>();	//keep them as tuple of node and state
		ListIterator<Node> iterNode = list.listIterator();
		String [] split =s.split(" ");
	
		while(iterNode.hasNext()) {
		String check = iterNode.next().getName();
			for(int  i=0 ; i< split.length ; i+=2) {
				if(split[i].equals(check)) {
					res.add(check +" "+ split[i+1]);
				}
			}
		}
		return res;
	}
	/**
	 * get list of nodes that in the intersection and look for the rest
	 * @param list
	 * @param s
	 * @return
	 */
	public String reminderString(LinkedList<Node> list ,String s) {
		String res= "";
		LinkedList<String> ls= new LinkedList<String>();
		String  [] pl= s.split(" ");
		for(int i=0 ;i < pl.length ; i++) {
			if(!pl[i].equals("")) {
				ls.add(pl[i]);
			}
		}
		ListIterator<String> iter= ls.listIterator();
		while(iter.hasNext()) {
			String l1= iter.next();
			if(this.net.getNode(l1)!= null) {
			if(list.contains(this.net.getNode(l1))) {
				String v= iter.next();
				res += l1+" "+v+ " ";
			}
		}
		}
		if(res.length()>0) {
		res= res.substring(0, res.length()-1);
		}
		return res;
		
	}
	
	public LinkedList<Node> intersection(CPT a , CPT b){
		
		LinkedList<Node> intersection = new LinkedList<Node>();
		String [] splitFirst= ((String)a.mat[1][0]).split(" ");
		String [] splitSecond= ((String)b.mat[1][0]).split(" ");
	
		for(int i=0 ; i < splitFirst.length ; i+=2) {
			for (int j = 0; j < splitSecond.length; j+=2) {
				if(splitSecond[j].equals(splitFirst[i])) {
					intersection.add(this.net.getNode(splitFirst[i]));
				}
			}
		}
		return intersection;
	}
	
	public LinkedList<Node> relativeReminder(CPT small , LinkedList<Node> intersection){		
		
		LinkedList<Node> reminder = new LinkedList<Node>();
		ListIterator<Node> iterInter = intersection.listIterator();
		String [] splitSmall= ((String)small.mat[1][0]).split(" ");
		
		for(int i=0 ; i<splitSmall.length ; i+=2) {
			boolean con = intersection.contains(this.net.getNode(splitSmall[i]));
			if(!intersection.contains(this.net.getNode(splitSmall[i]))) {
			reminder.add(this.net.getNode(splitSmall[i]));
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
			String [] s = ((String)needUp.mat[need][0]).split(" ");
			String res ="";
				for(int j = 0; j< s.length ; j+=2) {		//insert strings at the first column
					if(!(s[j] +" " + s[j+1]).equals(check)) {
						res+= s[j]+  " " + s[j+1];
			}
		}
				for (int i = 1; i < needUp.varCurr.size()+1; i++) {
					cpt.mat[row][i] = (Double)needUp.mat[need][i];	
					cpt.mat[row][0]= res;	
				
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
			
		for (int i = 1; i < curr.depth; i++) {	//update Strings
			update.mat[i][0]= curr.mat[i][0];
		}
		
		for (int i = 1; i < curr.depth; i++) { 	//update the values of the state
			update.mat[i][1]=	curr.mat[i][ColumnState];
		}
		
		return update;
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


	
}
