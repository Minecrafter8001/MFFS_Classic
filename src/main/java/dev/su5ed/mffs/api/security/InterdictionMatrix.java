package dev.su5ed.mffs.api.security;

import dev.su5ed.mffs.api.Activatable;
import dev.su5ed.mffs.api.module.ModuleAcceptor;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public interface InterdictionMatrix extends Activatable, BiometricIdentifierLink, ModuleAcceptor {

    /**
     * The range in which the Interdiction Matrix starts warning the player.
     */
    int getWarningRange();

    /**
     * The range in which the Interdiction Matrix has an effect on.
     */
    int getActionRange();

    /**
     * Merges an item into the Interdiction Matrix's safe keeping inventory.
     *
     * @param stack the item to merge
     * @return True if kept, false if dropped.
     */
    boolean mergeIntoInventory(ItemStack stack);

    Collection<ItemStack> getFilteredItems();

    /**
     * @return True if the filtering is on ban mode. False if it is on allow-only mode.
     */
    boolean getFilterMode();
}
