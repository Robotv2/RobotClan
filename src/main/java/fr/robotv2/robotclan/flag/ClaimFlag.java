package fr.robotv2.robotclan.flag;

import org.jetbrains.annotations.NotNull;

public enum ClaimFlag {

    BLOCK_BREAK(Role.MEMBER),
    BLOCK_PLACE(Role.MEMBER),
    DROP(Role.MEMBER),
    PICKUP(Role.MEMBER),
    PVP(Role.MEMBER),
    PVE(Role.MEMBER),
    INTERACT(Role.MEMBER);

    final Role defaultRole;
    ClaimFlag(Role defaultRole) {
        this.defaultRole = defaultRole;
    }

    @NotNull
    public Role getDefault() {
        return this.defaultRole;
    }
}
