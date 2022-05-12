package telran.courses.dto;

import javax.validation.constraints.*;

public class LoginData {
	/**
	 * @param email
	 * @param password
	 */

	@Email
	public String email;
	@Size(min = 7)
	public String password;

	public LoginData(@Email String email, @Size(min = 7) String password) {
		this.email = email;
		this.password = password;
	}

	public LoginData() {
	}
}
