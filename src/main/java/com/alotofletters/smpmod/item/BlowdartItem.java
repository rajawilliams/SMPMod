package com.alotofletters.smpmod.item;

import com.alotofletters.smpmod.entity.projectile.BlowdartEntity;
import com.alotofletters.smpmod.item.itemgroups.ItemGroupsSMP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Hashtable;
import java.util.List;

public class BlowdartItem extends Item {
	private Potion potion;

	public static final Hashtable<Potion, BlowdartItem> ITEM_HASHTABLE = new Hashtable<>();
	public static final BlowdartItem EMPTY = new BlowdartItem(Potions.EMPTY, new Item.Properties().group(ItemGroupsSMP.MOD_WEAPONS_GROUP));

	public BlowdartItem(Potion potion, Properties properties) {
		super(properties);
		this.potion = potion;
		ITEM_HASHTABLE.put(potion, this);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getDefaultInstance() {
		return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), this.potion);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		PotionUtils.addPotionTooltip(stack, tooltip, 0.125F);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			items.add(getDefaultInstance());
		}
	}

	public BlowdartEntity createDart(World worldIn, ItemStack stack, LivingEntity shooter) {
		BlowdartEntity entity = new BlowdartEntity(worldIn, shooter);
		entity.setPotionEffect(stack);
		return entity;
	}

	public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.PlayerEntity player) {
		int enchant = EnchantmentHelper.getEnchantmentLevel(net.minecraft.enchantment.Enchantments.INFINITY, bow);
		return enchant <= 0 ? false : this.getClass() == BlowdartItem.class;
	}
}
