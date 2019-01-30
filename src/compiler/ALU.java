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
		int second_in=Reg_data2;
		int ALU_output=0b0;
		int zero_control=0;
		if (ALU_src==1)
			second_in=offset_data;
		int func=input.get("func");
		switch (ALU_control(ALU_op1,ALU_op2,func)){
			case "add":
				ALU_output=Reg_data1+second_in;
			break;
			case "subtract":
				ALU_output=Reg_data1-second_in;
				if (ALU_output==0b0)
					zero_control=1;
			break;
			case "and":
				ALU_output=Reg_data1&second_in;
			break;
			case "or":
				ALU_output=Reg_data1|second_in;
			break;
			case "slt":
				if (Reg_data1 < second_in){
					ALU_output=0b1;
				}
			break;
		}

		if (Reg_Dst==1){
			//TODO multi....
		}



		// TODO jam o zarb o ina;
		return null;
	}
	public String ALU_control(int ALU_op1,int ALU_op2,int func){
		if (ALU_op1==0){
			if (ALU_op2==0){
				return "add";
			return "subtract";
		}else {
			if (ALU_op2==0){
				if (func==0b100000)
					return "add";
				if (func==0b100010)
					return "subtract";
				if (func==0b100100)
					return "and";
				if (func==0b100101)
					return "or";
				if (func==0b101010)
					return "slt";
			}
		}
	}


}
