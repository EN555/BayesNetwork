package ex0;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Condition {

	public String cond;		//in the cpt you create at every row all the possible things

	public Condition(String cond) {
		this.cond = cond;
	}
	
	@Override
	public boolean equals(Object o) {		//check if two conditions are equals 
		StringTokenizer str = new StringTokenizer(o.toString()," ");
		int len = str.countTokens();
		while(str.hasMoreTokens()) {
			if(!this.cond.contains(str.nextToken()))		//check if every partial String in the input string contain at currrent String 
				return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return this.cond;
	}
	
	

}
