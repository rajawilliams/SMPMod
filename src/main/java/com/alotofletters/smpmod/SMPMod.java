package com.alotofletters.smpmod;

import com.alotofletters.smpmod.init.SMPBlocks;
import com.alotofletters.smpmod.init.SMPItems;
import com.alotofletters.smpmod.init.gen.SMPFeatureRegistry;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main mod class.
 */
@Mod(SMPMod.MODID)
public class SMPMod {
	/**
	 * Has to match mod.toml.
	 */
	public final static String MODID = "smpmod";

	/**
	 * Logger for mod if anything goes wrong. Surprisingly, a lot more goes wrong than expected.
	 */
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	/**
	 * Logger for mod registries, put in the main class to simplify things.
	 */
	public static final Logger LOGGER_REGISTRY = LogManager.getLogger(MODID + " registry");

	/**
	 * Constructor. Since proxies have been effectively deprecated with the
	 * introduction of EventBus' in 1.7.10 Forge, there is no point to a proxy.
	 * Simply get the ModLoadingContext and IEventBus and you're on your
	 * merry way.
	 *
	 * @see com.alotofletters.smpmod.init.SMPEventSubscriber
	 */
	public SMPMod() {
		initialize(FMLJavaModLoadingContext.get().getModEventBus());
	}

//    static {
//        DungeonHooks.addDungeonMob(EntityType.SLIME, 50);
//    }

	/**
	 * Initialize the EventBus for the mod.
	 * @param eventBus The ModLoadingContexts' IEventBus.
	 * @see SMPMod#SMPMod()
	 */
	private void initialize(IEventBus eventBus) {
		eventBus.addListener(this::setup);

		SMPBlocks.BLOCKS.register(eventBus);
		LOGGER_REGISTRY.debug("Registered block DeferredRegistry onto EventBus!");
		SMPItems.ITEMS.register(eventBus);
		LOGGER_REGISTRY.debug("Registered item DeferredRegistry onto EventBus!");
		LOGGER.debug("Finished initialization!");
	}

	private void setup(FMLCommonSetupEvent event) {
		SMPFeatureRegistry.applyFeatures();
	}
}
