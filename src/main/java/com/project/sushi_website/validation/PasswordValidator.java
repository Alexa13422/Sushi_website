package com.project.sushi_website.validation;

import com.project.sushi_website.model.DTO.CustomerDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!hasLowerCaseLetters(password)) {
            returnCustomMessage(context, "Password must contain >1 lowercase letter");
            return false;
        }
//        if (!hasTwoUpperCaseLetters(password)) {
//            returnCustomMessage(context, "Password must contain >2 uppercase letters");
//            return false;
//        }
//        if (!hasThreeDigits(password)) {
//            returnCustomMessage(context, "Password must contain >3 digits");
//            return false;
//        }
//        if (!hasFourSpecialCharacters(password)) {
//            returnCustomMessage(context, "Password must contain >4 special characters");
//            return false;
//        }
        return true;
    }

    private boolean hasLowerCaseLetters(String password){
        return password.chars().filter(Character::isLowerCase).count()>=1;
    }

    private boolean hasTwoUpperCaseLetters(String password){
        return password.chars().filter(Character::isLowerCase).count()>=2;
    }

    private boolean hasThreeDigits(String password){
        return password.chars().filter(Character::isDigit).count()>=3;
    }

    private boolean hasFourSpecialCharacters(String password){
        return password.chars().filter(ch -> !Character.isLetterOrDigit(ch)).count()>=4;
    }


    private void returnCustomMessage(ConstraintValidatorContext context, String message) {

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}