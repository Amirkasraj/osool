package compiler;

import java.util.HashMap;
import java.util.Map;

public class Control extends Module {

    public Control(Module prev_){
        super(prev_);
    }

    @Override
    protected Map<String, Long> process(Map<String, Long> input) {
        HashMap<String,Long> ans = new HashMap<>(input);
        if (input == null || input.size()==0)
            return ans;

        Long ins = input.get("data0");
        input.remove("data0");
        ans.put("ins",ins);
        Long opcode = (ins>>26);
        ins^= (opcode<<26);
        ans.put("opcode",opcode);
        Long rs = (ins>>21);
        ins ^= (rs<<21);
        ans.put("rs",rs);
        ans.put("index0",rs);
        ans.put("write0",0l);
        Long rt = (ins>>16);
        ins ^= (rt<<16);
        ans.put("rt",rt);
        ans.put("index1",rt);
        ans.put("write1",0l);
        Long rd = (ins>>11);
        ins ^= (rd<<11);
        ans.put("rd",rd);
        Long shamt = (ins>>6);
        ins ^= (shamt<<6);
        ans.put("shamt",shamt);
        Long func = (ins);
        ans.put("func",func);
        Long immediate = (func + (shamt<<6) + (rd<<11));
        Long immediateExtended = immediate;
        if ((immediate>>15)%2==1) {
            immediate -= (1 << 16);
            for (int i=16;i<32;i++)
                immediateExtended+=(1L<<i);
            immediateExtended -= (1L<<32);
        }
        ans.put("immediate",immediate);
        ans.put("immediex",immediateExtended);
        if (opcode==0){ // r type
            //control lines
            ans.put("ALUOp1",0L);
            ans.put("ALUOp2",1L);
            ans.put("Branch",0L);
            ans.put("MemRead",0L);
            ans.put("MemWrite",0L);
            ans.put("MemtoReg",0L);
            ans.put("RegWrite",1L);
            ans.put("ALUSrc",0L);
            ans.put("RegDst",1L);

        }else {
            if (opcode==35){// lw
                //control lines
                ans.put("ALUOp1",0L);
                ans.put("ALUOp2",0L);
                ans.put("Branch",0L);
                ans.put("MemRead",1L);
                ans.put("MemWrite",0L);
                ans.put("MemtoReg",1L);
                ans.put("RegWrite",1L);
                ans.put("ALUSrc",1L);
                ans.put("RegDst",0L);
            }else if (opcode==43){// sw
                //control lines
                ans.put("ALUOp1",0L);
                ans.put("ALUOp2",0L);
                ans.put("Branch",0L);
                ans.put("MemRead",0L);
                ans.put("MemWrite",1L);
                ans.put("MemtoReg",0L);//x
                ans.put("RegWrite",0l);
                ans.put("ALUSrc",1l);
                ans.put("RegDst",0l);//x
            }else if (opcode==4){// beq
                //control lines
                ans.put("ALUOp1",1l);
                ans.put("ALUOp2",0l);
                ans.put("Branch",1l);
                ans.put("MemRead",0l);
                ans.put("MemWrite",0l);
                ans.put("MemtoReg",0l);//x
                ans.put("RegWrite",0l);
                ans.put("ALUSrc",0l);
                ans.put("RegDst",0l);//x
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
