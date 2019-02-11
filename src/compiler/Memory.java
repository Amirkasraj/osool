package compiler;

import java.util.HashMap;
import java.util.Map;

public class Memory extends Module{
    private Map<Integer,Integer> mem;

    public Memory(Module prev_, Map<Integer,Integer> init) {
        super(prev_);
        mem = new HashMap<>(init);
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        HashMap<String, Integer> ans = new HashMap<>(input);
        if (input == null || input.size()==0) return ans;

        Integer i=0;
        while (true) {
            if (!input.containsKey("index"+i.toString()))
                break;
            int index = input.get("index"+i.toString());
            ans.remove("index"+i.toString());
            int write = input.get("write"+i.toString());
            ans.remove("write"+i.toString());
            if (write==1) {
                int data = input.get("data"+i.toString());
                ans.remove("data"+i.toString());
                mem.put(index,data);
            } else {
                Integer value = mem.get(index);
                if (value==null) value=0;
                ans.put("data"+i.toString(), value);
            }
            i++;
        }
        return ans;
    }

}
