package ex0;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.text.html.HTMLDocument.Iterator;

public class CPT {
	public LinkedList<String> varCurr;		//list of all current variables
	public LinkedList<LinkedList<String>> varPar;	//list of list with all parents variables
	public LinkedList<String> varStrPar;    //list with all the condition permutation
	public Node curr;
	Object [][] mat;
	int depth = 0;
	public CPT(Node node) {
		this.curr=node;
		this.varCurr= new LinkedList<String>();
		this.varPar = new LinkedList<LinkedList<String>>();;			
		this.varStrPar= new LinkedList<String>();			
	}
	

	public void initVarMat() {
	if(this.varPar != null) {
	this.varStrPar = new LinkedList<String>();				//list with all the condition permutation
	generatePermutations(this.varPar, this.varStrPar, 0, "");
    ListIterator<String> it = this.varStrPar.listIterator();
    int loc= 1;
    while(it.hasNext()){		//enter the condition to the matrix
		String s= it.next();
		Condition event= new Condition(s);
		mat[loc][0] = event;
		loc++;
	}
	}
    ListIterator<String> iter = this.varCurr.listIterator();
    int col = 1;
    while(iter.hasNext()) {
    	this.mat[0][col++] = iter.next();
    }
	}
	
	public void generatePermutations(LinkedList<LinkedList<String>> lists, LinkedList<String> result, int depth, String current) {
	    if (depth == lists.size()) {
	        result.add(current);
	        return;
	    }
	    
	    for (int i = 0; i < lists.get(depth).size(); i++) {
	        generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i)+ " ");
	    }
	}

	public void setCurrVar(LinkedList<String> list)
	{
		this.varCurr = new LinkedList<String>();
		this.varCurr = list;
	}
	public void setVarPar(LinkedList<LinkedList<String>> list)
	{
		this.varPar = list;
		int factor = 1;
		for(LinkedList<String> iter : this.varPar ) {			
			factor*= iter.size();
		}
		this.depth = factor+1;
		this.mat = new Object[factor+1][1+varCurr.size()];			// i added to factor 1 because the first row uses for current variables
		for(int i = 0 ; i<(factor+1) ; i++) {
			for(int j=0 ; j< (1+varCurr.size()) ; j++) {
				this.mat[i][j] = 0;
			}
		}
			
		initVarMat();				//create all the option and enter them to the mat
	}
	public void setProb(String cond ,String [] str , Double [] numbers) {		//fill the mat 
		int line = 1 ;		//find the current line 
		if(cond.equals("")) {
			
		}
		else {
		while(!((Condition)this.mat[line][0]).equals(cond)) {		//find the line
		line++;
		}
		}
		int index = 0;
		while(index < str.length) { //take a depend
			int col = 1;
			while(!this.mat[0][col++].equals(str[index]));
			this.mat[line][--col] = numbers[index];  //fill the number at the mat
			index++;
			col=1;		//initial the col again
		}
	//	fillTheRest();
	}
	public void fillTheRest() {				//???
		
	}
	
	public String toString() {
		String s = " ";
		for(int i =0 ; i< this.depth ; i++) {
			for(int j=0; j < this.varCurr.size() +1 ; j++) {
			s+= this.mat[i][j] + " ";
			}
			s+="\n";
		}
			return s;
	}
	
}
