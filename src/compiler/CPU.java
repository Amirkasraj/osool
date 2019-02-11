package compiler;

import java.util.*;

public class CPU {
	private  MidRegister IF_ID, ID_EX, EX_MEM, MEM_WB;
	private Interpreter interpreter;
	private Memory insMem, dataMem, regFile;
	private ALU centralALU;
	private Control control;
	private int PC = 0x00000010 / 4;
	private Map<String,Integer> forwarding;
	private Integer clockNumber=0;

	private ArrayList<Module> array;
	public Set<Integer> branch_set = new HashSet<Integer>();

	public static boolean HAZARD = true;

	public CPU(Interpreter inter_) {
		interpreter = inter_;

		Map<Integer,Integer> init = interpreter.load(branch_set);

		insMem = new Memory(null, init);
		IF_ID = new MidRegister(insMem);
		control = new Control(IF_ID);
		init = new HashMap<>();
		regFile = new Memory(control, init);
		ID_EX = new MidRegister(regFile);
		centralALU = new ALU(ID_EX);
		EX_MEM = new MidRegister(centralALU);
		init.put(0x00000100,100);
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

		forwarding = new HashMap<>();
	}


	public void clock() {

		// Hazard
		if (HAZARD){
			if (MEM_WB.getOutput().containsKey("data0")){
				int value = MEM_WB.getOutput().get("data0");
				Integer key = MEM_WB.getOutput().get("rt");
				forwarding.put(key.toString(),value);
			}
			if (centralALU.getOutput().containsKey("wb")) {
				Integer key = centralALU.getOutput().get("wb");
				int value = centralALU.getOutput().get("ALU_Result");
				forwarding.put(key.toString(),value);
			}
			for (String key: forwarding.keySet())
				centralALU.addToInput(key,forwarding.get(key));
		}

		insMem.addToInput("write0",0);
		insMem.addToInput("index0",PC);
		insMem.addToInput("pc_4",PC+1);
		clockNumber++;
		clockElements();

		// PC
		if (centralALU.getOutput()!=null && centralALU.getOutput().containsKey("branch"))
			PC = centralALU.getOutput().get("ALU_Result");
		else
			PC++;
	}

	private void clockElements(){
		for (Module x: array)
			x.clock();
		for (Module x: array)
			x.update();
	}

	public String log() {
		String ans = "";

		ans+="Clock number: " + clockNumber.toString() + "\n";

		ans += "Instruction memory: " + insMem.getOutput().get("data0") + "\n";
		ans += "Registers: " + regFile.getOutput().get("ins") + "\n";
		ans += "ALU: " + centralALU.getOutput().get("ins") + "\n";
		ans += "Data memory: " + dataMem.getOutput().get("ins") + "\n";
		ans += "Write back: " + MEM_WB.getOutput().get("ins") + "\n";

		ans+="ID_EX: " + ID_EX.getOutput().get("ins") + "\n";
		ans += "\n";
		return ans;
	}
}
