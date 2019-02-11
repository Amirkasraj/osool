package compiler;

import java.util.HashMap;
import java.util.Map;

public class ALU extends Module {


	public ALU(Module prev_) {
		super(prev_);
	}
	private int ALU_op1;
	private int ALU_op2;
	private int ALU_src;
	private int Reg_Dst;
	private int Reg_data1;
	private int second_in;
	private int ALU_output;
	private int pc_4;

	@Override
	protected Map<String, Integer> process(Map<String, Integer> input) {
		HashMap<String,Integer>ans=new HashMap<>(input);
		if (input == null || input.size()==0) return ans;

        pc_4=input.get("pc_4");
        ALU_op1 = input.get("ALUOp1");
        ALU_op2 = input.get("ALUOp2");
        ALU_src = input.get("ALUSrc");
        Reg_Dst = input.get("RegDst");// az rt biad 0 , ya az rd biad 1/
		Integer rs = input.get("rs");
		Integer rt = input.get("rt");
		Integer rd = input.get("rd");
		Reg_data1 = input.get("data0");
		int Reg_data2 = input.get("data1");
		ans.remove("data0");
		ans.remove("data1");
		ans.put(rs.toString(),Reg_data1);
		ans.put(rt.toString(),Reg_data2);
		int offset_data= input.get("immediate");
		int branch_data = pc_4 + offset_data;
		ans.put("Branch_data",branch_data);
		second_in=Reg_data2;
		ALU_output=0b0;
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
		ans.put("ALU_Result",ALU_output);
		ans.put("goto",zero_control);

		if (Reg_Dst==1){
			ans.put("wb",rd);
		}else {
			ans.put("wb", rt);
			ans.put("index0",ALU_output);
			ans.put("write0",input.get("MemWrite"));
			if (input.get("MemWrite")==1)
				ans.put("data0",rt);
		}

		return ans;
	}

	public String ALU_control(int ALU_op1,int ALU_op2,int func){
		if (ALU_op1==1) {
            if (ALU_op2 == 0)
                return "add";
            return "subtract";
        }else{
			if (ALU_op2==1){
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
		return "";
	}

}
