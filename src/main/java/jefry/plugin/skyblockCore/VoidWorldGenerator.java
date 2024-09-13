package jefry.plugin.skyblockCore;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.World;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class VoidWorldGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        return createChunkData(world); // This generates an empty chunk (all air)
    }
}

