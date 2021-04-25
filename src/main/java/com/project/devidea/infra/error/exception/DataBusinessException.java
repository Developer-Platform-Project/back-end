package com.project.devidea.infra.error.exception;

import org.hibernate.exception.DataException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

public class DataBusinessException extends DataException {

    public DataBusinessException(String message, SQLException root) {
        super(message, root);
    }
}
