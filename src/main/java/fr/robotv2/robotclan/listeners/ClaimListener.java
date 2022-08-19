package fr.robotv2.robotclan.listeners;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.flag.ClaimFlag;
import fr.robotv2.robotclan.objects.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

public abstract class ClaimListener implements Listener {

    private final RobotClan instance;
    public ClaimListener(RobotClan plugin) {
        this.instance = plugin;
    }

    public RobotClan getPlugin() {
        return instance;
    }

    public boolean needCancel(@Nullable Location location, Player player, ClaimFlag flag) {

        if(location == null) {
            return false;
        }

        final Claim claim = getPlugin().getClaimManager().getClaim(location.getChunk());
        return (claim != null) && !claim.getClan().checkFlag(player, flag);
    }
}
