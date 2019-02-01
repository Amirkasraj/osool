package compiler;

import java.util.Map;

public class RegisterFile extends Memory {

    Module wb;

    public RegisterFile(Module prev_, Map<Integer,Integer> init, Module wb_) {
        super(prev_, init);
        wb = wb_;
    }

    @Override
    public void clock() {
        Input.putAll(wb.getOutput());
        posteriorOutput = process(Input);
        Input = prev.getOutput();
    }
}
