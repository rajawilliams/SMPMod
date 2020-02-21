package com.alotofletters.smpmod.init;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.item.itemgroups.ItemGroupsSMP;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
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

	public static final BlockItem COMPRESSED_SHEEP_BLOCK = new BlockItem(SMPBlocks.COMPRESSED_SHEEP, new Properties().group(ItemGroupsSMP.MOD_BLOCKS_GROUP));

	/**
	 * Handles the BlockItem registry.
	 * @param event The registry event
	 */
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> itemRegistry = event.getRegistry(); // get the registry for items
		itemRegistry.registerAll(COMPRESSED_SHEEP_BLOCK.setRegistryName("compressed_sheep"));
		SMPBlocks.BLOCKS.getEntries().stream()
				.map(RegistryObject::get) // get all blocks from the registry
				.forEach(block -> { // loop over each block
					if (COMPRESSED_SHEEP_BLOCK.getBlock().equals(block)) { return; }
					Properties properties = new Properties().group(ItemGroupsSMP.MOD_BLOCKS_GROUP);
					BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(block.getRegistryName());
					itemRegistry.register(blockItem);
				});
	}

	/*@SubscribeEvent
	public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if (Blocks.SPAWNER.getDefaultState().equals(event.getState().getBlock())) {
			PlayerEntity playerEntity = event.getHarvester();
		}
	}*/

	static {

	}
}
