package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CPU {
	private MidRegister PC, IF_ID, ID_EX, EX_MEM, MEM_WB;
	private Interpreter interpreter;
	private Memory insMem, dataMem;
	private RegisterFile regFile;
	private ALU centralALU;
	private Control control;

	private ArrayList<Module> array;

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

		PC = new MidRegister(null);
		PC.addToInput("index",0x00000010 / 4);
		insMem = new Memory(PC, init);
		IF_ID = new MidRegister(insMem);
		control = new Control(IF_ID);

		ID_EX = new MidRegister(null);
		centralALU = new ALU(ID_EX);
		EX_MEM = new MidRegister(centralALU);

		dataMem = new Memory(EX_MEM,init);
		MEM_WB = new MidRegister(dataMem);
		regFile = new RegisterFile(control, init, MEM_WB);
		ID_EX.setPrev(regFile);

		array = new ArrayList<>();
		array.add(PC);
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

		for (Module x: array) {
			x.clock();
			System.out.println(x.getClass());
		}
		System.out.println();
		for (Module x: array)
			x.update();

		if (centralALU.getOutput()!=null && centralALU.getOutput().containsKey("branch"))
			PC.addToInput("index", centralALU.getOutput().get("immediate"));
		else
			PC.addToInput("index", PC.getOutput().get("index")+1);

	}

	public Map<String, Integer> log() {
		return null;
		//az hame khorooji migire o mirize too map return mikone
	}
}
