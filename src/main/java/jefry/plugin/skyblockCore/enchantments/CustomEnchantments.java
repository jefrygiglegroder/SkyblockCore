package jefry.plugin.skyblockCore.enchantments;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CustomEnchantments {

    private static final Map<String, Enchantment> ENCHANTMENTS = new HashMap<>();

    public static void registerCustomEnchantment(Enchantment enchantment) {
        // Check if the enchantment is already registered
        if (Enchantment.getByKey(enchantment.getKey()) != null) {
            return; // Enchantment is already registered
        }

        try {
            // Use reflection to allow registering new enchantments
            Field acceptingNewField = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNewField.setAccessible(true);
            acceptingNewField.set(null, true);

            // Use reflection to invoke the protected method registerEnchantment
            Method registerMethod = Enchantment.class.getDeclaredMethod("registerEnchantment", Enchantment.class);
            registerMethod.setAccessible(true);
            registerMethod.invoke(null, enchantment);

            ENCHANTMENTS.put(enchantment.getKey().getKey(), enchantment);

            // Log successful registration
            System.out.println("Registered custom enchantment: " + enchantment.getKey().getKey());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerAllEnchantments() {
        registerCustomEnchantment(new AutoPickupEnchantment());
        registerCustomEnchantment(new HasteEnchantment());

        // Log to confirm registration
        System.out.println("Registered AutoPickupEnchantment and HasteEnchantment.");
    }

    public static Map<String, Enchantment> getEnchantments() {
        return ENCHANTMENTS;
    }
}
