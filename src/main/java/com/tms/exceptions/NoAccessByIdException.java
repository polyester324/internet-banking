package com.tms.exceptions;

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
