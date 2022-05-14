package telran.courses.service;

import java.util.List;
import java.util.Map;

import telran.courses.dto.Task;

public class KillAll extends KillingAbstract {

	@Override
	public List<Task> execute(Map<Integer, Task> tasks) {
		// TODO Auto-generated method stub
		return (List<Task>)(tasks.values());
	}

}
