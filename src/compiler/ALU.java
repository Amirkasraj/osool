package compiler;

import java.util.Map;

public class ALU extends Module {

	public ALU(Module prev_) {
		super(prev_);
	}

	@Override
	protected Map<String, Integer> process(Map<String, Integer> input) {

		int ALU_op1 = input.get("ALUOp1");
		int ALU_op2 = input.get("ALUOp2");
		int ALU_src = input.get("ALUSrc");
		int Reg_Dst = input.get("RegDst");// az rt biad 0 , ya az rd biad 1/
		int Reg_data1 = input.get("RegData1");
		int Reg_data2 = input.get("RegData2");
		int offset_data= input.get("Offset_Data");
		// integer binary ??!!! important!
		int rt_data = input.get("rt_Data");
		int rd_data = input.get("rd_Data");
		// int pc+4
		//int other control lines

		// TODO jam o zarb o ina;
		return null;
	}

}
