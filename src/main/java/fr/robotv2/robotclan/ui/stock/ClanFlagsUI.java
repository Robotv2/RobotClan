package fr.robotv2.robotclan.ui.stock;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.ClaimFlag;
import fr.robotv2.robotclan.flag.Role;
import fr.robotv2.robotclan.objects.Clan;
import fr.robotv2.robotclan.ui.FillAPI;
import fr.robotv2.robotclan.ui.Gui;
import fr.robotv2.robotclan.util.ItemAPI;
import fr.robotv2.robotclan.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ClanFlagsUI extends Gui {

    @Override
    public String getName(Player player, Object... objects) {
        final Clan clan = (Clan) objects[0];
        return "Flags";
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public void contents(Player player, Inventory inv, Object... objects) {
        FillAPI.setupEmptySlots(inv, FillAPI.FillType.ALL, null);

        final Clan clan = (Clan) objects[0];
        int count = 0;

        for(ClaimFlag flag : ClaimFlag.VALUES) {
            inv.setItem(count, getFlagItem(clan, flag));
            ++count;
        }
    }

    @Override
    public void onClick(Player player, Inventory inv, ItemStack current, int slot, @NotNull ClickType click) {

        if(!ItemAPI.hasKey(current, "CLAIM_FLAG", PersistentDataType.STRING)) {
            return;
        }

        final String flagStr = ItemAPI.getKeyValue(current, "CLAIM_FLAG", PersistentDataType.STRING);
        final ClaimFlag flag = ClaimFlag.valueOf(flagStr);

        final String clanUuid = ItemAPI.getKeyValue(current, "CLAN_UUID", PersistentDataType.STRING);
        final Clan clan = RobotClan.get().getClanManager().getClan(UUID.fromString(clanUuid));

        if(clan == null) {
            return;
        }

        final Role role = clan.getRequiredRole(flag).getNext();
        Bukkit.dispatchCommand(player, "clan flag " + flag + " " + role);
        RobotClan.get().getGuiManager().open(player, ClanFlagsUI.class, clan);
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {}

    private ItemStack getFlagItem(Clan clan, ClaimFlag flag) {
        return new ItemAPI.ItemBuilder()
                .setType(flag.getMaterial())
                .setName("&7Flag: &f" + StringUtil.beautify(flag.name()))
                .setLore("&7Current value: &f" + StringUtil.beautify(clan.getRequiredRole(flag).name()))
                .setKey("CLAIM_FLAG", flag.name())
                .setKey("CLAN_UUID", clan.getUniqueId().toString())
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }
}
