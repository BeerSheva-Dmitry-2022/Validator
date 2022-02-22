package telran.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
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

	private static String check(Annotation annotation, Object fieldData) throws Exception {
		String nameMethod = "valid" + annotation.annotationType().getSimpleName();
		try {
			Method method = Validator.class.getDeclaredMethod(nameMethod, Annotation.class, Object.class);
			return (String) method.invoke(null, annotation, fieldData);
		} catch (Exception e) {
			return null;
		}
	}

	static String validNotNull(Annotation annotation, Object fieldData) throws Exception {
		return fieldData == null ? getMessage(annotation) : null;
	}

	static String validNotEmpty(Annotation annotation, Object fieldData) throws Exception {
		if (!(fieldData instanceof String)) {
			return "Wrong validation type, should be string";
		}
		return (fieldData == null || ((String) fieldData).isEmpty()) ? getMessage(annotation) : null;
	}

	static String validMin(Annotation annotation, Object fieldData) throws Exception {
		if (!(fieldData instanceof Number)) {
			return "Wrong validation type, should be number";
		}
		return Double.parseDouble(fieldData.toString()) < getValue(annotation) ? getMessage(annotation) : null;
	}

	static String validMax(Annotation annotation, Object fieldData) throws Exception {
		if (!(fieldData instanceof Number)) {
			return "Wrong validation type, should be number";
		}
		return Double.parseDouble(fieldData.toString()) > getValue(annotation) ? getMessage(annotation) : null;
	}

	static String validPast(Annotation annotation, Object fieldData) throws Exception {
		if (!(fieldData instanceof TemporalAccessor)) {
			return "Wrong validation type, should be LocalDate";
		}
		return (fieldData == null || LocalDateTime.now().isBefore(LocalDateTime.from((TemporalAccessor) fieldData)))
				? getMessage(annotation)
				: null;
	}

	static String validFuture(Annotation annotation, Object fieldData) throws Exception {
		if (!(fieldData instanceof TemporalAccessor)) {
			return "Wrong validation type, should be LocalDate";
		}
		return (fieldData == null || LocalDateTime.now().isAfter(LocalDateTime.from((TemporalAccessor) fieldData)))
				? getMessage(annotation)
				: null;
	}

	static String validEmail(Annotation annotation, Object fieldData) throws Exception {
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		return (fieldData != null && fieldData.toString().matches(regex)) ? "" : getMessage(annotation);
	}

	private static String getMessage(Annotation annotation) throws Exception {
		return (String) annotation.getClass().getDeclaredMethod("message").invoke(annotation);
	}

	private static int getValue(Annotation annotation) throws Exception {
		return (int) annotation.getClass().getDeclaredMethod("value").invoke(annotation);
	}

}
