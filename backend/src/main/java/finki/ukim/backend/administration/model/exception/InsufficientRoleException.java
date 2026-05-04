package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ForbiddenException;

public class InsufficientRoleException  extends ForbiddenException {
    public InsufficientRoleException (String username) {
        super(String.format("User with username '%s' must be an admin or manager.", username));
    }
}
