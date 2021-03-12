package amymialee.blackpowder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class GunSoundEvents {
    private GunSoundEvents() {}

    public static final SoundEvent ITEM_FLINTLOCK_PISTOL_LOADING_END = createEvent("flintlock_loading_end");
    public static final SoundEvent ITEM_FLINTLOCK_PISTOL_LOADING_MIDDLE = createEvent("flintlock_loading_middle");
    public static final SoundEvent ITEM_FLINTLOCK_PISTOL_LOADING_START = createEvent("flintlock_loading_start");
    public static final SoundEvent ITEM_FLINTLOCK_PISTOL_SHOOT = createEvent("flintlock_shoot");

    public static final SoundEvent ITEM_BLUNDERBUSS_PISTOL_LOADING_END = createEvent("blunderbuss_loading_end");
    public static final SoundEvent ITEM_BLUNDERBUSS_PISTOL_LOADING_MIDDLE = createEvent("blunderbuss_loading_middle");
    public static final SoundEvent ITEM_BLUNDERBUSS_PISTOL_LOADING_START = createEvent("blunderbuss_loading_start");
    public static final SoundEvent ITEM_BLUNDERBUSS_PISTOL_SHOOT = createEvent("blunderbuss_shoot");

    public static final SoundEvent ITEM_MUSKET_PISTOL_LOADING_END = createEvent("musket_loading_end");
    public static final SoundEvent ITEM_MUSKET_PISTOL_LOADING_MIDDLE = createEvent("musket_loading_middle");
    public static final SoundEvent ITEM_MUSKET_PISTOL_LOADING_START = createEvent("musket_loading_start");
    public static final SoundEvent ITEM_MUSKET_PISTOL_SHOOT = createEvent("musket_shoot");

    public static final SoundEvent ITEM_RIFLE_PISTOL_LOADING_END = createEvent("rifle_loading_end");
    public static final SoundEvent ITEM_RIFLE_PISTOL_LOADING_MIDDLE = createEvent("rifle_loading_middle");
    public static final SoundEvent ITEM_RIFLE_PISTOL_LOADING_START = createEvent("rifle_loading_start");
    public static final SoundEvent ITEM_RIFLE_PISTOL_SHOOT = createEvent("rifle_shoot");

    public static final SoundEvent ENTITY_BULLET_IMPACT = createEvent("bullet_impact");

    private static SoundEvent createEvent(String key) {
        ResourceLocation location = new ResourceLocation(BlackPowder.MODID, key);
        return new SoundEvent(location).setRegistryName(location);
    }

    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(ITEM_FLINTLOCK_PISTOL_LOADING_END);
        registry.register(ITEM_FLINTLOCK_PISTOL_LOADING_MIDDLE);
        registry.register(ITEM_FLINTLOCK_PISTOL_LOADING_START);
        registry.register(ITEM_FLINTLOCK_PISTOL_SHOOT);

        registry.register(ITEM_BLUNDERBUSS_PISTOL_LOADING_END);
        registry.register(ITEM_BLUNDERBUSS_PISTOL_LOADING_MIDDLE);
        registry.register(ITEM_BLUNDERBUSS_PISTOL_LOADING_START);
        registry.register(ITEM_BLUNDERBUSS_PISTOL_SHOOT);

        registry.register(ITEM_MUSKET_PISTOL_LOADING_END);
        registry.register(ITEM_MUSKET_PISTOL_LOADING_MIDDLE);
        registry.register(ITEM_MUSKET_PISTOL_LOADING_START);
        registry.register(ITEM_MUSKET_PISTOL_SHOOT);

        registry.register(ITEM_RIFLE_PISTOL_LOADING_END);
        registry.register(ITEM_RIFLE_PISTOL_LOADING_MIDDLE);
        registry.register(ITEM_RIFLE_PISTOL_LOADING_START);
        registry.register(ITEM_RIFLE_PISTOL_SHOOT);

        registry.register(ENTITY_BULLET_IMPACT);
    }
}
