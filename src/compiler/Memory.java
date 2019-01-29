package compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Memory extends Module{

    ArrayList<Integer> arr;

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        HashMap<String, Integer> ans = new HashMap<>();
        int index = input.get("index");
        int write = input.get("write");
        if (write == 0){
            ans.put("data",arr.get(index));
        } else {
            ans.clear();
            // che konim ?
        }
        return ans;
    }

    public Memory(Module prev_) {
        super(prev_);
        arr = new ArrayList<>();
    }
}
