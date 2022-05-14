package telran.courses.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import static telran.courses.api.ApiTaskManagerConstants.*;

public class Task implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Integer pid;
	@Min(MIN_PRIORITY) @Max(MAX_PRIORITY)
	public int priority;
	public String creationTime;
	public Task(int priority, String creationTime) {
		this.priority = priority;
		this.creationTime = creationTime;
	}
	Task() {
		
	}
	@Override
	public int hashCode() {
		return Objects.hash(pid, priority, creationTime);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(pid, other.pid) && priority == other.priority && 
				Objects.equals(creationTime, other.creationTime);
}
