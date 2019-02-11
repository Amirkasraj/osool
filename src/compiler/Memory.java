package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memory extends Module{
    protected int arr[];

    public Memory(Module prev_, Map<Integer,Integer> init) {
        super(prev_);
        arr = new int[CPU.MAXMEM];
        for (int x:init.keySet())
            arr[x] = init.get(x);
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        if (input==null || !(input.containsKey("index0")))
            return null;
        HashMap<String, Integer> ans = new HashMap<>(input);

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
                arr[index] = data;
            } else {
                ans.put("data"+i.toString(), arr[index]);
            }
            i++;
        }
        return ans;
    }

}
