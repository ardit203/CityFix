package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class CitizenCannotBeStaffException extends ConflictException {

    public CitizenCannotBeStaffException(String username) {
        super(String.format("The user with username '%s' is a citizen and cannot be a staff member.", username));
    }
}
