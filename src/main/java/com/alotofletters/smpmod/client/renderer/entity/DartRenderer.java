package com.alotofletters.smpmod.client.renderer.entity;

import com.alotofletters.smpmod.entity.projectile.BlowdartEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import static com.alotofletters.smpmod.SMPMod.MODID;

public class DartRenderer extends ArrowRenderer<BlowdartEntity> {
	private static final ResourceLocation NORMAL_DART = new ResourceLocation(MODID, "textures/entity/projectiles/normal_dart.png");

	public DartRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public ResourceLocation getEntityTexture(BlowdartEntity entity) {
		return NORMAL_DART;
	}

	public static class RenderFactory implements IRenderFactory<BlowdartEntity>
	{
		@Override
		public EntityRenderer<? super BlowdartEntity> createRenderFor(EntityRendererManager manager)
		{
			return new DartRenderer(manager);
		}

	}
}
