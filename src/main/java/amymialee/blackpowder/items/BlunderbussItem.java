package amymialee.blackpowder.items;

import net.minecraft.enchantment.IVanishable;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvent;

import java.util.function.Predicate;

public class BlunderbussItem extends GunItem implements IVanishable {
    public BlunderbussItem(int bulletCount, float inaccuracy, int chargeTime, int quickChargeChange, SoundEvent[] events) {
        super(bulletCount, inaccuracy, chargeTime, quickChargeChange, events);
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return (stack) -> stack.getItem() == StartupCommon.blunderBallItem;
    }
}
