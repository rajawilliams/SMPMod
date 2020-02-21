package com.alotofletters.smpmod.init;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.item.BlowdartItem;
import com.alotofletters.smpmod.item.BlowgunItem;
import com.alotofletters.smpmod.item.ItemTierBlowgun;
import com.alotofletters.smpmod.item.itemgroups.ItemGroupsSMP;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMPItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, SMPMod.MODID);

	public static final RegistryObject<Item> SPIDER_TUSK = ITEMS.register("spider_tusk", () ->
			new Item(new Item.Properties().group(ItemGroupsSMP.MOD_ITEMS_GROUP)));

	public static final RegistryObject<Item> SPIDER_DART = ITEMS.register("spider_dart", () ->
		new BlowdartItem(Potions.LONG_POISON, new Item.Properties().group(ItemGroupsSMP.MOD_WEAPONS_GROUP)));

	public static final RegistryObject<Item> NEUROTOXIN_DART = ITEMS.register("neurotoxin_dart", () ->
			new BlowdartItem(Potions.LONG_WEAKNESS, new Item.Properties().group(ItemGroupsSMP.MOD_WEAPONS_GROUP)));

	public static final RegistryObject<Item> NORMAL_DART = ITEMS.register("dart", () -> BlowdartItem.EMPTY);

	public static final RegistryObject<Item> CANE_BLOWGUN = ITEMS.register("cane_blowgun", () ->
			new BlowgunItem(ItemTierBlowgun.SUGAR, new Item.Properties().group(ItemGroupsSMP.MOD_WEAPONS_GROUP)));

	public static final RegistryObject<Item> BAMBOO_BLOWGUN = ITEMS.register("bamboo_blowgun", () ->
			new BlowgunItem(ItemTierBlowgun.BAMBOO, new Item.Properties().group(ItemGroupsSMP.MOD_WEAPONS_GROUP)));

	public static final RegistryObject<Item> iron_blowgun = ITEMS.register("iron_blowgun", () ->
			new BlowgunItem(ItemTierBlowgun.IRON, new Item.Properties().group(ItemGroupsSMP.MOD_WEAPONS_GROUP).rarity(Rarity.UNCOMMON)));


}
