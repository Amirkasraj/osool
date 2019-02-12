package compiler;

import java.util.HashMap;
import java.util.Map;

public class ALU extends Module {


	public ALU(Module prev_) {
		super(prev_);
	}
	private Long ALU_op1;
	private Long ALU_op2;
	private Long ALU_src;
	private Long Reg_Dst;
	private Long Reg_data1;
	private Long second_in;
	private Long ALU_output;
	private Long pc_4;

	@Override
	protected Map<String, Long> process(Map<String, Long> input) {
		System.out.println(input);
		HashMap<String,Long>ans=new HashMap<>(input);
		if (input == null || input.size()==0) return ans;

        pc_4=input.get("pc_4");
        ALU_op1 = input.get("ALUOp1");
        ALU_op2 = input.get("ALUOp2");
        ALU_src = input.get("ALUSrc");
        Reg_Dst = input.get("RegDst");// az rt biad 0 , ya az rd biad 1/
		Long rs = input.get("rs");
		Long rt = input.get("rt");
		Long rd = input.get("rd");
		System.out.println(rs + " " + rt + " " + rd);
		Reg_data1 = input.get("data0");
		Long Reg_data2 = input.get("data1");
		ans.remove("data0");
		ans.remove("data1");
		ans.put(rs.toString(),Reg_data1);
		ans.put(rt.toString(),Reg_data2);
		Long offset_data= input.get("immediex");
		Long branch_data = pc_4 + offset_data;
		ans.put("Branch_data",branch_data);
		second_in=Reg_data2;
		ALU_output=0b0L;
		Long zero_control=0L;
		if (ALU_src==1L)
			second_in=offset_data;
		Long func=input.get("func");
		switch (ALU_control(ALU_op1,ALU_op2,func)){
			case "add":
				System.out.println(ALU_output+ " " + Reg_data1+ " " + second_in);
				ALU_output=Reg_data1+second_in;
			break;
			case "subtract":
				ALU_output=Reg_data1-second_in;
				if (ALU_output==0L)
					zero_control = 1L;
			break;
			case "and":
				ALU_output=Reg_data1&second_in;
			break;
			case "or":
				ALU_output=Reg_data1|second_in;
			break;
			case "nor":
				ALU_output=~(Reg_data1|second_in);
			break;
			case "slt":
				if (Reg_data1 < second_in){
					ALU_output=0b1L;
				}
			break;
		}
		ans.put("ALU_Result",ALU_output);
		ans.put("goto",zero_control);

		if (Reg_Dst==1L){
			ans.put("wb",rd);
		}else {
			ans.put("wb", rt);
			ans.put("index0",ALU_output);
			ans.put("write0",input.get("MemWrite"));
			if (input.get("MemWrite")==1)
				ans.put("data0",rt);
		}

		priorOutput = ans;
		return ans;
	}

	public String ALU_control(Long ALU_op1,Long ALU_op2,Long func){
		if (ALU_op1 == 0L) {
            if (ALU_op2 == 1L)
                return "subtract";
            return "add";
        }else{
			if (ALU_op2.equals(0L)){
				if (func==0b100000L)
					return "add";
				if (func==0b100010L)
					return "subtract";
				if (func==0b100100L)
					return "and";
				if (func==0b100101L)
					return "or";
				if (func==0b100111L)
					return "nor";
				if (func==0b101010L)
					return "slt";
			}
		}
		return "";
	}

}
