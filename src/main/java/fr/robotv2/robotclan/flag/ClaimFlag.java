package fr.robotv2.robotclan.flag;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum ClaimFlag {

    BLOCK_BREAK(Role.MEMBER, Material.IRON_PICKAXE),
    BLOCK_PLACE(Role.MEMBER, Material.OAK_WOOD),
    DROP(Role.MEMBER, Material.GOLD_INGOT),
    PICKUP(Role.MEMBER, Material.REDSTONE),
    PVP(Role.MEMBER, Material.DIAMOND_SWORD),
    PVE(Role.MEMBER, Material.GOLDEN_SWORD),
    INTERACT(Role.MEMBER, Material.OAK_DOOR);

    public static final ClaimFlag[] VALUES = ClaimFlag.values();

    final Role defaultRole;
    final Material material;
    ClaimFlag(Role defaultRole, Material mat) {
        this.defaultRole = defaultRole;
        this.material = mat;
    }

    @NotNull
    public Role getDefault() {
        return this.defaultRole;
    }

    public Material getMaterial() {
        return this.material;
    }
}
