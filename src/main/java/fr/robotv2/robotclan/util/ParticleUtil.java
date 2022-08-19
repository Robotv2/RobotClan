package fr.robotv2.robotclan.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParticleUtil {

    private static List<Location> getPath(Location start, Location end, double step) {

        final List<Location> locs = new ArrayList<>();
        locs.add(start);

        Vector v = end.clone().subtract(start).toVector();
        v = v.normalize().multiply(step);
        Location current = start.clone();

        while (current.distance(end) > step) {
            locs.add(current.clone());
            current = current.add(v);
        }

        locs.add(end);
        return locs;
    }

    public static void showParticle(@NotNull Player player, @NotNull Location firstLocation, @NotNull Location secondLocation, @NotNull Particle particle) {

        if(!Objects.equals(firstLocation.getWorld().getName(), secondLocation.getWorld().getName())) {
            return;
        }

        final List<Location> particles = new ArrayList<>();
        final double height = player.getEyeLocation().getY();

        final Location firstBound = firstLocation.clone();
        firstBound.setY(height);

        final Location secondBound = secondLocation.clone();
        secondBound.setY(height);

        final Location firstCorner = new Location(player.getWorld(), firstBound.getBlockX(), height, secondBound.getBlockZ());
        final Location secondCorner = new Location(player.getWorld(), secondBound.getBlockX(), height, firstBound.getBlockZ());

        final double step = 0.5;
        particles.addAll(ParticleUtil.getPath(firstBound, firstCorner, step));
        particles.addAll(ParticleUtil.getPath(secondBound, secondCorner,step));
        particles.addAll(ParticleUtil.getPath(firstBound, secondCorner, step));
        particles.addAll(ParticleUtil.getPath(secondBound, firstCorner, step));

        for(Location location : particles) {
            player.spawnParticle(particle, location, 1);
        }
    }

    public static void showParticle(@NotNull Player player, @NotNull Chunk chunk, @NotNull Particle particle) {
        showParticle(player, chunk.getBlock(0, 0 ,0).getLocation(), chunk.getBlock(15, 255, 15).getLocation(), particle);
    }
}
