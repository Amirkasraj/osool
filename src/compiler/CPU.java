package compiler;

import java.util.*;

public class CPU {
	private  MidRegister IF_ID, ID_EX, EX_MEM, MEM_WB;
	private Interpreter interpreter;
	private Memory insMem, dataMem, regFile;
	private ALU centralALU;
	private Control control;
	private Long PC = 0x00000010L / 4 -1;
	private Map<String,Long> forwarding;
	private Map<Long,Long> writeBack;
	private Long clockNumber=0L;
	private int WAIT = 0;
	private Long temp = 0L;

	private ArrayList<Module> array;
	public Set<Long> branch_set = new HashSet<>();

	public static boolean HAZARD = true;

	private String[] controls = {"ALUOp1","ALUOp2","Branch","MemRead","MemWrite","MemtoReg","RegWrite","ALUSrc","RegDst"};

	public CPU(Interpreter inter_) {
		interpreter = inter_;

		Map<Long,Long> init = interpreter.load(branch_set);

		insMem = new Memory(null, init);
		IF_ID = new MidRegister(insMem);
		control = new Control(IF_ID);
		init = new HashMap<>();
		init.put(29L,0x00FF0000L);
		regFile = new Memory(control, init);
		ID_EX = new MidRegister(regFile);
		centralALU = new ALU(ID_EX);
		EX_MEM = new MidRegister(centralALU);
		init = new HashMap<>();
		init.put(0x00000100L,100L);
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

		writeBack = new HashMap<>();
		writeBack.put(-1L, MEM_WB.getOutput().get("ins"));

		// LW
		if (MEM_WB.getOutput().containsKey("RegWrite") && MEM_WB.getOutput().get("RegWrite")==1 && MEM_WB.getOutput().get("MemtoReg") == 1) {
			Long value = MEM_WB.getOutput().get("data0");
			Long key = MEM_WB.getOutput().get("rt");
			forwarding.put(key.toString(), value);
			writeBack.put(key, value);
		}

		// R
		if (centralALU.getOutput().containsKey("RegDst") && centralALU.getOutput().get("RegDst")==1) {
			Long key = centralALU.getOutput().get("rd");
			Long value = centralALU.getOutput().get("ALU_Result");
			forwarding.put(key.toString(), value);
			writeBack.put(key, value);
		}

		// BEQ?
		if (HAZARD) {
			if (branch_set.contains(PC)) {
				WAIT = 2;
				System.out.println("GINKS");
				temp = PC;
				PC=0L;
			}
		}

		// BUBBLE
		if (WAIT > 0) {
			insMem.addToInput("write0",0L);
			insMem.addToInput("index0",0L);
			insMem.addToInput("pc_4",1L);
			clockNumber++;
			clockElements();
			WAIT--;
			if (WAIT==0) PC=temp+1;
			return;
		}

		// PC
		if (centralALU.getOutput()!=null && centralALU.getOutput().containsKey("Branch")
				&& centralALU.getOutput().get("Branch")==1 &&  centralALU.getOutput().containsKey("goto")
				&& centralALU.getOutput().get("goto")==1)
			PC = centralALU.getOutput().get("Branch_data");
		else
			if (centralALU.getOutput()!=null && centralALU.getOutput().containsKey("Branch")
				&& centralALU.getOutput().get("Branch")==1 && centralALU.getOutput().containsKey("pc_4"))
				PC = centralALU.getOutput().get("pc_4");
			else PC++;

		// WB
		for(Long x: writeBack.keySet()) {
			if (x<0)
				continue;
			regFile.addToInput("index2",x);
			regFile.addToInput("write2",1L);
			regFile.addToInput("data2",writeBack.get(x));
		}

		// Hazard
		if (HAZARD){
			for (String key: forwarding.keySet())
				centralALU.addToInput(key,forwarding.get(key));
		}

		insMem.addToInput("write0",0L);
		insMem.addToInput("index0",PC);
		insMem.addToInput("pc_4",PC+1L);
		clockNumber++;
		clockElements();


	}

	private void clockElements(){
		for (Module x: array)
			x.clock();
		for (Module x: array)
			x.update();
	}

