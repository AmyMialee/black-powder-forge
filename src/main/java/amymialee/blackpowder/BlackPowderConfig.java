package amymialee.blackpowder;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = BlackPowder.MODID)
public
class BlackPowderConfig implements ConfigData {
    public boolean useGlobalEnchantCap = true;
    public int globalEnchantCap = 10;
}