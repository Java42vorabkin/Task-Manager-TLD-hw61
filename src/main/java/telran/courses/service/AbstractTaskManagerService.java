package telran.courses.service;

import static telran.courses.api.ApiTaskManagerConstants.MAX_PID;
import static telran.courses.api.ApiTaskManagerConstants.MIN_PID;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractTaskManagerService implements TaskManagerService {
	abstract protected boolean exists(int id);

	protected int getPid() { 
		int pid = 0;
		var threadLocal = ThreadLocalRandom.current();
		do {
			pid = threadLocal.nextInt(MIN_PID, MAX_PID);
		} while(exists(pid));
		return pid;
	}

}
