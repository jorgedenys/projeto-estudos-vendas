package com.jdsjara.vendas.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.jdsjara.vendas.validation.constraintvalidation.NotEmptyListValidator;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyListValidator.class)
public @interface NotEmptyList {
	
	String message() default "A lista não pode ser vazia.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
}
