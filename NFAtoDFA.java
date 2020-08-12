import java.lang.ref.Reference;
import java.util.*; 

public class NFAtoDFA {
	static String NFAInput = "";
	static String[] splittedNFA = new String[] {};
	
	static Hashtable<Integer, String> hashOfZeros = new Hashtable<Integer, String>(); 
	static Hashtable<Integer, String> hashOfOnes = new Hashtable<Integer, String>(); 
	static Hashtable<Integer, String> stateReference = new Hashtable<Integer, String>(); 
	static Stack<String> stackOfCurrentStates = new Stack<String>(); 
	static Stack<String> stackOfusedStates = new Stack<String>(); 

	static String AcceptStates = "";
	static String initialStateForDFA = "";
	static String[] epsilonArray ;
	
	static String[] splitZeros = new String[] {};
	static String[] splitOnes = new String[] {};
	static String[] splitEpsilons = new String[] {};
	static String[] splitAcceptStates = new String[] {};
	
	
	static boolean isZeros = true;
	static boolean isOnes = true;
	static boolean isEpsilons = true;
	static boolean isAcceptStates = true;
	static int counthashtags = 0;
	
	static int indexZero = 0;
	static int indexOne = 1;
	static int indexEpsilon = 2;
	static int indexAcceptState = 3;
	
	public NFAtoDFA(String NFAInput) {
		this.NFAInput = NFAInput;
		
		for(int i = 0; i<this.NFAInput.length(); i++) {
			if(this.NFAInput.charAt(0) == '#') {  // Case1: If there is no Zero transition
				isZeros = false;
				
			}
			
			if(this.NFAInput.charAt(i) == '#') {
				counthashtags++;
			}
			
			if(counthashtags == 2 && this.NFAInput.charAt(i-1) == '#' && this.NFAInput.charAt(i) == '#' && i>0 ) { // Case2: If there is no One transition
				isOnes = false;

			}
			
			if(counthashtags == 3 && this.NFAInput.charAt(i-1) == '#' && this.NFAInput.charAt(i) == '#' && i>0 ) { // Case3: If there is no Epsilon transition
				isEpsilons = false;

			}
			
			if(this.NFAInput.charAt(this.NFAInput.length()-1) == '#') {
				isAcceptStates = false;

			}
			
		}
		
		System.out.println("isZeros " + isZeros);
		System.out.println("isOnes " + isOnes);
		System.out.println("isEpsilons " + isEpsilons);
		System.out.println("isAcceptStates " + isAcceptStates);		
	
		splittedNFA = this.NFAInput.split("#");
		
		// Accessing Array
		
		if(isZeros) {
			splitZeros = splittedNFA[indexZero].split(";");
		}
		if(isOnes) {
			splitOnes = splittedNFA[indexOne].split(";");
		}
		if(isEpsilons) {
			splitEpsilons = splittedNFA[indexEpsilon].split(";");
		}
		if(isAcceptStates) {
			splitAcceptStates = splittedNFA[indexAcceptState].split(",");
			System.out.println("splitAcceptStates " + splittedNFA[indexAcceptState] +"indexAcceptState " + indexAcceptState);
		}
		
		epsilonArray = new String[getNumberofStates()+1];

	}

	public static int getNumberofStates() {
		String[] split1 = NFAInput.split("#");
		int maximumNum = 0;
		int currentNum = 0;
		String curr = "";
		for(int i = 0; i<NFAInput.length(); i++) {
			if (NFAInput.charAt(i) != ',' && NFAInput.charAt(i) != ';' && NFAInput.charAt(i) != '#') {
				curr = curr + Character.toString(NFAInput.charAt(i));

			}
			else if(!curr.equals("")) {
				currentNum = Integer.parseInt(curr); 
				curr = "";
				if (currentNum >= maximumNum) {
					maximumNum = currentNum;
				}
			}
		}
		System.out.println("Number of states: "+maximumNum);
		return maximumNum;
	}

	public static void storeEpsilons() {
		int countOfStates = getNumberofStates() + 1;
		for(int i = 0; i<countOfStates; i++) {
			epsilonArray[i] =  Integer.toString(i);
		}
		if(isEpsilons == true) {
			for (int i = 0; i<splitEpsilons.length; i++) {
				String[] currEps = splitEpsilons[i].split(",");
				epsilonArray[Integer.parseInt(currEps[0])] += currEps[1];
			}
		}

	}
	
	public static String searchingEpsilons(String state) {
		String currentState = state;
		for(int i = 0; i<state.length(); i++) {
			char curr = state.charAt(i);
			int currInt = Integer.parseInt(String.valueOf(curr));  
			String nextStates = epsilonArray[currInt];
			for(int j = 0; j<nextStates.length(); j++) {
				if (currentState.indexOf(nextStates.charAt(j)) < 0) {
					currentState += nextStates.charAt(j);
				}
			}
		}
		return currentState;
	}
	
