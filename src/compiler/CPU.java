package compiler;

import java.util.*;

public class CPU {
	private  MidRegister IF_ID, ID_EX, EX_MEM, MEM_WB;
	private Interpreter interpreter;
	private Memory insMem, dataMem, regFile;
	private ALU centralALU;
	private Control control;
	private int PC = 0x00000010 / 4;

	private ArrayList<Module> array;
	public Set<Integer> branch_set = new HashSet<Integer>();


	public static int MAXMEM = 100;
	public static boolean HAZARD = false;

	public CPU(Interpreter inter_) {
		interpreter = inter_;

		Map<Integer,Integer> init = new HashMap<>();
		for (int l=0;l<MAXMEM;l++) {
			String s = interpreter.nextInstruction();
			String sbin = Interpreter.toBinary(s);
			int nbin = Interpreter.to_int(sbin);
			int op = (nbin>>26);
			if (op==4)
				branch_set.add(l);
			init.put(l,nbin);
		}

		insMem = new Memory(null, init);
		IF_ID = new MidRegister(insMem);
		control = new Control(IF_ID);
		init = new HashMap<>();
		regFile = new Memory(control, init);
		ID_EX = new MidRegister(regFile);
		centralALU = new ALU(ID_EX);
		EX_MEM = new MidRegister(centralALU);
		dataMem = new Memory(EX_MEM,init);
		MEM_WB = new MidRegister(dataMem);

		array = new ArrayList<>();
		array.add(insMem);
		array.add(IF_ID);
		array.add(control);
		array.add(regFile);
		array.add(ID_EX);
		array.add(centralALU);
		array.add(EX_MEM);
		array.add(dataMem);
		array.add(MEM_WB);
	}


	public void clock() {

		// Hazard
//		if (HAZARD){
//			for (String x: forward.getOutput().keySet())
//				centralALU.addToInput(x,forward.getOutput().get(x));
//			if (branch_set.contains(PC)) { // pc in set
//				clockElements();
//				clockElements();
//				clockElements();
//			}
//		}

		insMem.addToInput("write0",0);
		insMem.addToInput("index0",PC);
		insMem.addToInput("pc_4",PC+1);
		clockElements();

		// PC
		if (centralALU.getOutput()!=null && centralALU.getOutput().containsKey("branch"))
			PC = centralALU.getOutput().get("ALU_Result");
		else
			PC++;


	}

	private void clockElements(){
		for (Module x: array) {
			x.clock();
			System.out.println(x.getClass());
		}
		System.out.println();
		for (Module x: array)
			x.update();
	}

	public Map<String, Integer> log() {
		return null;
		//az hame khorooji migire o mirize too map return mikone
	}
}
