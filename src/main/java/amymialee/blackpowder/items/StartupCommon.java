package amymialee.blackpowder.items;

import amymialee.blackpowder.GunSoundEvents;
import amymialee.blackpowder.bullet.BulletItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class StartupCommon {
    public static GunItem blunderbussItem;
    public static GunItem blunderbehemothItem;
    public static GunItem flintlockItem;
    public static GunItem musketItem;
    public static GunItem rifleItem;

    public static BulletItem musketBallItem;
    public static BulletItem blunderBallItem;
    //public static EntityType<BulletEntity> bulletEntityType;

    @SubscribeEvent
    public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
        SoundEvent[] blunderbussSounds = {
                GunSoundEvents.ITEM_BLUNDERBUSS_PISTOL_LOADING_START,
                GunSoundEvents.ITEM_BLUNDERBUSS_PISTOL_LOADING_MIDDLE,
                GunSoundEvents.ITEM_BLUNDERBUSS_PISTOL_LOADING_END,
                GunSoundEvents.ITEM_BLUNDERBUSS_PISTOL_SHOOT,
                GunSoundEvents.ENTITY_BULLET_IMPACT
        };
        SoundEvent[] flintlockSounds = {
                GunSoundEvents.ITEM_FLINTLOCK_PISTOL_LOADING_START,
                GunSoundEvents.ITEM_FLINTLOCK_PISTOL_LOADING_MIDDLE,
                GunSoundEvents.ITEM_FLINTLOCK_PISTOL_LOADING_END,
                GunSoundEvents.ITEM_FLINTLOCK_PISTOL_SHOOT,
                GunSoundEvents.ENTITY_BULLET_IMPACT
        };
        SoundEvent[] musketSounds = {
                GunSoundEvents.ITEM_MUSKET_PISTOL_LOADING_START,
                GunSoundEvents.ITEM_MUSKET_PISTOL_LOADING_MIDDLE,
                GunSoundEvents.ITEM_MUSKET_PISTOL_LOADING_END,
                GunSoundEvents.ITEM_MUSKET_PISTOL_SHOOT,
                GunSoundEvents.ENTITY_BULLET_IMPACT
        };
        SoundEvent[] rifleSounds = {
                GunSoundEvents.ITEM_RIFLE_PISTOL_LOADING_START,
                GunSoundEvents.ITEM_RIFLE_PISTOL_LOADING_MIDDLE,
                GunSoundEvents.ITEM_RIFLE_PISTOL_LOADING_END,
                GunSoundEvents.ITEM_RIFLE_PISTOL_SHOOT,
                GunSoundEvents.ENTITY_BULLET_IMPACT
        };

        blunderbussItem = new BlunderbussItem(8, 20, 160, 25, blunderbussSounds);
        blunderbehemothItem = new BlunderbussItem(800, 40, 480, 35, blunderbussSounds);
        flintlockItem = new GunItem(1, 10, 50, 15, flintlockSounds);
        musketItem = new GunItem(1, 7, 100, 15, musketSounds);
        rifleItem = new GunItem(1, 3, 160, 25, rifleSounds);
        musketBallItem = new BulletItem();
        blunderBallItem = new BulletItem();

        blunderbussItem.setRegistryName("blunderbuss");
        blunderbehemothItem.setRegistryName("blunderbehemoth");
        flintlockItem.setRegistryName("flintlock_pistol");
        musketItem.setRegistryName("musket");
        rifleItem.setRegistryName("rifle");
        musketBallItem.setRegistryName("musket_ball");
        blunderBallItem.setRegistryName("blunder_ball");

        itemRegisterEvent.getRegistry().register(blunderbussItem);
        itemRegisterEvent.getRegistry().register(blunderbehemothItem);
        itemRegisterEvent.getRegistry().register(flintlockItem);
        itemRegisterEvent.getRegistry().register(musketItem);
        itemRegisterEvent.getRegistry().register(rifleItem);
        itemRegisterEvent.getRegistry().register(musketBallItem);
        itemRegisterEvent.getRegistry().register(blunderBallItem);
    }

    /*@SubscribeEvent
    public static void onEntityRegistration(RegistryEvent.Register<EntityType<?>> entityTypeRegisterEvent) {
        bulletEntityType = EntityType.Builder.<BulletEntity>create(BulletEntity::new, EntityClassification.MISC)
                .size(0.25F, 0.25F)
                .build("blackpowder:bullet");
        bulletEntityType.setRegistryName("blackpowder:bullet");
        entityTypeRegisterEvent.getRegistry().register(bulletEntityType);
    }

     */

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {}
}
