package compiler;

import java.util.HashMap;
import java.util.Map;

public class Memory extends Module{
    private Map<Long,Long> mem;

    public Memory(Module prev_, Map<Long,Long> init) {
        super(prev_);
        mem = new HashMap<Long, Long>(init);
    }

    @Override
    protected Map<String, Long> process(Map<String, Long> input) {
        HashMap<String, Long> ans = new HashMap<>(input);
        if (input == null || input.size()==0) return ans;

        Integer i=0;
        while (true) {
            if (!input.containsKey("index"+i.toString()))
                break;
            Long index = input.get("index"+i.toString());
            ans.remove("index"+i.toString());
            Long write = input.get("write"+i.toString());
            ans.remove("write"+i.toString());
            if (write==1) {
                Long data = input.get("data"+i.toString());
                ans.remove("data"+i.toString());
                mem.put(index,data);
            } else {
                Long value = mem.get(index);
                if (value==null) value=0L;
                ans.put("data"+i.toString(), value);
            }
            i++;
        }
        return ans;
    }

    public Long read(Long index) {
        Long ans= mem.get(index);
        if (ans==null) ans = 0L;
        return ans;
    }

}
