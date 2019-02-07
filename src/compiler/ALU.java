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
	private int branch;
	private int MemRead;
	private int MemWrite;
	private int MemtoReg;
	private int ALU_output;
	private int Pc_4;

	@Override
	protected Map<String, Integer> process(Map<String, Integer> input) {
        HashMap<String,Integer>ans=new HashMap<>();

        Pc_4=input.get("Pc_4");
        ALU_op1 = input.get("ALUOp1");
        ALU_op2 = input.get("ALUOp2");
        ALU_src = input.get("ALUSrc");
        Reg_Dst = input.get("RegDst");// az rt biad 0 , ya az rd biad 1/
		Reg_data1 = input.get("RegData1");
		int Reg_data2 = input.get("RegData2");
		int offset_data= input.get("Offset_Data");
		// integer binary ??!!! important!
		int rt_data = input.get("rt_Data");
		int rd_data = input.get("rd_Data");
		// int pc+4
		//int other control lines
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
		branch=input.get("Branch");
		MemRead=input.get("MemRead");
		MemWrite=input.get("MemWrite");
		MemtoReg=input.get("MemtoReg");

		ans.put("ALU_Result",ALU_output);
		ans.put("Zero",zero_control);
		ans.put("Branch",branch);
		ans.put("MemRead",MemRead);
		ans.put("MemWrite",MemWrite);
		ans.put("MemtoReg",MemtoReg);


		if (Reg_Dst==1){
			ans.put("BackWrite",rd_data);
		}else {
		    ans.put("BackWrite",rt_data);
        }



		// TODO jam o zarb o ina;
		return null;
	}

	public String ALU_control(int ALU_op1,int ALU_op2,int func){
		if (ALU_op1==0) {
            if (ALU_op2 == 0)
                return "add";
            return "subtract";
        }else{
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
		return "";
	}


	public Map<String,Integer>log(){
		HashMap<String,Integer>report = new HashMap<>();
		report.put("ALUOp1",ALU_op1);
		report.put("ALUOp2",ALU_op2);
		report.put("ALUSrc",ALU_src);
		report.put("RegDst",Reg_Dst);
		report.put("Branch",branch);
		report.put("MemRead",MemRead);
		report.put("MemWrite",MemWrite);
		report.put("MemtoReg",MemtoReg);

		report.put("ALU_input1",Reg_data1);
		report.put("ALU_input2",second_in);
		report.put("ALU_output",ALU_output);
		report.put("PC+4",Pc_4);

		return report;
	}

}