	public String log() {
		String ans = "";

		ans += "Clock number: " + clockNumber.toString() + "\n";

		ans += "	Pipeline: \n";
		ans += "		IF : " + Interpreter.to_binary_string(insMem.getOutput().get("data0"),32) + "\n";
		ans += "		ID : " + Interpreter.to_binary_string(regFile.getOutput().get("ins"),32) + "\n";
		ans += "		EX : " + Interpreter.to_binary_string(centralALU.getOutput().get("ins"),32) + "\n";
		ans += "		MEM: " + Interpreter.to_binary_string(dataMem.getOutput().get("ins"),32) + "\n";
		ans += "		WB : " + Interpreter.to_binary_string(writeBack.get(-1L),32) + "\n";
		ans += "\n";

		ans += "	Middle registers:\n";
		ans+="		IF/ID: \n";
		ans += "			PC+4: " + IF_ID.getOutput().get("pc_4") + "*4\n";

		ans+="		ID/EX: \n";
		ans += "			PC+4: " + ID_EX.getOutput().get("pc_4") + "*4\n";
		ans += "			rs: " + Interpreter.to_binary_string(ID_EX.getOutput().get("rs"),5) + "\n";
		ans += "			rt: " + Interpreter.to_binary_string(ID_EX.getOutput().get("rt"),5) + "\n";
		ans += "			immediate sign extended: " + Interpreter.to_binary_string(ID_EX.getOutput().get("immediex"),32) + "\n";

		ans+="		EX/MEM: \n";
		ans += "			PC+4: " + EX_MEM.getOutput().get("pc_4") + "*4\n";
		ans += "			ALU result: " + EX_MEM.getOutput().get("ALU_Result") + "\n";
		ans += "			branch or not: " + EX_MEM.getOutput().get("goto") + "\n";
		ans += "			branch destination: " + EX_MEM.getOutput().get("Branch_data") + "\n";

		ans+="		MEM/WB: \n";
		ans += "			PC+4: " + MEM_WB.getOutput().get("pc_4") + "*4\n";
		ans += "			Memory data: "+MEM_WB.getOutput().get("data0") + "\n";
		ans += "\n";

		ans += "	Pipeline control values:\n";
		ans += "		ID/EX:\n";
		for(String c: controls)
			ans += "			"+c +": " + ID_EX.getOutput().get(c) + "\n";
		ans += "		EX/MEM:\n";
		for(String c: controls)
			ans += "			"+c +": " + EX_MEM.getOutput().get(c) + "\n";
		ans += "		MEM/WB:\n";
		for(String c: controls)
			ans += "			"+c +": " + MEM_WB.getOutput().get(c) + "\n";
		ans += "\n";

		ans += "	Register file data:\n";
		ans += "		Register data 0: " + regFile.getOutput().get("data0") +"\n";
		ans += "		Register data 1: " + regFile.getOutput().get("data1") +"\n";
		ans += "\n";

		ans += "	Memory data: " + dataMem.getOutput().get("data0") +"\n";
		ans += "\n";

		ans += "	ALUs:\n";
		ans += "		Main ALU:\n";
		Long rs = centralALU.getOutput().get("rs");
		Long rt = centralALU.getOutput().get("rt");
		String valueS = "null";
		if (rs!=null) valueS = centralALU.getOutput().get(rs.toString()).toString();
		String valueT = "null";
		if (rs!=null) valueT = centralALU.getOutput().get(rt.toString()).toString();
		ans += "			Inputs: " + valueS + ", " + valueT +  "\n";
		ans += "			Output: " + centralALU.getOutput().get("ALU_Result") + "\n";

		Long value = -1L;
		if (centralALU.getOutput().get("immediate")!=null)
			value = centralALU.getOutput().get("immediate") + centralALU.getOutput().get("pc_4");
		ans += "		Branch ALU: " + value + "\n";
		ans += "		PC ALU: "+ (PC+1) + "*4\n";
		ans += "\n";

		ans += "	Register values: \n";
		for (Long i=0L;i<=4L;i++) {
			String x = "$t" + i.toString();
			ans += "		"+x+": "+ regFile.read(i+8L) + "\n";
		}
		ans += "		$sp: " + regFile.read(29L) +"\n";
		ans += "		$ra: " + regFile.read(31L) +"\n";

		ans += "	Forwarding: \n";
		ans += "		" + forwarding.toString() + "\n";

		ans += "-----------------------------\n";
		return ans;
	}
}
