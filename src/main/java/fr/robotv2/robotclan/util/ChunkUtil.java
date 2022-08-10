package fr.robotv2.robotclan.util;

import com.google.common.collect.Queues;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Queue;

public class ChunkUtil {

    public static Queue<Chunk> getChunksAround(Chunk chunk) {
        int[] offset = {-2, -1, 0, 1, 2};

        final World world = chunk.getWorld();
        final int baseX = chunk.getX();
        final int baseZ = chunk.getZ();

        final Queue<Chunk> chunksAroundPlayer = Queues.newConcurrentLinkedQueue();

        for(int x : offset) {
            for(int z : offset) {
                chunksAroundPlayer.add(world.getChunkAt(baseX + x, baseZ + z));
            }
        }

        return chunksAroundPlayer;
    }
}
