package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ForbiddenException;

public class StaffOutsideManagerScopeException extends ForbiddenException {

    public StaffOutsideManagerScopeException() {
        super("Staff member is outside the manager's municipality and department.");
    }
}
