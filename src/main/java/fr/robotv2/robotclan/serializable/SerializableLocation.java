package fr.robotv2.robotclan.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class SerializableLocation implements java.io.Serializable {

    private double X;
    private double Y;
    private double Z;

    private float yaw;
    private float pitch;

    private String worldName;

    @Nullable
    public Location toLocation() {

        if(worldName == null || Bukkit.getWorld(worldName) == null) {
            return null;
        }

        return new Location(Bukkit.getWorld(worldName), X, Y, Z, yaw, pitch);
    }

    public void setLocation(Location location) {
        this.X = location.getX();
        this.Y = location.getY();
        this.Z = location.getZ();
        this.worldName = location.getWorld().getName();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
}
