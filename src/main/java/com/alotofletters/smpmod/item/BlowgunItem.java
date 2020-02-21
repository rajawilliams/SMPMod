package com.alotofletters.smpmod.item;

import com.alotofletters.smpmod.entity.projectile.BlowdartEntity;
import com.alotofletters.smpmod.init.SMPItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BlowgunItem extends ShootableItem {
	public static final Predicate<ItemStack> DARTS = stack -> stack.getItem() instanceof BlowdartItem;
	private final IItemTier tier;

	public BlowgunItem(IItemTier tier, Item.Properties properties) {
		super(properties.defaultMaxDamage(tier.getMaxUses()));
		this.tier = tier;
	}

	@Override
	public int getItemEnchantability() {
		return this.tier.getEnchantability();
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return this.tier.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		boolean flag = playerIn.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
		ItemStack ammoStack = playerIn.findAmmo(stack);

		if (!ammoStack.isEmpty() || flag) {
			if (ammoStack.isEmpty()) {
				ammoStack = new ItemStack(SMPItems.NORMAL_DART.get());
			}

			boolean flag1 = playerIn.abilities.isCreativeMode || (stack.getItem() instanceof BlowdartItem && ((BlowdartItem)ammoStack.getItem()).isInfinite(ammoStack, stack, playerIn));
			if (!worldIn.isRemote) {
				BlowdartItem blowdartItem = (BlowdartItem)(ammoStack.getItem() instanceof BlowdartItem ? ammoStack.getItem() : SMPItems.NORMAL_DART.get());
				BlowdartEntity blowdartEntity = blowdartItem.createDart(worldIn, ammoStack, playerIn);
				blowdartEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.0F, 3.0F);

				blowdartEntity.setDamage(1); // low damage
				blowdartEntity.setDamage(blowdartEntity.getDamage() + blowdartEntity.getDamage() * tier.getEfficiency() + tier.getAttackDamage());
				int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
				if (j > 0) {
					blowdartEntity.setDamage(blowdartEntity.getDamage() + (double)j * 0.5D + 0.5D);
				}

				int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
				if (k > 0) {
					blowdartEntity.setKnockbackStrength(k);
				}

				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
					blowdartEntity.setFire(100);
				}

				stack.damageItem(1, playerIn, (p_220009_1_) -> {
					p_220009_1_.sendBreakAnimation(playerIn.getActiveHand());
				});

				worldIn.addEntity(blowdartEntity);
			}

			worldIn.playSound(null,
					playerIn.getPosX(),
					playerIn.getPosY(),
					playerIn.getPosZ(),
					SoundEvents.ENTITY_ARROW_SHOOT,
					SoundCategory.PLAYERS,
					1.0F,
					1.0F / (random.nextFloat() * 0.4F + 1.2F) + 0.5F);

			if (!flag1 && !playerIn.abilities.isCreativeMode) {
				ammoStack.shrink(1);
				if (ammoStack.isEmpty()) {
					playerIn.inventory.deleteStack(ammoStack);
				}
			}

			playerIn.addStat(Stats.ITEM_USED.get(this));
			playerIn.getCooldownTracker().setCooldown(this, (int)(15 + 15 * tier.getEfficiency()));
		}

		return ActionResult.func_226248_a_(stack);
	}

	@Override
	public Predicate<ItemStack> getInventoryAmmoPredicate() {
		return DARTS;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
}
