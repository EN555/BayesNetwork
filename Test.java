package ex0;

import java.awt.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Test {
	public static LinkedList<String> generatePermutations(LinkedList<LinkedList<String>> lists, LinkedList<String> result, int depth, String current) {
	    if (depth == lists.size()) {
	        result.add(current);
	        return 	result;
	    }
	    
	    for (int i = 0; i < lists.get(depth).size(); i++) {
	        generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i)+ " ");
	    }
	    return result;
	}
	public static void main(String[] args) {
//first network
//		Network net = new Network();
//		Node node1 = net.addNode("Var A");
//		node1.addCurr("Values: true,false");
//		node1.addPar("Parents: none");
//		node1.addProb("=true,0.1");
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
//		System.out.println(node1.getCpt().toString());
//		System.out.println("/////");
//		System.out.println(node2.getCpt().toString());
//		System.out.println("/////");
//		System.out.println(node3.getCpt().toString());
//System.out.println("////////////////////////////////////////////////////////////////////////////////////////////////////////////");
//second network
		Network net2 = new Network();
		Node node11 = net2.addNode("Var B");
		node11.addCurr("Values: true,false");
		node11.addPar("Parents: none");
		node11.addProb("=true,0.001");
		Node node22 = net2.addNode("Var E");
		node22.addCurr("Values: true,false");
		node22.addPar("Parents: none");
		node22.addProb("=true,0.002");
		Node node33 = net2.addNode("Var A");
		node33.addCurr("Values: true,false");
		node33.addPar("Parents: B,E");
		node33.addProb("true,true,=true,0.95");
		node33.addProb("true,false,=true,0.94");
		node33.addProb("false,true,=true,0.29");
		node33.addProb("false,false,=true,0.001");
		Node node44 = net2.addNode("Var J");
		node44.addCurr("Values: true,false");
		node44.addPar("Parents: A");
		node44.addProb("true,=true,0.9");
		node44.addProb("false,=true,0.05");
		Node node55 = net2.addNode("Var M");
		node55.addCurr("Values: true,false");
		node55.addPar("Parents: A");
		node55.addProb("true,=true,0.7");
		node55.addProb("false,=true,0.01");
//		System.out.println("var b");
//		System.out.println(node11.getCpt().toString());
//		System.out.println("var E");
//		System.out.println(node22.getCpt().toString());
//		System.out.println("var A");
//		System.out.println(node33.getCpt().toString());
//System.out.println("var j");
//System.out.println(node44.getCpt().toString());
//System.out.println("var m");
//System.out.println(node55.getCpt().toString());
////		
//////////////////////////////////////////////////////////////////////
//		LinkedList<LinkedList<String>> str = new LinkedList<LinkedList<String>>();
//		LinkedList<String> a = new LinkedList<String>();
//		LinkedList<String> b = new LinkedList<String>();
//		LinkedList<String> c = new LinkedList<String>();
//		a.add("1");
//		a.add("3");
//		b.add("2");
//		c.add("4");
//		c.add("5");
//		str.add(a);
//		str.add(b);
//		str.add(c);
//		LinkedList<String> res = new LinkedList<String>();
//		LinkedList<String> vert = generatePermutations(str, res, 0, "");
//		ListIterator<String> iter = vert.listIterator();
//		while(iter.hasNext()) {
//			System.out.println(iter.next());
//		}
		
////////////////////////////////////////////////////////////////////////////////		
		Queries1 quer = new Queries1(net2);
//		System.out.println(quer.parseProb("P(B=true|B=true),1"));
//		LinkedList<Node> lis = new LinkedList<Node>();
//		lis.add(node2);
//		lis.add(node3);
//		LinkedList<String> res = new LinkedList<String>();
//		res = quer.generatePerNode(lis);
//		ListIterator<String> iter = res.listIterator();
//		while(iter.hasNext()) {
//			System.out.println(iter.next());
//		}
//		String str = var.replaceAll("[^A-Za-z]"," ");
//		String [] split = str.split(" ");
//		System.out.println(str);
//		System.out.println(split.length);
//		for(int i= ; i<str.length() ;) {
//			System.out.println(split[i]);
//			if(i+ 2 <str.length()) {
//				i += 2;
//			}
//			else {
//				i = str.length();			}
//		}
//		System.out.println("//");
//for(int i = 3 ; i<split.length ; i+=2) {
//	if(i<split.length) {
//	System.out.println(split[i]);
//	}
//	
//}
//System.out.println("////");
String var = "P(C=run|B=set,A=true),1";	
//String var2 = "P(B=true|J=true,M=true),1";
//LinkedList<String> per =quer.parseProb(var);
//ListIterator<String> itera = per.listIterator();
//while(itera.hasNext()) {
//	System.out.println(itera.next());
//}
//System.out.println(quer.parseProb(var));
//System.out.println("//probability//");
Queries2 quer2 = new Queries2(net2);
//LinkedList<CPT>  hidden = quer2.prob(var);
//for(CPT cp : hidden) {
//	System.out.println(cp);
//}
System.out.println("///join///");
//LinkedList<CPT> cp =quer2.prob(var);	
//	for(CPT a : cp) {
//		System.out.println(a);
//	}
//	System.out.println("//check//");
//	CPT a =quer2.replaceUpdateCpt(node55, "true");
//	System.out.println(a);
	//CPT b =quer2.replaceUpdateCpt(node33, "true");
//System.out.println(quer2.MarginCpt(quer2.convertCpt(node33.cpt),node33));
//System.out.println(quer2.convertCpt(node33.cpt));	
//LinkedList<CPT> list = quer2.prob(var);
//ListIterator<CPT> iter =list.listIterator();
//System.out.println(list);
//CPT a1= iter.next();
//CPT a2= iter.next();
//CPT a3= quer2.joinCpt(a1, a2);
//System.out.println(a3);
//CPT a4 = quer2.joinCpt(iter.next(), a3);
//System.out.println(a4);
//CPT a5 = quer2.MarginCpt(a4, node33);
//System.out.println("???");
//System.out.println(a5);
////System.out.println("///");
//CPT a6= quer2.joinCpt(a5, iter.next());
//System.out.println(a6);
//CPT a7 =quer2.MarginCpt(a6, node22);
//CPT res = quer2.joinCpt(a7, quer2.convertCpt(node11.cpt));
//System.out.println(node22.getName());
//System.out.println(a6);
//CPT a7= quer2.joinCpt(quer2.convertCpt(node11.cpt), a6);
//System.out.println(a7);
//System.out.println((5.922425899999999E-4 )/(5.922425899999999E-4 +0.001491857649 ));
//String res = quer2.prob(var);
//ListIterator<CPT> iter= list.listIterator();
//System.out.println(list);
//CPT a1= iter.next();
//System.out.println(a1);
//CPT a2 = iter.next();
//System.out.println(a2);
//System.out.println("///////hello///////");
//System.out.println(a1);
//System.out.println(a2);
//CPT a3 =quer2.joinCpt(a1, a2);
//System.out.println("blaaaaaaaaaaaaaaaaaaaaaaa");
//System.out.println(quer2.convertCpt(node11.cpt));
//System.out.println(quer2.convertCpt(node22.cpt));
//System.out.println(quer2.convertCpt(node33.cpt));
//System.out.println(quer2.convertCpt(node44.cpt));
//System.out.println(quer2.convertCpt(node55.cpt));
//CPT a1 = quer2.convertCpt(node33.cpt);
//CPT a2 =quer2.convertCpt(node44.cpt);
//CPT a3= quer2.joinCpt(a1, a2);
//CPT a4 =quer2.convertCpt(node55.cpt);
//CPT a5= quer2.joinCpt(a4, a3);
//System.out.println(a5);
//CPT a6 = quer2.MarginCpt(a5, node33);
//System.out.println(a6);
//System.out.println(quer2.MarginCpt(a2, node33));
System.out.println(quer2.convertCpt(node44.cpt));
//CPT a8 = quer2.joinCpt(a7, quer2.convertCpt(node11.cpt));
//System.out.println(s);
//LinkedList<CPT> list =new LinkedList<CPT>();
//list.add(quer2.convertCpt(node55.cpt));
//list.add(quer2.convertCpt(node44.cpt));
//list.add(quer2.convertCpt(node33.cpt));
//System.out.println(quer2.MinMultiplicaionMediatorMain(list));
//System.out.println(quer2.convertCpt(node44.cpt));
String s = quer2.prob("P(J=true|J=true),1");
System.out.println(s);
	
	}

                       
}
