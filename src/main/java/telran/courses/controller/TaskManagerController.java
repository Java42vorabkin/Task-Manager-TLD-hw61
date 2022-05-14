package telran.courses.controller;


import java.util.List;

import javax.validation.*;
import javax.validation.constraints.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import telran.courses.dto.Course;
import telran.courses.dto.InputAdd;
import telran.courses.dto.InputGet;
import telran.courses.dto.InputKill;
import telran.courses.dto.Task;
import telran.courses.exceptions.BadRequestException;
import telran.courses.service.TaskManagerService;

import static telran.courses.api.ApiTaskManagerConstants.*;
@RestController
@RequestMapping("/tasks")
@Validated
@CrossOrigin
public class TaskManagerController {
	static Logger LOG = LoggerFactory.getLogger(TaskManagerController.class);
	
	@Autowired
	private TaskManagerService taskManagerService;
	
	@PostMapping
	Task addTask(@RequestBody @Valid InputAdd inpAdd) {
		Task addedTask = taskManagerService.addTask(inpAdd.approach, inpAdd.priority);
		if (addedTask.pid == null) {
			throw new RuntimeException("service has not generated id");
		}
		LOG.debug("added task with id {}, priority={} ", addedTask.pid, addedTask.priority);
		return addedTask;
	}
	
	@GetMapping
	List<Task> getTasks(@RequestBody @Valid InputGet inputGet) {
		List<Task> tasks = taskManagerService.getTasks(inputGet.sortingType);
		LOG.trace("getting {} tasks", tasks.size());
		return tasks;
	}
	
	@DeleteMapping
	List<Task> kill(@RequestBody @Valid InputKill inputKill) {
		List<Task> tasks = taskManagerService.kill(inputKill.pid, inputKill.priority);
		return tasks;
	}
}
