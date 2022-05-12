package telran.courses;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.*;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.courses.dto.Course;
import telran.courses.dto.LoginData;
import telran.courses.dto.LoginResponse;
import telran.courses.exceptions.BadRequestException;
import telran.courses.security.Account;
import telran.courses.security.AccountingManagement;
import telran.courses.service.CoursesService;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityTests {
	@Autowired
MockMvc mockMvc;
	@MockBean
	CoursesService coursesService;
	@MockBean
	PasswordEncoder passwordEncoder;
	@MockBean
	AccountingManagement accounting;
	String adminEmail = "admin@tel-ran.com";
	String userEmail = "user@tel-ran.com";
	Account userAccount = new Account(adminEmail, "user", "USER");
	Account adminAccount = new Account(userEmail, "admin", "ADMIN");
	
	
	static String userToken="xxx";
	static String adminToken;
	ObjectMapper mapper = new ObjectMapper();
	@BeforeEach
	void setUp() {
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(accounting.getAccount(adminEmail)).thenReturn(adminAccount);
		when(accounting.getAccount(userEmail)).thenReturn(userAccount);
		when(coursesService.updateCourse(anyInt(), any(Course.class))).thenThrow(BadRequestException.class);
		when(coursesService.addCourse(any(Course.class))).thenThrow(BadRequestException.class);
		when(coursesService.getCourse(anyInt())).thenThrow(BadRequestException.class);
		when(coursesService.getAllCourses()).thenThrow(BadRequestException.class);
		
	}
	@Test
	@Order(1)
	void loginTest() throws  Exception {
		
		LoginData loginDataAdmin = new LoginData(adminEmail, "xxxxxxxxxxxx");
		LoginData loginDataUser = new LoginData(userEmail, "xxxxxxxxxxxx");
		adminToken = loginRequest(loginDataAdmin);
		userToken = loginRequest(loginDataUser);
	}
	@Test
	
	void addCourse() throws  Exception {
		testAdmin(HttpMethod.POST, "/courses");
		
	}
	@Test
	void getCourse() throws  Exception {
		testAnyRole(HttpMethod.GET, "/courses/123");
		
	}
	@Test
	void getCourses() throws  Exception {
		testAnyRole(HttpMethod.GET, "/courses");
		
	}
	@Test
	void removeCourse() throws  Exception {
		
		testAdmin(HttpMethod.DELETE, "/courses/123");
	}
	@Test
	void updateCourse() throws  Exception {
		testAdmin(HttpMethod.PUT, "/courses/123");
		
	}
	private void performRequestAndExpect(HttpMethod method, String uri, ResultMatcher status, String authToken) throws  Exception {
		mockMvc.perform(request(method, uri).contentType("application/json")
				.content(mapper.writeValueAsString(new Course("", "", 0, 0, "")))
				.header("Authorization", authToken)).andDo(print()).andExpect(status);
	}
	
	private String loginRequest(LoginData loginData) throws Exception {
		String responseJson =  mockMvc.perform(post("/login").contentType("application/json")
				.content(mapper.writeValueAsString(loginData)))
		.andReturn().getResponse().getContentAsString();
		LoginResponse response = mapper.readValue(responseJson, LoginResponse.class);
		return response.accessToken;
	}
	private void testAnyRole(HttpMethod method, String uri) throws Exception{
		// security flow with adminToken
				performRequestAndExpect(method, uri, status().isBadRequest(), adminToken);
				/*************************************/
				// security flow with userToken
				performRequestAndExpect(method, uri, status().isBadRequest(), userToken);
				/***************************************/
				// security flow with wrong token
				performRequestAndExpect(method, uri, status().isForbidden(), "wrong-token");
	}
	private void testAdmin(HttpMethod method, String uri) throws Exception {
		// security flow with adminToken
				performRequestAndExpect(method, uri, status().isBadRequest(), adminToken);
				/*************************************/
				// security flow with userToken
				performRequestAndExpect(method, uri, status().isForbidden(), userToken);
				/***************************************/
				// security flow with wrong token
				performRequestAndExpect(method, uri, status().isForbidden(), "wrong-token");
	}
}
