package compiler;

import java.util.HashMap;
import java.util.Map;

public class Control extends Module {

    public Control(Module prev_){
        super(prev_);
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        HashMap<String,Integer> ans = new HashMap<>(input);
        if (input == null || input.size()==0)
            return ans;

        int ins = input.get("data0");
        ans.put("ins",ins);
        int opcode = (ins>>26);
        ins^= (opcode<<26);
        ans.put("opcode",opcode);
        int rs = (ins>>21);
        ins ^= (rs<<21);
        ans.put("index0",rs);
        ans.put("write0",0);
        int rt = (ins>>16);
        ins ^= (rt<<16);
        ans.put("rt",rt);
        ans.put("index1",rt);
        ans.put("write1",0);
        int rd = (ins>>11);
        ins ^= (rd<<11);
        ans.put("rd",rd);
        int shamt = (ins>>6);
        ins ^= (shamt<<6);
        ans.put("shamt",shamt);
        int func = (ins);
        ans.put("func",func);
        int immediate = func + (shamt<<6) + (rd<<11);
        ans.put("immediate",immediate);
        if (opcode==0){ // r type
            //control lines
            ans.put("ALUOp1",0);
            ans.put("ALUOp2",1);
            ans.put("Branch",0);
            ans.put("MemRead",0);
            ans.put("MemWrite",0);
            ans.put("MemtoReg",0);
            ans.put("RegWrite",1);
            ans.put("ALUSrc",0);
            ans.put("RegDst",1);

        }else {
            if (opcode==35){// lw
                //control lines
                ans.put("ALUOp1",0);
                ans.put("ALUOp2",0);
                ans.put("Branch",0);
                ans.put("MemRead",1);
                ans.put("MemWrite",0);
                ans.put("MemtoReg",1);
                ans.put("RegWrite",1);
                ans.put("ALUSrc",1);
                ans.put("RegDst",0);
            }else if (opcode==43){// sw
                //control lines
                ans.put("ALUOp1",0);
                ans.put("ALUOp2",0);
                ans.put("Branch",0);
                ans.put("MemRead",0);
                ans.put("MemWrite",1);
                ans.put("MemtoReg",0);//x
                ans.put("RegWrite",0);
                ans.put("ALUSrc",1);
                ans.put("RegDst",0);//x
            }else if (opcode==4){// beq
                //control lines
                ans.put("ALUOp1",1);
                ans.put("ALUOp2",0);
                ans.put("Branch",1);
                ans.put("MemRead",0);
                ans.put("MemWrite",0);
                ans.put("MemtoReg",0);//x
                ans.put("RegWrite",0);
                ans.put("ALUSrc",0);
                ans.put("RegDst",0);//x
            }
        }
        return ans;

    }

    @Override
    public void clock() {
        if (input ==null) return;
        input = prev.getOutput();
        priorOutput = process(input);
    }

    @Override
    public void update() {
        return;
    }
}
