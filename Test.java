package ex0;

import java.awt.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class Test {
	public static void generatePermutations(LinkedList<LinkedList<String>> lists, LinkedList<String> result, int depth, String current) {
	    if (depth == lists.size()) {
	        result.add(current);
	        return;
	    }
	    
	    for (int i = 0; i < lists.get(depth).size(); i++) {
	        generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i)+" ");
	    }
	}
	public static void main(String[] args) {

		Network net = new Network();
		Node node1 = net.addNode("Var A");
		node1.addCurr("Values: true,false");
		node1.addPar("Parents: none");
		node1.addProb("=true,0.1");
//		Node node2 = net.addNode("Var B");
//		node2.addCurr("Values: set,noset,maybe");
//		node2.addPar("Parents: none");
//		node2.addProb("=set,0.2,=noset,0.5");
//		Node node3 = net.addNode("Var C");
//		node3.addCurr("Values: go,stay,run");
//		node3.addPar("Parents: A,B");
//		node3.addProb("true,set,=go,0.25,=stay,0.7");
//		node3.addProb("true,noset,=go,0.2,=stay,0.6");
//		node3.addProb("true,maybe,=go,0.3,=stay,0.2");
//		node3.addProb("false,set,=go,0.55,=stay,0.15");
//		node3.addProb("false,noset,=go,0.28,=stay,0.3");
//		node3.addProb("false,maybe,=go,0.45,=stay,0.25");
//		System.out.println(node3.getCpt().toString());
//		System.out.println("/////");
//		System.out.println(node2.getCpt().toString());
		System.out.println("/////");
		System.out.println(node1.getCpt().toString());
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//		Network c= new Network();
//		Node b = new Node("var b" , c);
//	Object arr [] = new Object[3];
//	arr[0] = "hello"; 
//	arr[1] = 2;
//	arr [2]=3;
//	
//	for(int i = 0 ;i< 3 ; i++)
//		System.out.println(arr[i]);
//		System.out.println("////");
//		String s= "=true,0.1";
//		String co = s.split("=")[0];		//extract the substring that contain the condition
//		System.out.println(co + "----");
//		StringTokenizer ou = new StringTokenizer(co ,",");
//		String condition="";				//only the condition of the condition
//		while(ou.hasMoreTokens()) {
//			condition+= ou.nextToken()+ " ";
//		}
//		System.out.println(condition+ "----");
//		String part = s.substring(co.length()+1);		//create array with all the depended variables
//		System.out.println(part);
//		String str = part.replaceAll("[^A-Za-z]"," ");
//		String [] str_var = str.split(" ");
//		String[] numbers = s.replaceAll("[^0-9.]+", " ").trim().split(" ");		//extract all the numbers
//		Double [] doub = new Double[numbers.length];
//	//	System.out.println(doub[0]);
//		int loc= 0;
//		for(String it : numbers) 
//			doub[loc++] = Double.parseDouble(it);		
		
		
		
/////////////////////////////////////////////
		
		
		
//		StringTokenizer s = new StringTokenizer("true,set,=go,0.25,=stay,0.7" , " //,//=");
//		while(s.hasMoreTokens()){
//			System.out.print(s.nextToken() + " ");
//		}
//		String st = "true,maybe,=go,0.3,=stay,0.2"; 
//		String sub = st.split(",=")[0];
//		StringTokenizer s = new StringTokenizer(sub , ",");
//		String p = " ";
//		while(s.hasMoreTokens()){
//			p+= s.nextToken()+ " ";
//		}
//		System.out.println(p);
//		String part = st.substring(sub.length());
//		System.err.println("////");
//		String result = part.replaceAll("[^A-Za-z]"," ");
//		String [] res = result.split(" ");
//		for(String l : res)
//			System.out.print(l+ " ");
//		System.out.println();
//		System.out.println("////");
//		String[] numbers = st.replaceAll("[^0-9.]+", " ").trim().split(" ");
//		for(String num : numbers)
//			System.out.print(num + " ");
	}

}
