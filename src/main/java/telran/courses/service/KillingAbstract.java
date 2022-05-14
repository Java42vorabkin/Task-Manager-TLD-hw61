package telran.courses.service;

import java.util.List;
import java.util.Map;

import telran.courses.dto.Task;

public abstract class KillingAbstract {
	private Integer par;
	
	public abstract List<Task> execute(Map<Integer, Task> tasks);
	public void setParameter(int par) {
		this.par = par;
	}
	protected Integer getPar() {
		return par;
	}
}
