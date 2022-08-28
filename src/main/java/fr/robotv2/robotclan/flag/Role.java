package fr.robotv2.robotclan.flag;

public enum Role {

    OWNER,
    OFFICIER,
    MEMBER,
    VISITOR;

    public Role getNext() {
        return values[ordinal() - 1 < 0 ? values.length - 1 : ordinal() - 1];
    }

    public Role getBefore() {
        return values[ordinal() + 1 >= values.length ? 0 : ordinal() + 1];
    }

    private static final Role[] values = Role.values();
}
