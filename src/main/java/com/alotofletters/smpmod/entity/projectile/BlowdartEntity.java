package com.alotofletters.smpmod.entity.projectile;

import com.alotofletters.smpmod.entity.EntityTypesSMP;
import com.alotofletters.smpmod.item.BlowdartItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

@SuppressWarnings("unchecked")
public class BlowdartEntity extends AbstractArrowEntity {

	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(ArrowEntity.class, DataSerializers.VARINT);
	private Potion potion = Potions.EMPTY;
	private boolean fixedColor;

	private static final EntityType<BlowdartEntity> ENTITY_TYPE = (EntityType<BlowdartEntity>) EntityTypesSMP.BLOWGUN_DART;

	public BlowdartEntity(World worldIn) {
		super(ENTITY_TYPE, worldIn);
	}

	public BlowdartEntity(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(ENTITY_TYPE, worldIn);
	}

	public BlowdartEntity(World world, LivingEntity shooter) {
		super(ENTITY_TYPE, shooter, world);
	}

	public BlowdartEntity(EntityType<? extends AbstractArrowEntity> entityEntityType, World world) {
		super(entityEntityType, world);
	}
	public void setPotionEffect(ItemStack stack) {
		if (stack.getItem() instanceof BlowdartItem) {
			this.potion = PotionUtils.getPotionFromItem(stack);
		}
	}

	private void refreshColor() {
		this.fixedColor = false;
		if (this.potion == Potions.EMPTY) {
			this.dataManager.set(COLOR, -1);
		} else {
			this.dataManager.set(COLOR, PotionUtils.getPotionColor(this.potion));
		}

	}

	public void addEffect(EffectInstance effect) {
		this.getDataManager().set(COLOR, PotionUtils.getPotionColor(this.potion));
	}

	protected void registerData() {
		super.registerData();
		this.dataManager.register(COLOR, -1);
	}

	private void setFixedColor(int p_191507_1_) {
		this.fixedColor = true;
		this.dataManager.set(COLOR, p_191507_1_);
	}

	public void tick() {
		super.tick();
		if (this.world.isRemote) {
			if (this.inGround) {
				if (this.timeInGround % 5 == 0) {
					this.spawnPotionParticles(1);
				}
			} else {
				this.spawnPotionParticles(2);
			}
		} else if (this.inGround && this.timeInGround != 0 && this.timeInGround >= 600) {
			this.world.setEntityState(this, (byte)0);
			this.potion = Potions.EMPTY;
			this.dataManager.set(COLOR, -1);
		}

	}

	private void spawnPotionParticles(int particleCount) {
		int i = this.getColor();
		if (i != -1 && particleCount > 0) {
			double d0 = (double)(i >> 16 & 255) / 255.0D;
			double d1 = (double)(i >> 8 & 255) / 255.0D;
			double d2 = (double)(i >> 0 & 255) / 255.0D;

			for(int j = 0; j < particleCount; ++j) {
				this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
			}

		}
	}

	public int getColor() {
		return this.dataManager.get(COLOR);
	}

	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.potion != Potions.EMPTY && this.potion != null) {
			compound.putString("Potion", this.potion.getRegistryName().toString()); // 100% sure this is a better way
		}

		if (this.fixedColor) {
			compound.putInt("Color", this.getColor());
		}

	}

	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("Potion", 8)) {
			this.potion = PotionUtils.getPotionTypeFromNBT(compound);
		}
		for(EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(compound)) {
			this.addEffect(effectinstance);
		}
		if (compound.contains("Color", 99)) {
			this.setFixedColor(compound.getInt("Color"));
		} else {
			this.refreshColor();
		}

	}

	protected void arrowHit(LivingEntity living) {
		super.arrowHit(living);
		for(EffectInstance effectinstance : this.potion.getEffects()) {
			living.addPotionEffect(new EffectInstance(effectinstance.getPotion(), Math.max(effectinstance.getDuration() / 8, 1), effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.doesShowParticles()));
		}

	}

	@Override
	protected ItemStack getArrowStack() {
		if ((this.potion == Potions.EMPTY)
				&& (!BlowdartItem.ITEM_HASHTABLE.containsKey(this.potion))) {
			return new ItemStack(BlowdartItem.EMPTY);
		} else {
			return PotionUtils.addPotionToItemStack(new ItemStack(BlowdartItem.ITEM_HASHTABLE.get(this.potion)), this.potion);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 0) {
			int i = this.getColor();
			if (i != -1) {
				double d0 = (double)(i >> 16 & 255) / 255.0D;
				double d1 = (double)(i >> 8 & 255) / 255.0D;
				double d2 = (double)(i >> 0 & 255) / 255.0D;

				for(int j = 0; j < 20; ++j) {
					this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
				}
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
