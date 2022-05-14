package telran.courses.service;

import java.util.List;

import telran.courses.dto.Task;

public interface SortingAlgorithm {
	List<Task> execute(List<Task> tasks);
}
