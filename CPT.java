package ex0;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.swing.text.html.HTMLDocument.Iterator;

public class CPT {
	public LinkedList<String> varCurr;		//list of all current variables
	public LinkedList<LinkedList<String>> varPar;	//list of list with all parents variables
	public LinkedList<String> varStrPar;    //list with all the condition permutation
	public Node curr;
	Object [][] mat;
	int depth = 0;			//the number of row of the matrix
	public CPT(Node node) {
		this.curr=node;
		this.varCurr= new LinkedList<String>();
		this.varPar = new LinkedList<LinkedList<String>>();;			
		this.varStrPar= new LinkedList<String>();			
	}
	/**
	 * create all the permutation of the parents and update them at the matrix
	 */

	public void initVarMat() {
	if(!this.varPar.isEmpty()) {
	this.varStrPar = new LinkedList<String>();				//list with all the condition permutation
	generatePermutations(this.varPar, this.varStrPar, 0, "");
    ListIterator<String> it = this.varStrPar.listIterator();
    int loc= 1;
    while(it.hasNext()){		//enter the condition to the matrix
		String s= it.next();
//		Condition event= new Condition(s);
		mat[loc][0] = s;
		loc++;
	}
	}
    ListIterator<String> iter = this.varCurr.listIterator();
    int col = 1;
    while(iter.hasNext()) {
    	this.mat[0][col++] = iter.next();			//enter all the current variables
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
		this.varCurr = list;			//create pointer to the list of the variables
	}
	/**
	 * build the matrix and init the all the events of the matrix
	 * @param list
	 */
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
				this.mat[i][j] = 0.0;
			}
		}
			
		initVarMat();				//create all the option and enter them to the mat
	}
	/**
	 * update the prob at the matrix and fill the rest
	 * @param cond
	 * @param str
	 * @param numbers
	 */
	public void setProb(String cond ,String [] str , Double [] numbers) {		//fill the mat 
		int line = 1 ;		//find the current line 
		if(cond.equals("")) {
			
		}
		else {

			String [] con = cond.split(" ");
			boolean found= true;
			while(found) {
			String [] st= ((String)(this.mat[line][0])).split(" ");
			for(int i=1 ; i< st.length ; i+=2) {
				if(st[i].equals(con[(i-1)/2])) {
					found=false;
				}
				else {
					line++;
					found=true;
					break;
				}
			}
				
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
		fillTheRest(line);

	}
	public void fillTheRest(int line) {				
		BigDecimal sum = new BigDecimal("0");
		int index_empty = 0;
		for(int i=1 ; i < this.varCurr.size()+1 ; i++) {
			sum = sum.add(new BigDecimal(String.valueOf((Double)(this.mat[line][i]))));
			if((Double)(this.mat[line][i])== 0.0){
				index_empty = i;
			
			}
			}
		BigDecimal all =new BigDecimal("1.0").subtract(sum);
		this.mat[line][index_empty] = all.doubleValue();
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
