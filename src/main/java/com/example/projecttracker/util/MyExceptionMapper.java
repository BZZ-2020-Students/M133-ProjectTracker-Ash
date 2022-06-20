package com.example.projecttracker.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for the {@link ConstraintViolationException}.
 *
 * @author Alyssa Heimlicher
 * @version 1.2
 * @since 2022-06-20
 */
@Provider
public class MyExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(exception))
                .type("text/plain")
                .build();
    }

    /**
     * Prepares the message to be returned.
     * @param exception the exception to prepare the message for
     * @return the message to be returned
     */
    private String prepareMessage(ConstraintViolationException exception) {
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            msg.append(cv.getPropertyPath()).append(" ").append(cv.getMessage()).append("\n");
        }
        return msg.toString();
    }
}