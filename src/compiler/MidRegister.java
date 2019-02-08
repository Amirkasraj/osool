package compiler;

import java.util.Map;

public class MidRegister extends Module {
		
	public MidRegister(Module prev_) {
		super(prev_);
	}
	
	@Override
	public void clock() {
		super.clock();
		priorOutput = posteriorOutput;
	}

	@Override
	protected Map<String, Integer> process(Map<String, Integer> input) {
		return input;
	}

	@Override
	public void update() {
		return;
	}

	protected Map<String,Integer>log(){return null;}
}

