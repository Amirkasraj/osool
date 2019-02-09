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
        if (input==null || !(input.containsKey("index")))
            return null;
        HashMap<String, Integer> ans = new HashMap<>(input);

        int index = input.get("index");
        //ans.remove("index");
        if (input.containsKey("write")) {
            ans.remove("write");
            int data = input.get("data");
            ans.remove("data");
            arr[index] = data;
        } else {
            ans.put("data",arr[index]);
        }

        return ans;
    }

}
