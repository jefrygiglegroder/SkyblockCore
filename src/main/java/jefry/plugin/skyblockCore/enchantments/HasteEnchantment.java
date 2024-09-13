package jefry.plugin.skyblockCore.enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import io.papermc.paper.enchantments.EnchantmentRarity;
import java.util.Collections;
import java.util.Set;

public class HasteEnchantment extends CustomEnchantment {

    public static final NamespacedKey KEY = new NamespacedKey("skyblockcore", "haste");

    public HasteEnchantment() {
        super(KEY, "Haste", "Increases mining speed.",
                EnchantmentRarity.RARE, 1, 3, false, false); // Display name, description, etc.
    }

    @Override
    public @NotNull String getName() {
        return "Haste";
    }

    @Override
    public int getMaxLevel() {
        return 3;  // Haste I to III
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;  // Apply to mining tools
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        // Allow it only on pickaxes
        return item.getType().name().endsWith("PICKAXE");
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public int getMinModifiedCost(int i) {
        return 0;
    }

    @Override
    public int getMaxModifiedCost(int i) {
        return 0;
    }

    @Override
    public int getAnvilCost() {
        return 0;
    }

    @Override
    public float getDamageIncrease(int i, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public float getDamageIncrease(int i, @NotNull EntityType entityType) {
        return 0;
    }

    @Override
    public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
        // You can implement this if specific equipment slots need to be activated.
        return Collections.emptySet(); // No specific slots required
    }

    @Override
    public @NotNull String translationKey() {
        return null;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return KEY;  // Return the correct key for this enchantment
    }

    @Override
    public @NotNull String getTranslationKey() {
        return "enchantment.skyblockcore.haste"; // Translation key for localization support
    }
}
