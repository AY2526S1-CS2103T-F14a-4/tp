package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}
 */
public class Role {

    public static final String MESSAGE_CONSTRAINTS = "Roles are only either \"tutor\" or \"student\".";
    public static final String VALIDATION_REGEX = "^(student|tutor)$";

    public final String role;

    /**
     * Constructs a {@code Role}.
     *
     * @param role A valid role.
     */
    public Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        this.role = role;
    }

    /**
     * Returns true if a given string is a valid role.
     */
    public static boolean isValidRole(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return role.equals(otherRole.role);
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return role;
    }

}
