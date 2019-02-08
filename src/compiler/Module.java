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
		///?
	}

	protected Map<String, Integer> getOutput() {
		return priorOutput;
	}

	public void update() {
		priorOutput = posteriorOutput;
	}

	public int read(String key) {
		return priorOutput.get(key);
	}

	public void write(String key, int value) {
		input.put(key, value);
	}

	abstract protected Map<String, Integer> process(Map<String, Integer> input);

	public void setPrev(Module prev_) {
		prev = prev_;
	}

	public void addToInput(String key, int value){
		input.put(key,value);
	}
}
