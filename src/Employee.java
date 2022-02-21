import java.time.LocalDate;

import telran.validation.constraints.*;

public class Employee {
	public Employee(long id, String email, String name, LocalDate birthDate, LocalDate jobFinishDate, Address address) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.birthDate = birthDate;
		this.jobFinishDate = jobFinishDate;
		this.address = address;
	}
	private static final int MAX_VALUE = 9_999_999;
	private static final int MIN_VALUE = 1_000_000;
	@Max(value = MAX_VALUE, message = "id of emp can not be great than " + MAX_VALUE)
	@Min(value = MIN_VALUE, message = "id of emp can not be less than " + MIN_VALUE)
	long id;
	@Email
	String email;
	@NotEmpty
	String name;
	@Past
	LocalDate birthDate;
	@Future
	LocalDate jobFinishDate;
	@Valid
	Address address;
	
}
