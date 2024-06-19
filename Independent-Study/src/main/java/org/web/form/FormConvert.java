package org.web.form;

public interface FormConvert <S, T>{
    T convert(S s);
}

// UserForm userForm => Users
