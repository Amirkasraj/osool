package compiler;

import java.util.HashMap;
import java.util.Map;

public class Forward extends Module{

    HashMap<String,Integer> data;

    public Forward(Module prev_) {
        super(prev_);
        data = new HashMap<>();
    }

    @Override
    protected Map<String, Integer> process(Map<String, Integer> input) {
        return null;
    }

    @Override
    public void clock() {
        input = prev.getOutput();
        String rd = Interpreter.to_binary_string(input.get("rd"));
        int value = input.get(rd);
        data.put(rd,value);
    }

    @Override
    public void update() {
        return;
    }

    @Override
    protected Map<String, Integer> getOutput() {
        return data;
    }
}

