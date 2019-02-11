package compiler;

import java.util.HashMap;
import java.util.Map;

public abstract class Module {
	protected Map<String, Integer> input,priorOutput, posteriorOutput;
	protected Module prev;

	public Module(Module prev_) {
		input = new HashMap<>();
		priorOutput = new HashMap<>();
		prev = prev_;
	}

	public void clock() {
		if (prev!=null)
			input = prev.getOutput();
		if (input ==null) return;
		posteriorOutput = process(input);
		priorOutput = posteriorOutput;
	}

	protected Map<String, Integer> getOutput() {
		return priorOutput;
	}

	public void update() {
		return;
	}

	abstract protected Map<String, Integer> process(Map<String, Integer> input);


	public void addToInput(String key, int value){
		input.put(key,value);
	}
}
