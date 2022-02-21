import java.time.LocalDate;

import telran.validation.Validator;

public class FrameworkValidator {

	public static void main(String[] args) {
		Employee emp = new Employee(1000000, "google@gmail.com", "Bob", LocalDate.of(1990, 1, 1), LocalDate.of(2023,3,1), new Address("B7", "yaelim", 100));
		System.out.println(Validator.validate(emp));
		

	}

}
