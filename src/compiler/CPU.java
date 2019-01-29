package compiler;

import java.util.Map;

public class CPU {
	MidRegister PC, Instructions, data, IF_ID, ID_EX, EX_MEM, MEM_WB;
	ALU centeral;
	Interpreter interpreter;
	public CPU(Interpreter inter_) {
		interpreter = inter_;

		// loole keshi
	}
	
	public void clock() {
		// hame clock mikhoran
		//hame update mishan
	}
	
	public Map<String, Integer> log() {
		return null;
		//az hame khorooji migire o mirize too map return mikone
		// mirize toye filaa:

	}
}
