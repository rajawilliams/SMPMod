package com.alotofletters.smpmod.item.itemgroups;

import com.alotofletters.smpmod.init.SMPBlocks;
import com.alotofletters.smpmod.init.SMPEventSubscriber;
import com.alotofletters.smpmod.init.SMPItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ItemGroupsSMP {

	public static final ItemGroup MOD_BLOCKS_GROUP = new ItemGroupMod("smp_blocks", () -> SMPEventSubscriber.COMPRESSED_SHEEP_BLOCK);
	public static final ItemGroup MOD_ITEMS_GROUP = new ItemGroupMod("smp_items", SMPItems.SPIDER_TUSK::get);
	public static final ItemGroup MOD_WEAPONS_GROUP = new ItemGroupMod("smp_weapons", SMPItems.BAMBOO_BLOWGUN::get);

	public static class ItemGroupMod extends ItemGroup {

		private final Supplier<Item> itemSupplier;

		public ItemGroupMod(String label, Supplier<Item> itemSupplier) {
			super(label);
			this.itemSupplier = itemSupplier;
		}

		@Override
		@MethodsReturnNonnullByDefault
		public ItemStack createIcon() {
			return new ItemStack(itemSupplier.get());
		}
	}
}
