package de.example.fortresswitherboost;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public final class FortressWitherBoost extends JavaPlugin implements Listener {
    private int multiplier;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        multiplier = Math.max(1, Math.min(10, getConfig().getInt("multiplier", 5)));
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled: total multiplier = " + multiplier + "x in Nether Fortresses.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWitherSkeletonSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.WITHER_SKELETON) return;
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;

        Location base = event.getLocation();
        World world = base.getWorld();
        if (world == null || world.getEnvironment() != World.Environment.NETHER) return;
        if (!world.hasStructureAt(base, Structure.FORTRESS)) return;

        // The original natural spawn is already one. Add multiplier - 1 extras.
        for (int i = 1; i < multiplier; i++) {
            Location candidate = findNearbyFortressLocation(base, world);
            if (candidate != null) {
                world.spawn(candidate, WitherSkeleton.class, CreatureSpawnEvent.SpawnReason.CUSTOM);
            }
        }
    }

    private Location findNearbyFortressLocation(Location base, World world) {
        for (int attempt = 0; attempt < 12; attempt++) {
            double dx = ThreadLocalRandom.current().nextDouble(-2.5, 2.5);
            double dz = ThreadLocalRandom.current().nextDouble(-2.5, 2.5);
            Location candidate = base.clone().add(dx, 0, dz);
            if (!world.hasStructureAt(candidate, Structure.FORTRESS)) continue;
            if (!candidate.getBlock().isPassable()) continue;
            if (!candidate.clone().add(0, 1, 0).getBlock().isPassable()) continue;
            if (candidate.clone().subtract(0, 1, 0).getBlock().isPassable()) continue;
            return candidate;
        }
        return null;
    }
}
