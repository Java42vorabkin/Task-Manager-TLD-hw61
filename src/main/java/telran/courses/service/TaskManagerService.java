package telran.courses.service;

import java.util.List;

import telran.courses.dto.Task;

public interface TaskManagerService {
	Task addTask(int approach, int priority);
	List<Task> getTasks(int sortingType);
	List<Task> kill(int pid, int priotity);
}
