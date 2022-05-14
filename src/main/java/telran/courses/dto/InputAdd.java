package telran.courses.dto;

import static telran.courses.api.ApiTaskManagerConstants.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class InputAdd implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Min(MIN_APPROACH) @Max(MAX_APPROACH)
	public int approach;
	@Min(MIN_PRIORITY) @Max(MAX_PRIORITY)
	public int priority;
}
