package com.alotofletters.smpmod.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;

import static net.minecraft.item.ItemGroup.TOOLS;

import java.util.Arrays;

public class ProspectingEnchantment extends Enchantment {
	private static final EnchantmentType GOLDEN_PICKAXE = EnchantmentType.create("GOLDEN_PICKAXE", item -> item.equals(Items.GOLDEN_PICKAXE));

	public ProspectingEnchantment() {
		super(Rarity.RARE, GOLDEN_PICKAXE, new EquipmentSlotType[]{});
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return ItemTier.GOLD.getEnchantability() - 3;
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

//	static {
//		EnchantmentType[] enchantmentTypes = TOOLS.getRelevantEnchantmentTypes();
//		EnchantmentType[] newEnchantmentTypes = Arrays.copyOf(enchantmentTypes, enchantmentTypes.length + 1);
//		newEnchantmentTypes[enchantmentTypes.length + 1] = GOLDEN_PICKAXE;
//		TOOLS.setRelevantEnchantmentTypes(newEnchantmentTypes);
//	}
}
