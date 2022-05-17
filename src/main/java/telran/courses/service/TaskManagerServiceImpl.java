package telran.courses.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import static telran.courses.api.ApiTaskManagerConstants.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import telran.courses.dto.Task;

@Service
public class TaskManagerServiceImpl extends AbstractTaskManagerService {
	// Key - PID, value - task
	private Map<Integer, Task> tasks = new HashMap<>();
	// Key - approach, value - adding algorithm
	private Map<Integer, AddingTask> addingTask = new HashMap<>();
	// Key - sorting type, Value - sorting algorithm 
	private Map<Integer, SortingAlgorithm> sortingAlgo = new HashMap<>();
	// Key - kill type, Value - killing algorithm 
	private Map<Integer, KillingAbstract> killAlgo = new HashMap<>();
	
	protected boolean exists(int pid) {
		return tasks.containsKey(pid);

	}
	
	int addingNaive = 0;
	int addingFIFO = 1;
	int addingPriority = 2;
	int sortByTime = 0;
	int sortByPriority = 1;
	int sortByPid = 2;
	int killByPid = 0;
	int killByPriority = 1;
	int killAll = 2;
	
	public TaskManagerServiceImpl() {
		addingTask.put(addingNaive, new AddingTaskByNaive());
		addingTask.put(addingFIFO, new AddingTaskByFIFO());
		addingTask.put(addingPriority, new AddingTaskByPriority());
		sortingAlgo.put(sortByTime, new SortingByTime());
		sortingAlgo.put(sortByPriority, new SortingByPriority());
		sortingAlgo.put(sortByPid, new SortingByPID());
		killAlgo.put(killByPid, new KillByPid());
		killAlgo.put(killByPriority, new KillByPriority());
		killAlgo.put(killAll, new KillAll());
	}
	
	@Override
	public Task addTask(int approach, int priority) {
		LocalDateTime currentTime = LocalDateTime.now();
		Task newTask = new Task(priority, currentTime.toString());
		return addingTask.get(approach).execute(newTask, tasks);
	}
	@Override
	public List<Task> getTasks(int sortingType) {
		SortingAlgorithm algo = sortingAlgo.get(sortingType);
		List<Task> listOfTasks = algo.execute((List<Task>)(tasks.values()));
		return listOfTasks;
	}
	@Override
	public List<Task> kill(int pid, int priotity) {
		KillingAbstract algo;
		if(pid > (MIN_PID-1)) {
			algo = killAlgo.get(killByPid);
			algo.setParameter(pid);
		} else if(priotity > MIN_PRIORITY-1 ) {
			algo = killAlgo.get(killByPriority);
			algo.setParameter(priotity);
		} else {
			algo = killAlgo.get(killAll);
		}		
		List<Task> killedTasks = algo.execute(tasks);
		return killedTasks;
	}

}
