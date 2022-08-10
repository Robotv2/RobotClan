package fr.robotv2.robotclan.manager;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClanManager {

    private final Map<UUID, Clan> clans = new ConcurrentHashMap<>();

    public Collection<Clan> getClans() {
        return Collections.unmodifiableCollection(clans.values());
    }

    @Nullable
    public Clan getClan(UUID clanUUID) {
        return this.clans.get(clanUUID);
    }

    @Nullable
    public Clan getClan(String clanName) {
        return getClans().stream()
                .filter(clan -> clan.getName().equalsIgnoreCase(clanName))
                .findFirst().orElse(null);
    }

    @Nullable
    public Clan getClan(Player player) {
        return getClans().stream()
                .filter(clan -> clan.hasAccess(player))
                .findFirst().orElse(null);
    }

    public boolean hasClan(Player player) {
        return getClans().stream().anyMatch(clan -> clan.hasAccess(player));
    }

    public boolean exist(UUID uuid) {
        return getClan(uuid) != null;
    }

    public boolean exist(String clanName) {
        return getClan(clanName) != null;
    }

    public void registerClan(Clan clan) {
        Objects.requireNonNull(clan);
        this.clans.put(clan.getUniqueId(), clan);
    }

    public void unregisterClan(Clan clan, boolean deleteFromDb) {
        Objects.requireNonNull(clan);
        this.clans.remove(clan.getUniqueId());

        if(deleteFromDb) {
            RobotClan.get().getClanData().remove(clan);
        }
    }
}
