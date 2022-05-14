package telran.courses.dto;

import static telran.courses.api.ApiTaskManagerConstants.*;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class InputGet implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Min(MIN_SORTING_TYPE) @Max(MAX_SORTING_TYPE)
	public int sortingType;

}
