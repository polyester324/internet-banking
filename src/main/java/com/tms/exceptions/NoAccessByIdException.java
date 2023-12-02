package com.tms.exceptions;

/**
 * NoAccessByIdException is an Exception class for checking clients id for authorisation
 */
public class NoAccessByIdException extends RuntimeException{
    private final Long ID;
    private final String LOGIN;
    public NoAccessByIdException(Long id, String login) {
        this.ID = id;
        this.LOGIN = login;
    }

    @Override
    public String toString() {
        return "NoAccessException{" +
                "id=" + ID +
                ", login='" + LOGIN + '\'' +
                '}';
    }
}
