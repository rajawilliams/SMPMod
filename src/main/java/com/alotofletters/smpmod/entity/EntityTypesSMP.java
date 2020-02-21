package com.alotofletters.smpmod.entity;

import com.alotofletters.smpmod.entity.projectile.BlowdartEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alotofletters.smpmod.SMPMod.MODID;

public class EntityTypesSMP {

	public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);

	public static EntityType<?> BLOWGUN_DART = EntityType.Builder.<BlowdartEntity>create(BlowdartEntity::new, EntityClassification.MISC)
			.size(0.5F, 0.5F)
			.setCustomClientFactory(BlowdartEntity::new)
			.build(MODID + ":blowgun_dart");

	static {
		ENTITIES.register("blowgun_dart", () -> BLOWGUN_DART);
	}


}
