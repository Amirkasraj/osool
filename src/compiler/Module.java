package compiler;

import java.util.HashMap;
import java.util.Map;

public abstract class Module {
	protected Map<String, Long> input,priorOutput, posteriorOutput;
	protected Module prev;

	public Module(Module prev_) {
		input = new HashMap<>();
		priorOutput = new HashMap<>();
		prev = prev_;
	}

	public void clock() {
		if (prev!=null)
			input.putAll(prev.getOutput());
		if (input ==null) return;
		posteriorOutput = process(input);
		priorOutput = posteriorOutput;
	}

	protected Map<String, Long> getOutput() {
		return priorOutput;
	}

	public void update() {
		input = new HashMap<>();
		return;
	}

	abstract protected Map<String, Long> process(Map<String, Long> input);


	public void addToInput(String key, Long value){
		input.put(key,value);
	}
}
