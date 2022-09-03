package fr.robotv2.robotclan.ui;

import fr.robotv2.robotclan.util.ItemAPI;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FillAPI {

    private static ItemStack empty;

    public enum FillType {
        ALL, BOTTOM, TOP, BOTTOM_AND_TOP;
    }

    public static ItemStack getDefaultEmpty() {
        return new ItemAPI.ItemBuilder().setType(Material.BLACK_STAINED_GLASS_PANE)
                .setName("&8").addFlags(ItemFlag.HIDE_ATTRIBUTES).build();
    }

    public static void setEmpty(ItemStack item) {
        empty = item;
    }

    public static ItemStack getEmpty() {
        if(empty == null)
            empty = getDefaultEmpty();
        return empty;
    }

    public static void setupEmptySlots(Inventory inv, FillType type, @Nullable ItemStack item) {
        final ItemStack fill = item != null ? item : getEmpty();
        switch(type) {
            case ALL:
                for(int i=0; i <= inv.getSize() - 1; i++) {
                    inv.setItem(i, fill);
                }
                break;

            case TOP:
                for(int i=0; i <= 8; i++) {
                    inv.setItem(i, fill);
                }
                break;

            case BOTTOM:
                for(int i = inv.getSize() - 8; i <= inv.getSize(); i++) {
                    inv.setItem(i, fill);
                }
                break;

            case BOTTOM_AND_TOP:
                setupEmptySlots(inv, FillType.TOP, fill);
                setupEmptySlots(inv, FillType.BOTTOM, fill);
                break;
        }
    }
}
