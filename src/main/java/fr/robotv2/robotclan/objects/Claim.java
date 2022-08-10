package fr.robotv2.robotclan.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fr.robotv2.robotclan.RobotClan;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Objects;
import java.util.UUID;

@DatabaseTable(tableName = "robotclan_claims")
public final class Claim {

    @DatabaseField(columnName = "worldName")
    private String worldName;

    @DatabaseField(columnName = "chunkKey")
    private Long chunkKey;

    @DatabaseField(columnName = "clanOwner")
    private UUID clanUUID;

    public Claim() {}

    public Claim(Clan clan, Chunk chunk) {
        this.clanUUID = clan.getUniqueId();
        this.worldName = chunk.getWorld().getName();
        this.chunkKey = chunk.getChunkKey();
    }

    public boolean isValid() {
        return getWorld() != null && getClan() != null;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public Chunk getChunk() {
        return getWorld().getChunkAt(chunkKey);
    }

    public Clan getClan() {
        return RobotClan.get().getClanManager().getClan(clanUUID);
    }

    @Override
    public boolean equals(Object object) {

        if(this == object) {
            return true;
        }

        if(!(object instanceof Claim claim)) {
            return false;
        }

        return Objects.equals(claim.clanUUID, clanUUID) &&
                Objects.equals(claim.chunkKey, chunkKey) &&
                Objects.equals(claim.worldName, worldName);
    }
}
