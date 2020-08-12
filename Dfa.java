
import java.awt.List;
import java.util.ArrayList;

public class Dfa {
	
static String DFAInput = "";
static String[] states = new String[] {};
static String[] acceptStates = new String[] {};
static String currentState = "0"; // could be changed to the initial value of dfa input

static boolean accepted = false;
static String initialStateFromNFA = "";
public Dfa(String DFAInput) {
	this.DFAInput = DFAInput;
	initialStateFromNFA = NFAtoDFA.initialStateForDFA;
}

public static void initialize() {
	String[] statesAndAccept = DFAInput.split("#");
	if(NFAtoDFA.isAcceptStates) {
		acceptStates = statesAndAccept[1].split(",");
	}
	
	states = statesAndAccept[0].split(";");
}

public static void isAccepted(String state) {
	for(int i = 0; i < acceptStates.length; i++) {
		if (state.equals(acceptStates[i])) {
//			System.out.println("Hello true");
			accepted = true;
			break;
		}
		
		else {
			accepted = false;
		}
	}	
}

public static int findStatePosition(String currS) {
	int index = 0;
	String[] trans = new String[] {} ;
	String currState = "";
	for(int state = 0; state < states.length; state++) {
		trans = states[state].split(",");
		currState = trans[0];
		if (currState.equals(currS)) {
			index = state;
			break;
		}
		
	}
	
	return index;
	
}

public static boolean runningDFA(String input) {
	initialize();
	String[] transition = new String[] {} ;
	int statepos = 0;
	for(int i = 0; i < input.length(); i++) {
		char inp = input.charAt(i);
		if(i == 0) {
			statepos = findStatePosition(initialStateFromNFA);
			transition = states[statepos].split(","); 
		}
		else {
			transition = states[statepos].split(","); 
		}
		
		
		if (inp == '0') {
			currentState = transition[1];
			//statepos = Integer.parseInt(currentState);
			statepos = findStatePosition(currentState);
			isAccepted(currentState);
		}
		
		else if(inp == '1'){
			currentState = transition[2];
			//statepos = Integer.parseInt(currentState);
			statepos = findStatePosition(currentState);
			isAccepted(currentState);
		}
		
		else {
			System.out.println("Invalid Input, you should enter 0 or 1.");
			accepted = false;
			break;
		}
	
	}
	
	return accepted;
	
}


}

