package telran.courses;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.courses.dto.Course;
import telran.courses.service.CoursesService;
import static telran.courses.api.ApiConstants.*;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"app.security.enable=false"})
class ControllerNoSecurityTests {
	private static final Integer COURSE_ID = MIN_ID;
	private static final int LESS_THAN_MIN_ID = MIN_ID - 1;
	private static final int GREATER_THAN_MAX_ID = MAX_ID + 1;
	@Autowired
MockMvc mockMvc;
	@MockBean
	CoursesService coursesService;
	static Course course;
	static String wrongCourseJsonHours;
	static String correctCourseJson;
	static String courseJsonResponse;
	static String emptyListJson = "[]";
	static String wrongCourseJsonCost;
	static String wrongCourseJsonDate;
	static {
		ObjectMapper mapper = new ObjectMapper();
		course = new Course("course","lecturer",MIN_HOURS, MAX_COST, LocalDate.now().toString());
		
		try {
			
			course.id = COURSE_ID;
			correctCourseJson = mapper.writeValueAsString(course);
			courseJsonResponse = mapper.writeValueAsString(course);
			Course wrongCourse = new Course("course","lecturer",MIN_HOURS - 1, MAX_COST, LocalDate.now().toString());
			wrongCourseJsonHours = mapper.writeValueAsString(wrongCourse);
			wrongCourse.hours = MIN_HOURS;
			wrongCourse.cost = MAX_COST + 1;
			wrongCourseJsonCost = mapper.writeValueAsString(wrongCourse);
			wrongCourse.cost = MAX_COST;
			wrongCourse.openingDate = "1999/12/12";
			wrongCourseJsonDate = mapper.writeValueAsString(wrongCourse);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
	}
	@Test
	void addCourse() throws  Exception {
		when(coursesService.addCourse(any(Course.class))).thenReturn(course);
		assertEquals(courseJsonResponse, performCorrectRequest(HttpMethod.POST, "/courses"));
		wrongContentRequests();
				
		
	}
	private void wrongContentRequests() throws Exception {
		performWrongRequest(HttpMethod.POST, "/courses", wrongCourseJsonHours);
		performWrongRequest(HttpMethod.POST, "/courses", wrongCourseJsonCost);
		performWrongRequest(HttpMethod.POST, "/courses", wrongCourseJsonDate);
	}
	@Test
	void updateCourse() throws Exception {
		when(coursesService.updateCourse(anyInt(),any(Course.class))).thenReturn(course);
		assertEquals(courseJsonResponse, performCorrectRequest(HttpMethod.PUT, "/courses/" + MIN_ID));
		wrongContentRequests();
	}
	@Test
	void getCourse() throws Exception {
		when(coursesService.getCourse(anyInt())).thenReturn(course);
		assertEquals(courseJsonResponse, performCorrectRequest(HttpMethod.GET, "/courses/" + MIN_ID));
		wrongCourseIdRequests();
	}
	private void wrongCourseIdRequests() throws Exception {
		performWrongRequest(HttpMethod.GET, "/courses/" + LESS_THAN_MIN_ID, correctCourseJson);
		performWrongRequest(HttpMethod.GET, "/courses/" + GREATER_THAN_MAX_ID, correctCourseJson);
	}
	@Test
	void removeCourse() throws Exception {
		when(coursesService.removeCourse(anyInt())).thenReturn(course);
		assertEquals(courseJsonResponse, performCorrectRequest(HttpMethod.DELETE, "/courses/" + MIN_ID));
		wrongCourseIdRequests();
	}
	@Test
	void getCourses() throws Exception {
		when(coursesService.getAllCourses()).thenReturn(new ArrayList<Course>());
		assertEquals(emptyListJson, performCorrectRequest(HttpMethod.GET, "/courses/"));
		
	}
	
	private String performCorrectRequest(HttpMethod method, String uri) throws  Exception {
		return mockMvc.perform(request(method, uri).contentType("application/json")
				.content(correctCourseJson)).andDo(print()).andExpect(status().isOk()).andReturn()
		.getResponse().getContentAsString();
				
	}
	private void performWrongRequest(HttpMethod method, String uri, String wrongCourseJson) throws  Exception {
		mockMvc.perform(request(method,uri).contentType("application/json").content(wrongCourseJson))
				.andDo(print()).andExpect(status().isBadRequest());
	}

}
