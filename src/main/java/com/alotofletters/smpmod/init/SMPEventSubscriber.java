package com.alotofletters.smpmod.init;

import com.alotofletters.smpmod.SMPMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import net.minecraft.item.Item.Properties;

/**
 * EventBus subscriber for when the initialize function isn't enough
 */
@Mod.EventBusSubscriber(modid = SMPMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMPEventSubscriber {

	/**
	 * Handles the BlockItem registry.
	 * @param event The registry event
	 */
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> itemRegistry = event.getRegistry(); // get the registry for items

		SMPBlocks.BLOCKS.getEntries().stream()
				.map(RegistryObject::get) // get all blocks from the registry
				.forEach(block -> { // loop over each block
					Properties properties = new Properties(); // TODO: .group(ItemGroup) to put in creative inventory groups
					BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(block.getRegistryName());
					itemRegistry.register(blockItem);
				});
	}
}
