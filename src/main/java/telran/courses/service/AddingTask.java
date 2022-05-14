package telran.courses.service;

import java.util.Map;

import telran.courses.dto.Task;

public interface AddingTask {
	Task execute(Task toBeAdded, Map<Integer, Task> tasks);
}
