package amymialee.blackpowder;

import amymialee.blackpowder.client.StartupClient;
import amymialee.blackpowder.items.StartupCommon;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BlackPowder.MODID)
public class BlackPowder {
    public static final String MODID = "blackpowder";

    public BlackPowder() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addGenericListener(SoundEvent.class, GunSoundEvents::registerSounds);
        modEventBus.register(StartupCommon.class);
        if (!Dist.CLIENT.isDedicatedServer()) {
            modEventBus.register(StartupClient.class);
        }
    }
}
