public class TestNew2 {

	public static void main(String args[]) {
//
//		NFAtoDFA nfa = new NFAtoDFA("0,0;0,1;1,3#0,1;1,2;2,3#1,2;3,2#3");
//		Dfa dfa = new Dfa(nfa.convertIntoDfa()); 
//		System.out.println(dfa.runningDFA("000111"));
//		System.out.println(dfa.runningDFA("10110"));
//		System.out.println(dfa.runningDFA("00110"));
//		System.out.println(dfa.runningDFA("101010"));
//		System.out.println(dfa.runningDFA("1111"));
		
		System.out.println("---------Test Case 2-------------");
		
		NFAtoDFA nfa2 = new NFAtoDFA("0,1;1,2;2,3#1,3;3,4;4,2#0,2;3,1;2,4#2");
		Dfa dfa2 = new Dfa(nfa2.convertIntoDfa()); 
		System.out.println(dfa2.runningDFA("0000"));
		System.out.println(dfa2.runningDFA("011011"));
		System.out.println(dfa2.runningDFA("01010"));
		System.out.println(dfa2.runningDFA("101010"));
		System.out.println(dfa2.runningDFA("11100"));
	}
}
