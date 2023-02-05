package com.jdsjara.vendas.validation.constraintvalidation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.jdsjara.vendas.validation.NotEmptyList;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List> {

	@Override
	public boolean isValid(List list, 
						   ConstraintValidatorContext context) {
		return list != null && !list.isEmpty();
	}
	
}
