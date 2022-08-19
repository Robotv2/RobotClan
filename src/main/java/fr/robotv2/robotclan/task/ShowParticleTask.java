package fr.robotv2.robotclan.task;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.manager.ClaimManager;
import fr.robotv2.robotclan.objects.Claim;
import fr.robotv2.robotclan.util.ParticleUtil;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShowParticleTask extends BukkitRunnable {

    private int timer = 40;
    private final Player player;

    public ShowParticleTask(Player player) {
        this.player = player;
    }


    @Override
    public void run() {

        if(timer <= 0) {
            this.cancel();
            return;
        }

        ParticleUtil.showParticle(player, player.getChunk(), getParticle(player));
        --timer;
    }

    private Particle getParticle(Player player) {

        final ClaimManager manager = RobotClan.get().getClaimManager();
        final Chunk chunk = player.getChunk();

        if(!manager.isClaimed(chunk)) {
            return Particle.VILLAGER_HAPPY;
        } else {
            final Claim claim = manager.getClaim(chunk);
            return claim.getClan().hasAccess(player) ? Particle.HEART : Particle.CLOUD;
        }
    }
}
