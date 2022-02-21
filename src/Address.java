import telran.validation.constraints.*;

public class Address {
	
	public Address(String city, String street, int aprt) {
		super();
		this.city = city;
		this.street = street;
		this.aprt = aprt;
	}
	
	
	@NotEmpty
	String city;
	@NotNull
	String street;
	@Min(1)
	int aprt;
	
	
	
}
