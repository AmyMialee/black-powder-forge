package amymialee.blackpowder.bullet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class BulletItem extends ArrowItem {
    public BulletItem() {
        super(new Item.Properties().group(ItemGroup.COMBAT));
    }

    public AbstractArrowEntity createBullet(World worldIn, ItemStack stack, LivingEntity shooter) {
        return new ArrowEntity(worldIn, shooter);
    }
}
