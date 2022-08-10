package fr.robotv2.robotclan.objects;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.serializable.SerializableInventory;
import fr.robotv2.robotclan.serializable.SerializableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "robotclan_clans")
public final class Clan {

    @DatabaseField(columnName = "id", id = true, unique = true)
    private UUID id;

    @DatabaseField(columnName = "owner", unique = true)
    private UUID owner;

    @DatabaseField(columnName = "name", unique = true)
    private String name;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "bank")
    private double bank;

    @DatabaseField(columnName = "officiers", dataType = DataType.SERIALIZABLE)
    private final ArrayList<UUID> officiers = new ArrayList<>();

    @DatabaseField(columnName = "members", dataType = DataType.SERIALIZABLE)
    private final ArrayList<UUID> members = new ArrayList<>();

    @DatabaseField(columnName = "base-location", dataType = DataType.SERIALIZABLE)
    private final SerializableLocation baseLocation = new SerializableLocation();

    @DatabaseField(columnName = "chest", dataType = DataType.SERIALIZABLE)
    private final SerializableInventory inventory = new SerializableInventory();

    public Clan() {}

    public Clan(UUID uuid, UUID owner, String name) {
        this.id = uuid;
        this.owner = owner;
        this.name = name;
    }

    // <<- IDENTIFICATION ->>

    public UUID getUniqueId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description == null ? "" : this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // <<- OWNER ->>

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwner(Player player) {
        return isOwner(player.getUniqueId());
    }

    public boolean isOwner(UUID uuid) {
        return this.owner.equals(uuid);
    }

    // <<- OFFICIERS ->>

    public List<UUID> getOfficiers() {
        return this.officiers;
    }

    public boolean isOfficier(UUID playerUUID) {
        return this.officiers.contains(playerUUID);
    }

    public void addOfficier(UUID playerUUID) {
        if(!isOfficier(playerUUID)) {
            getOfficiers().add(playerUUID);
        }
    }

    public void removeOfficier(UUID playerUUID) {
        if(isOfficier(playerUUID)) {
            getOfficiers().remove(playerUUID);
        }
    }

    // <<- MEMBERS ->>

    public List<UUID> getMembers() {
        return members;
    }

    public boolean isMember(UUID playerUUID) {
        return members.contains(playerUUID);
    }

    public void addMember(UUID playerUUID) {
        if(!isMember(playerUUID)) {
            getMembers().add(playerUUID);
        }
    }

    public void removeMember(UUID playerUUID) {
        if(isMember(playerUUID)) {
            getMembers().remove(playerUUID);
        }
    }

    // <<- BANK

    public Double getBankValue() {
        return bank;
    }

    public void addToBank(Number value) {
        bank += value.doubleValue();
    }

    public void removeFromBank(Number value) {
        bank -= value.doubleValue();
    }

    public void setBank(Number value) {
        bank = value.doubleValue();
    }

    // <<- CLAIMS -->

    public List<Claim> getClaims() {
        return RobotClan.get().getClaimManager().getClaims().stream()
                .filter(claim -> Objects.equals(claim.getClan().getUniqueId(), getUniqueId()))
                .collect(Collectors.toList());
    }

    //<<- BASE LOCATION ->>

    @Nullable
    public Location getBaseLocation() {
        return baseLocation.toLocation();
    }

    public void setBaseLocation(Location baseLocation) {
        this.baseLocation.setLocation(baseLocation);
    }

    // <<- OPEN INVENTORY ->>

    public void openInventory(Player player) {
        player.openInventory(inventory.getInventory());
    }

    public Inventory getInventory() {
        return this.inventory.getInventory();
    }

    // <<- UTILITY ->>

    public void sendMessage(String message) {
        final Player owner = Bukkit.getPlayer(getOwner());

        if(owner != null) {
            owner.sendMessage(message);
        }

        getMembers().stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(message));
    }

    public boolean hasAccess(Player player) {
        return hasAccess(player.getUniqueId());
    }

    public boolean hasAccess(UUID playerUUID) {
        return isOwner(playerUUID) || isOfficier(playerUUID) || isMember(playerUUID);
    }

    @Override
    public boolean equals(Object object) {

        if(object == this) {
            return true;
        }

        if(!(object instanceof Clan clan)) {
            return false;
        }

        return Objects.equals(clan.getUniqueId(), id);
    }
}
