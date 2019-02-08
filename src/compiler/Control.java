package compiler;

import java.util.HashMap;
import java.util.Map;

public class Control extends Module {

    public Control(Module prev_){
        super(prev_);
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        if (input==null || input.size()==0)
            return null;
        HashMap<String,Integer> ans = new HashMap<>(input);
        int ins = input.get("data");
        System.out.println(ins);
        ans.remove("data");
        int opcode = (ins>>26);
        ins^= (opcode<<26);
        ans.put("opcode",opcode);
        int rs = (ins>>21);
        ins ^= (rs<<21);
        ans.put("rs",rs);
        int rt = (ins>>16);
        ins ^= (rt<<16);
        ans.put("rt",rt);
        if (opcode==0){ // r type
            int rd = (ins>>11);
            ins ^= (rd<<11);
            ans.put("rd",rd);
            int shamt = (ins>>6);
            ins ^= (shamt<<6);
            ans.put("shamt",shamt);
            int func = (ins);
            ans.put("func",func);
        }else {
            int immediate = ins;
            ans.put("immediate",immediate);
        }
        //System.out.println(ans);
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
