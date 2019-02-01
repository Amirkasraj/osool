package compiler;

import java.util.HashMap;
import java.util.Map;

public class CPU {
	private MidRegister PC, IF_ID, ID_EX, EX_MEM, MEM_WB;
	private Interpreter interpreter;
	private Memory insMem, dataMem;
	private RegisterFile regFile;
	private ALU centralALU;

	public static int MAXMEM = 100;

	public CPU(Interpreter inter_) {
		interpreter = inter_;

		Map<Integer,Integer> init = new HashMap<>();
		for (int l=0;l<MAXMEM;l++) {
			String s = interpreter.nextInstruction();
			//System.out.println(s);
			String sbin = Interpreter.toBinary(s);
			//System.out.println(sbin);
			int nbin = Interpreter.to_int(sbin);
			//System.out.println(nbin);
			init.put(l,nbin);
		}

		insMem = new Memory(null, init);
		IF_ID = new MidRegister(insMem);

		init.clear();
		init.put(0x00000010,Interpreter.register_num("$PC"));

		ID_EX = new MidRegister(null);
		centralALU = new ALU(ID_EX);
		EX_MEM = new MidRegister(centralALU);

		dataMem = new Memory(EX_MEM,init);
		PC = new MidRegister(dataMem);
		MEM_WB = new MidRegister(dataMem);
		regFile = new RegisterFile(IF_ID, init, MEM_WB);
		insMem.setPrev(PC);
		ID_EX.setPrev(regFile);

	}
	
	public void clock() {

		// hame clock mikhoran
		//hame update mishan
	}
	
	public Map<String, Integer> log() {

		return null;
		//az hame khorooji migire o mirize too map return mikone
	}
}
