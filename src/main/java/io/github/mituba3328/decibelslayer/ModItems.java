package io.github.mituba3328.decibelslayer;

import io.github.mituba3328.decibelslayer.ExampleMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;


public class ModItems {
	public static Item register(Item item, String id) {
		// Create the identifier for the item.
		Identifier itemID = Identifier.of(ExampleMod.MOD_ID, id);

		// Register the item.
		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

		// Return the registered item!
		return registeredItem;
	}

    public static final Item SUSPICIOUS_SUBSTANCE = register(
		new Item(new Item.Settings()),
		"suspicious_substance"
    );

    public static void initialize() {
    }
}