package com.alotofletters.smpmod.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ItemTierBlowgun implements IItemTier {

	BAMBOO(60, 3.0f, 2.0f, 12, () -> {
		return Ingredient.fromItems(Items.BAMBOO, Items.SUGAR_CANE);
	}),
	SUGAR(32, 2.0f, 1.0f, 7, () -> {
		return Ingredient.fromItems(Items.SUGAR_CANE);
	}),
	IRON(250, 5.0f, 3.0f, 14, () -> {
		return Ingredient.fromItems(Items.IRON_INGOT);
	});

	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyValue<Ingredient> repairMaterial;

	/**
	 * More efficiency means more time to shoot, however
	 * guarantees higher
	 * @param maxUsesIn Max damage before breaking
	 * @param efficiencyIn Damage modifier, also tickrate modifier
	 * @param attackDamageIn Base damage
	 * @param enchantabilityIn How enchantable is the tier?
	 * @param repairMaterialIn
	 */
	ItemTierBlowgun(int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		this.maxUses = maxUsesIn;
		this.efficiency = efficiencyIn;
		this.attackDamage = attackDamageIn;
		this.enchantability = enchantabilityIn;
		this.repairMaterial = new LazyValue<>(repairMaterialIn);
	}

	@Override
	public int getMaxUses() { return this.maxUses; }

	@Override
	public float getEfficiency() {
		return 0;
	}

	@Override
	public float getAttackDamage() {
		return 0;
	}

	@Override
	public int getHarvestLevel() {
		return 0;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return null;
	}
}
