package jefry.plugin.skyblockCore.enchantments;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class CustomEnchantment extends Enchantment {

    private final NamespacedKey key;
    private final String displayName;
    private final String description;
    private final EnchantmentRarity rarity;
    private final int maxLevel;
    private final int startLevel;
    private final boolean isTreasure;
    private final boolean isCursed;

    // Constructor for initializing custom enchantments
    public CustomEnchantment(@NotNull NamespacedKey key, String displayName, String description,
                             EnchantmentRarity rarity, int startLevel, int maxLevel, boolean isTreasure, boolean isCursed) {
        super();
        this.key = key;
        this.displayName = displayName;
        this.description = description;
        this.rarity = rarity;
        this.startLevel = startLevel;
        this.maxLevel = maxLevel;
        this.isTreasure = isTreasure;
        this.isCursed = isCursed;
    }

    @Override
    public @NotNull String getName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return startLevel;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return isTreasure;
    }

    @Override
    public boolean isCursed() {
        return isCursed;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return true; // This can be customized per enchantment
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return Collections.emptySet(); // Customize if needed
    }

    @Override
    public @NotNull Component displayName(int level) {
        return Component.text(displayName + " " + level);
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return rarity;
    }
}
