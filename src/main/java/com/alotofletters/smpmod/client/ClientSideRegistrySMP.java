package com.alotofletters.smpmod.client;

import com.alotofletters.smpmod.client.renderer.entity.DartRenderer;
import com.alotofletters.smpmod.entity.EntityTypesSMP;
import com.alotofletters.smpmod.entity.projectile.BlowdartEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientSideRegistrySMP {

	@SuppressWarnings("unchecked")
	public static void registerEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler((EntityType<BlowdartEntity>)EntityTypesSMP.BLOWGUN_DART, new DartRenderer.RenderFactory());
	}
}
