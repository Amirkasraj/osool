package compiler;

import java.util.HashMap;
import java.util.Map;

public abstract class Module {
	protected Map<String, Integer> Input,priorOutput, posteriorOutput;
	protected Module prev;

	public Module(Module prev_) {
		Input = new HashMap<>();
		priorOutput = new HashMap<>();
		prev = prev_;
	}

	public void clock() {
		posteriorOutput = process(Input);
		Input = prev.getOutput();
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
		Input.put(key, value);
	}

	abstract protected Map<String, Integer> process(Map<String, Integer> input);
}
