package fr.robotv2.robotclan.manager;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.Chunk;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ClaimManager {

    private final Map<Chunk, Claim> claims = new ConcurrentHashMap<>();

    public void registerClaim(Claim claim) {
        Objects.requireNonNull(claim);
        if(!claim.isValid()) return;
        this.claims.put(claim.getChunk(), claim);
    }

    public void registerClaim(Clan clan, Chunk chunk) {
        final Claim claim = new Claim(clan, chunk);
        this.claims.put(chunk, claim);
    }

    public void unregisterClaim(Claim claim, boolean deleteFromDb) {
        Objects.requireNonNull(claim);
        this.claims.remove(claim.getChunk());

        if(deleteFromDb) {
            RobotClan.get().getClaimData().remove(claim);
        }
    }

    public Collection<Claim> getClaims() {
        return Collections.unmodifiableCollection(this.claims.values());
    }

    public Claim getClaim(Chunk chunk) {
       return this.claims.get(chunk);
    }

    public boolean isClaimed(Chunk chunk) {
        return getClaim(chunk) != null;
    }
}
