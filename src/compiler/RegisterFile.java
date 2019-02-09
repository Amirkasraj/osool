package compiler;

import java.util.HashMap;
import java.util.Map;

public class RegisterFile extends Memory {

    Module wb;

    public RegisterFile(Module prev_, Map<Integer,Integer> init, Module wb_) {
        super(prev_, init);
        wb = wb_;
    }

    @Override
    public void clock() {
        input = prev.getOutput();
        if (input==null) input = new HashMap<>();
        posteriorOutput = process(input);
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        if (input==null || input.size()==0)
            return null;
        HashMap <String,Integer> ans = new HashMap<>(input);
        Integer rs = input.get("rs");
        Integer rt = input.get("rt");
        Integer rd = input.get("rd");
        ans.put(rs.toString(),arr[rs]);
        ans.put(rt.toString(),arr[rt]);
        if (wb.getOutput().containsKey("RegWrite")){
            Integer index = wb.getOutput().get("rd");
            int value = wb.getOutput().get(index.toString());
            arr[index] = value;
        }
        return ans;
    }
}
