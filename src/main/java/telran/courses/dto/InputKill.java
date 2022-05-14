package telran.courses.dto;

import static telran.courses.api.ApiTaskManagerConstants.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class InputKill implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Min(MIN_PID-1) @Max(MAX_PID)
	public int pid;
	@Min(MIN_PRIORITY-1) @Max(MAX_PRIORITY)
	public int priority;

}
