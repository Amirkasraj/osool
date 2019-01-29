package compiler;

import java.util.Map;

public class MidRegister extends Module {
		
	public MidRegister(Module prev_) {
		super(prev_);
	}
	
	@Override
	public void clock() {
		
	}

	@Override
	protected Map<String, Integer> process(Map<String, Integer> input) {
		return input;
	}
}
