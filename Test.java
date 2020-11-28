package ex0;

import java.awt.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;


public class Test {
	public static void main(String[] args) {
//first network
		Network net = new Network();
		Node node1 = net.addNode("A");
		node1.addCurr("Values: true,false");
		node1.addPar("Parents: none");
		node1.addProb("=true,0.1");
		Node node2 = net.addNode("B");
		node2.addCurr("Values: set,noset,maybe");
		node2.addPar("Parents: none");
		node2.addProb("=set,0.2,=noset,0.5");
		Node node3 = net.addNode("C");
		node3.addCurr("Values: go,stay,run");
		node3.addPar("Parents: A,B");
		node3.addProb("true,set,=go,0.25,=stay,0.7");
		node3.addProb("true,noset,=go,0.2,=stay,0.6");
		node3.addProb("true,maybe,=go,0.3,=stay,0.2");
		node3.addProb("false,set,=go,0.55,=stay,0.15");
		node3.addProb("false,noset,=go,0.28,=stay,0.3");
		node3.addProb("false,maybe,=go,0.45,=stay,0.25");
//		System.out.println(node1.getCpt().toString());
//		System.out.println("/////");
//		System.out.println(node2.getCpt().toString());
//		System.out.println("/////");
//		System.out.println(node3.getCpt().toString());
//System.out.println("////////////////////////////////////////////////////////////////////////////////////////////////////////////");
//second network
		Network net2 = new Network();
		Node node11 = net2.addNode("B");
		node11.addCurr("Values: true,false");
		node11.addPar("Parents: none");
		node11.addProb("=true,0.001");
		Node node22 = net2.addNode("E");
		node22.addCurr("Values: true,false");
		node22.addPar("Parents: none");
		node22.addProb("=true,0.002");
		Node node33 = net2.addNode("A");
		node33.addCurr("Values: true,false");
		node33.addPar("Parents: B,E");
		node33.addProb("true,true,=true,0.95");
		node33.addProb("true,false,=true,0.94");
		node33.addProb("false,true,=true,0.29");
		node33.addProb("false,false,=true,0.001");
		Node node44 = net2.addNode("J");
		node44.addCurr("Values: true,false");
		node44.addPar("Parents: A");
		node44.addProb("true,=true,0.9");
		node44.addProb("false,=true,0.05");
		Node node55 = net2.addNode("M");
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
		
		//query1
		
System.out.println("query1");
Queries1 quer = new Queries1(net2);
System.out.println(quer.parseProb("P(B=true|J=true,M=true),1"));
		
		
		//query2
System.out.println("query2");			
Queries2 quer2 = new Queries2(net2);
String s = quer2.prob("P(B=true|J=true,M=true),1");
System.out.println(s);

//query3
System.out.println("query3");
Queries3 quer3 = new Queries3(net2);
String prob = quer3.prob("P(B=true|J=true,M=true),1");
	System.out.println(prob);

	}               
}
