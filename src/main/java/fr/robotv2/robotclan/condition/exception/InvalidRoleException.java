package fr.robotv2.robotclan.condition.exception;

import fr.robotv2.robotclan.flag.Role;
import revxrsal.commands.exception.ThrowableFromCommand;

@ThrowableFromCommand
public class InvalidRoleException extends RuntimeException {

    private final Role required;
    public InvalidRoleException(Role role) {
        this.required = role;
    }

    public Role getRequired() {
        return required;
    }
}