	public static void convertNfaIntoTransitions() {
		storeEpsilons();
		
		String nextState = "";
		String currentState = "";
		int key = 0;
		String initialState = searchingEpsilons("0"); //INITIALIZATION
		initialStateForDFA = initialState;
		stateReference.put(key, initialState); //INITIALIZATION
		currentState = initialState;
		stackOfCurrentStates.push(currentState);
		
		while(!stackOfCurrentStates.isEmpty()) {
			currentState = stackOfCurrentStates.pop();
			stackOfusedStates.push(currentState);
			if(isZeros) {
				for(int i = 0; i<splitZeros.length; i++) {
					String zero = splitZeros[i];
					char gettingZero = zero.charAt(0);
					char next0 = zero.charAt(2);
					if(currentState.indexOf(gettingZero) >= 0) {
						if(nextState.indexOf(next0) < 0) {
							nextState += next0;
						}
					}
				}
			}

			String newNextStateZero = searchingEpsilons(nextState);
			nextState = "";
			if(isOnes) {
				for(int i = 0; i<splitOnes.length; i++) {
					String one = splitOnes[i];
					char gettingOne = one.charAt(0);
					char next1 = one.charAt(2);
					if(currentState.indexOf(gettingOne) >= 0) {
						if(nextState.indexOf(next1) < 0) {
							nextState += next1;
						}

					}
				}
			}
			
			
			String newNextStateOne = searchingEpsilons(nextState);
			nextState = "";
			if(newNextStateZero.equals(newNextStateOne)) {
				if(key == 0) {
					if(!checkStackDuplicates(newNextStateOne)) {
						stackOfCurrentStates.push(newNextStateZero);
					}
					hashOfZeros.put(key, newNextStateZero);
					hashOfOnes.put(key, newNextStateOne);
				}
				else {
					if(!checkStackDuplicates(newNextStateOne)) {
						stackOfCurrentStates.push(newNextStateZero);
						
					}
					
					hashOfZeros.put(key, newNextStateZero);
					hashOfOnes.put(key, newNextStateOne);
					stateReference.put(key, currentState);
				}
				
			}
			else {
				if(key == 0) {
					if(!checkStackDuplicates(newNextStateZero)) {
						stackOfCurrentStates.push(newNextStateZero);
					}
					if(!checkStackDuplicates(newNextStateOne)) {
						stackOfCurrentStates.push(newNextStateOne);
					}
					
					hashOfZeros.put(key, newNextStateZero);
					hashOfOnes.put(key, newNextStateOne);
				}
				else {
					if(!checkStackDuplicates(newNextStateZero)) {
						stackOfCurrentStates.push(newNextStateZero);
					}
					if(!checkStackDuplicates(newNextStateOne)) {
						stackOfCurrentStates.push(newNextStateOne);
					}

					hashOfZeros.put(key, newNextStateZero);
					hashOfOnes.put(key, newNextStateOne);
					stateReference.put(key, currentState);
				}
			}
			key += 1;
	  }
		
	}
	
	public static boolean checkStackDuplicates(String state) {
		if(stackOfCurrentStates.contains(state) || stackOfusedStates.contains(state)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String formAcceptStates() {
		for(int i = 0; i<stateReference.size(); i++) {
			String valueRef = stateReference.get(i);
			for(int j = 0; j<splitAcceptStates.length; j++) {
				if (valueRef.indexOf(splitAcceptStates[j]) >= 0 ) {
					System.out.println("formAcceptStates() + valueRef " + valueRef);
					System.out.println("formAcceptStates() + splitAcceptStates[j] " + splitAcceptStates[j]);
					AcceptStates += valueRef+",";
				}
			}
	    }
		
		String [] acceptS = AcceptStates.split(",");    
		
        //This array has duplicate elements
         
        //Create set from array elements
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>( Arrays.asList(acceptS) );
         
        //Get back the array without duplicates
        String[] numbersWithoutDuplicates = linkedHashSet.toArray(new String[] {});
         
        //Verify the array content
        System.out.println( "Accept States  " + Arrays.toString(numbersWithoutDuplicates) );
        String acc = "#";
        for(int i = 0; i<numbersWithoutDuplicates.length; i++) {
        		acc += numbersWithoutDuplicates[i]+",";
        }
        
        acc = acc.substring(0, acc.length()-1);
        System.out.println(acc);
		return acc;
	}
	
	public static String convertIntoDfa() {
		convertNfaIntoTransitions();
		String Dfa = "";
		for(int i = 0; i<stateReference.size(); i++) {
			String valueRef = stateReference.get(i);
			String valueZero = hashOfZeros.get(i);
			String valueOne = hashOfOnes.get(i);
			if(valueRef.equals("")) {
				stateReference.put(i, "D");
			}
			if(valueZero.equals("")) {
				hashOfZeros.put(i, "D");
			}
			if(valueOne.equals("")) {
				hashOfOnes.put(i, "D");			
			}	
		}
		
		if(isAcceptStates) {
			AcceptStates = formAcceptStates();
		}
		else {
			AcceptStates = "#";
		}
		 
		 for(int i = 0; i<stateReference.size(); i++) {
			 Dfa += stateReference.get(i)+ "," + hashOfZeros.get(i) + "," + hashOfOnes.get(i) +";";
		 }
		 Dfa = Dfa.substring(0, Dfa.length()-1);
		 Dfa += AcceptStates;
		 System.out.println("stateReference" + stateReference);
		 System.out.println("--");
		 System.out.println("hashOfZeros " + hashOfZeros);
		 System.out.println("--");
		 System.out.println("hashOfOnes" + hashOfOnes);
		 System.out.println("DFA: "+Dfa);
		return Dfa;
		
	}
	
	public static void main(String args[]) {
		//0,0;1,2;3,3#0,0;0,1;2,3;3,3#1,2#3 task
//		NFAtoDFA nfa = new NFAtoDFA("##0,4#3,4");
//	    convertIntoDfa();
//		System.out.println(epsilonArray[0]);
//		System.out.println(epsilonArray[1]);
//		System.out.println(epsilonArray[2]);
//		System.out.println(epsilonArray[3]);
//		System.out.println(stateReference);
//		System.out.println("--");
//		System.out.println(hashOfZeros);
//		System.out.println("--");
//		System.out.println(hashOfOnes);
	}
	
}
