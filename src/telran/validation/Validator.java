package telran.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.*;
import telran.validation.constraints.Valid;

public class Validator {
	public static List<String> validate(Object obj) {
		List<String> res = new ArrayList<>();
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();

		Arrays.stream(fields).forEach(field -> {
			field.setAccessible(true);
			Arrays.stream(field.getAnnotations()).forEach(annotation -> {
				try {
					if (annotation.annotationType() == Valid.class) {
						res.addAll(validate(field.get(obj)));
						return;
					}
					
					String message = check(annotation, field.get(obj));
					if (message != null) {
						res.add(message);
					}
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			});
		});
		return res;
	}

	private static String check(Annotation annotation, Object object) throws Exception {
		String nameMethod = "valid" + annotation.annotationType().getSimpleName();
		Method method = Validator.class.getDeclaredMethod(nameMethod, Annotation.class, Object.class);
		return (String) method.invoke(Validator.class, annotation, object);
	}

	@SuppressWarnings("unused")
	private static String validNotNull(Annotation annotation, Object object) throws Exception {
		return object == null ? getMessage(annotation) : null;
	}

	@SuppressWarnings("unused")
	private static String validNotEmpty(Annotation annotation, Object object) throws Exception {
		return ((String) object).length() == 0 ? getMessage(annotation) : null;
	}

	@SuppressWarnings("unused")
	private static String validMin(Annotation annotation, Object object) throws Exception {
		return object.getClass().getSimpleName().equals("Integer")
				? (int) object < getValue(annotation) ? getMessage(annotation) : null
				: (long) object < getValue(annotation) ? getMessage(annotation) : null;

	}

	@SuppressWarnings("unused")
	private static String validMax(Annotation annotation, Object object) throws Exception {
		return (long) object > getValue(annotation) ? getMessage(annotation) : null;
	}

	@SuppressWarnings("unused")
	private static String validPast(Annotation annotation, Object object) throws Exception {
		return LocalDate.now().isBefore((LocalDate) object) ? getMessage(annotation) : null;
	}

	@SuppressWarnings("unused")
	private static String validFuture(Annotation annotation, Object object) throws Exception {
		return LocalDate.now().isAfter((LocalDate) object) ? getMessage(annotation) : null;
	}

	@SuppressWarnings("unused")
	private static String validEmail(Annotation annotation, Object object) throws Exception {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		return ((String) object).matches(regex) ? "" : getMessage(annotation);
	}

	private static String getMessage(Annotation annotation) throws Exception {
		return (String) annotation.getClass().getDeclaredMethod("message").invoke(annotation);
	}

	private static int getValue(Annotation annotation) throws Exception {
		return (int) annotation.getClass().getDeclaredMethod("value").invoke(annotation);
	}

}
