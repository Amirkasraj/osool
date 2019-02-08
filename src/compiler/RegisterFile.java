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
        input.putAll(wb.getOutput());
        posteriorOutput = process(input);
    }
}
