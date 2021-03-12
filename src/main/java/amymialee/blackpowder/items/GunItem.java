package amymialee.blackpowder.items;

import amymialee.blackpowder.GunSoundEvents;
import amymialee.blackpowder.bullet.BulletItem;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.Tag;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class GunItem extends CrossbowItem implements IVanishable {
    public int bulletCount;
    public float inaccuracy;
    public int chargeTime;
    public int quickChargeChange;
    public SoundEvent START;
    public SoundEvent MIDDLE;
    public SoundEvent END;
    public SoundEvent SHOOT;
    public SoundEvent HIT;

    public GunItem(int bulletCount, float inaccuracy, int chargeTime, int quickChargeChange, SoundEvent[] soundEvents) {
        super(new Item.Properties().maxStackSize(1).group(ItemGroup.COMBAT));
        this.bulletCount = bulletCount;
        this.inaccuracy = inaccuracy;
        this.chargeTime = chargeTime;
        this.quickChargeChange = quickChargeChange;
        this.START = soundEvents[0];
        this.MIDDLE = soundEvents[1];
        this.END = soundEvents[2];
        this.SHOOT = soundEvents[3];
        this.HIT = soundEvents[4];
    }

    private boolean isLoadingStart = false;
    private boolean isLoadingMiddle = false;
    public static final Predicate<ItemStack> MUSKETBALLS = (stack) -> {
        return stack.getItem() == StartupCommon.musketBallItem;
    };

    public static SoundEvent getShootSound(GunItem item) {
        return item.SHOOT;
    }

    public static SoundEvent getHitSound(GunItem item) {
        return item.HIT;
    }

    @Override
    public Predicate<ItemStack> getAmmoPredicate() {
        return getInventoryAmmoPredicate();
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return MUSKETBALLS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (isCharged(itemstack)) {
            fireBullets(worldIn, playerIn, handIn, itemstack,3.15F * 4, inaccuracy, bulletCount);
            setCharged(itemstack, false);
            return ActionResult.resultConsume(itemstack);
        } else if (!playerIn.findAmmo(itemstack).isEmpty()) {
            if (!isCharged(itemstack)) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
                playerIn.setActiveHand(handIn);
            }
            return ActionResult.resultConsume(itemstack);
        } else {
            return ActionResult.resultFail(itemstack);
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getCharge(i, stack, chargeTime, quickChargeChange);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(entityLiving, stack)) {
            setCharged(stack, true);
            SoundCategory soundcategory = entityLiving instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            worldIn.playSound((PlayerEntity)null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), END, soundcategory, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isRemote) {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundevent = this.getSoundEvent(i);
            SoundEvent soundevent1 = i == 0 ? MIDDLE : null;
            float f = (float)(stack.getUseDuration() - count) / (float)getChargeTime(stack, chargeTime, quickChargeChange);
            if (f < 0.2F) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
            }
            if (f >= 0.2F && !this.isLoadingStart) {
                this.isLoadingStart = true;
                worldIn.playSound(null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.isLoadingMiddle) {
                this.isLoadingMiddle = true;
                worldIn.playSound(null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(), soundevent1, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return getChargeTime(stack, chargeTime, quickChargeChange) + 3;
    }

    private SoundEvent getSoundEvent(int enchantmentLevel) {
        return START;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<ItemStack> list = getChargedProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemstack = list.get(0);
            tooltip.add((new TranslationTextComponent("item.minecraft.crossbow.projectile")).appendString(" ").append(itemstack.getTextComponent()));
            if (flagIn.isAdvanced() && itemstack.getItem() == Items.FIREWORK_ROCKET) {
                List<ITextComponent> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.addInformation(itemstack, worldIn, list1, flagIn);
                if (!list1.isEmpty()) {
                    for(int i = 0; i < list1.size(); ++i) {
                        list1.set(i, (new StringTextComponent("  ")).append(list1.get(i)).mergeStyle(TextFormatting.GRAY));
                    }

                    tooltip.addAll(list1);
                }
            }

        }
    }

    @Override
    public int func_230305_d_() {
        return 8;
    }

    private static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, stack);
        int j = i == 0 ? 1 : 3;
        boolean flag = entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).abilities.isCreativeMode;
        ItemStack itemstack = entityIn.findAmmo(stack);
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!func_220023_a(entityIn, stack, itemstack, k > 0, flag)) {
                return false;
            }
        }
        return true;
    }

    private static boolean func_220023_a(LivingEntity p_220023_0_, ItemStack stack, ItemStack p_220023_2_, boolean p_220023_3_, boolean p_220023_4_) {
        if (p_220023_2_.isEmpty()) {
            return false;
        } else {
            boolean flag = p_220023_4_ && p_220023_2_.getItem() instanceof BulletItem;
            ItemStack itemstack;
            if (!flag && !p_220023_4_ && !p_220023_3_) {
                itemstack = p_220023_2_.split(1);
                if (p_220023_2_.isEmpty() && p_220023_0_ instanceof PlayerEntity) {
                    ((PlayerEntity)p_220023_0_).inventory.deleteStack(p_220023_2_);
                }
            } else {
                itemstack = p_220023_2_.copy();
            }
            addChargedProjectile(stack, itemstack);
            return true;
        }
    }

    public static boolean isCharged(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }

    public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    private static void addChargedProjectile(ItemStack crossbow, ItemStack projectile) {
        CompoundNBT compoundnbt = crossbow.getOrCreateTag();
        ListNBT listnbt;
        if (compoundnbt.contains("ChargedProjectiles", 9)) {
            listnbt = compoundnbt.getList("ChargedProjectiles", 10);
        } else {
            listnbt = new ListNBT();
        }

        CompoundNBT compoundnbt1 = new CompoundNBT();
        projectile.write(compoundnbt1);
        listnbt.add(compoundnbt1);
        compoundnbt.put("ChargedProjectiles", listnbt);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack stack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null && compoundnbt.contains("ChargedProjectiles", 9)) {
            ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 10);
            if (listnbt != null) {
                for(int i = 0; i < listnbt.size(); ++i) {
                    CompoundNBT compoundnbt1 = listnbt.getCompound(i);
                    list.add(ItemStack.read(compoundnbt1));
                }
            }
        }

        return list;
    }

    private static void clearProjectiles(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null) {
            ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 9);
            listnbt.clear();
            compoundnbt.put("ChargedProjectiles", listnbt);
        }
    }

    public static boolean hasChargedProjectile(ItemStack stack, Item ammoItem) {
        return getChargedProjectiles(stack).stream().anyMatch((p_220010_1_) -> {
            return p_220010_1_.getItem() == ammoItem;
        });
    }

    private static void fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack gun, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle, int bulletCount) {
        if (!worldIn.isRemote) {
            for (int i = 0; i < bulletCount; i++) {
                ProjectileEntity projectileentity = createBullet(worldIn, shooter, gun, projectile);

                if (shooter instanceof ICrossbowUser) {
                    ICrossbowUser icrossbowuser = (ICrossbowUser) shooter;
                    icrossbowuser.func_230284_a_(icrossbowuser.getAttackTarget(), gun, projectileentity, projectileAngle);
                } else {
                    Vector3d vector3d1 = shooter.getUpVector(1.0F);
                    Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
                    Vector3d vector3d = shooter.getLook(1.0F);
                    Vector3f vector3f = new Vector3f(vector3d);
                    vector3f.transform(quaternion);
                    projectileentity.shoot((double) vector3f.getX(), (double) vector3f.getY(), (double) vector3f.getZ(), velocity, inaccuracy);
                }
                worldIn.addEntity(projectileentity);
            }
            worldIn.playSound((PlayerEntity) null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), GunItem.getShootSound((GunItem) gun.getItem()), SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    private static AbstractArrowEntity createBullet(World worldIn, LivingEntity shooter, ItemStack gun, ItemStack ammo) {
        BulletItem bulletitem = (BulletItem)(ammo.getItem() instanceof BulletItem ? ammo.getItem() : StartupCommon.musketBallItem);
        AbstractArrowEntity abstractbulletentity = bulletitem.createBullet(worldIn, ammo, shooter);
        abstractbulletentity.setHitSound(GunItem.getHitSound((GunItem) gun.getItem()));
        abstractbulletentity.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, gun);
        if (i > 0) {
            abstractbulletentity.setPierceLevel((byte)i);
        }
        return abstractbulletentity;
    }

    public static void fireBullets(World worldIn, LivingEntity shooter, Hand handIn, ItemStack stack, float velocityIn, float inaccuracyIn, int bulletCount) {
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getRandomSoundPitches(shooter.getRNG());
        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.isCreativeMode;
            if (!itemstack.isEmpty()) {
                if (i == 0) {
                    fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F, bulletCount);
                } else if (i == 1) {
                    fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F, bulletCount);
                } else if (i == 2) {
                    fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F, bulletCount);
                }
            }
        }
        fireBulletsAfter(worldIn, shooter, stack);
    }

    private static float[] getRandomSoundPitches(Random rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomSoundPitch(flag), getRandomSoundPitch(!flag)};
    }

    private static float getRandomSoundPitch(boolean flagIn) {
        float f = flagIn ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void fireBulletsAfter(World worldIn, LivingEntity shooter, ItemStack stack) {
        if (shooter instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)shooter;
            serverplayerentity.addStat(Stats.ITEM_USED.get(stack.getItem()));
        }
        clearProjectiles(stack);
    }

    public static int getChargeTime(ItemStack stack, int chargeTime, int quickChargeChange) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? chargeTime : chargeTime - quickChargeChange * i;
    }

    private static float getCharge(int useTime, ItemStack stack, int chargeTime, int quickChargeChange) {
        float f = (float)useTime / (float)getChargeTime(stack, chargeTime, quickChargeChange);
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}
