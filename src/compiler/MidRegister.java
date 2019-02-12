package compiler;

import java.util.HashMap;
import java.util.Map;

public class MidRegister extends Module {
		
	public MidRegister(Module prev_) {
		super(prev_);
	}

	@Override
	public void clock() {
		if (prev!=null)
			input.putAll(prev.getOutput());
		if (input ==null) return;
		posteriorOutput = process(input);
	}

	@Override
	protected Map<String, Long> process(Map<String, Long> input) {
		return input;
	}

	@Override
	public void update() {
		priorOutput = posteriorOutput;
		input = new HashMap<>();
	}

	protected Map<String,Integer>log(){return null;}
}

