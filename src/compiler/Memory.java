package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memory extends Module{
    private int arr[];

    public Memory(Module prev_, Map<Integer,Integer> init) {
        super(prev_);
        arr = new int[CPU.MAXMEM];
        for (int x:init.keySet())
            arr[x] = init.get(x);
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        HashMap<String, Integer> ans = new HashMap<>();
        int index = input.get("index");
        int write = input.get("write");
        if (write == 0){
            ans.put("data",arr[index]);
        } else {
            ans.clear();
            // che konim ?
        }
        return ans;
    }

}
