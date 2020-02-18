package com.alotofletters.smpmod.gen.fakewell;

import com.mojang.datafixers.Dynamic;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.placement.CountConfig;
import net.minecraftforge.common.DimensionManager;

import java.util.Random;
import java.util.function.Function;

public class FakeWellStructure extends ScatteredStructure<NoFeatureConfig> {

	public FakeWellStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	protected int getSeedModifier() {
		return 5842500;
	}

	@Override
	public IStartFactory getStartFactory() {
		return Start::new;
	}

	@Override
	public String getStructureName() {
		return "Fake_Well";
	}

	@Override
	public int getSize() {
		return 13;
	}

	public static class Start extends StructureStart {

		public Start(Structure<?> structure, int i, int i1, MutableBoundingBox mutableBoundingBox, int i2, long l) {
			super(structure, i, i1, mutableBoundingBox, i2, l);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
			int x = (chunkX << 4) + 2; // 2 is close enough... right?
			int z = (chunkZ << 4) + 2;
			int y = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG) - 8;
			FakeWellPiece piece = new FakeWellPiece(templateManagerIn, new BlockPos(x, y, z));
			this.components.add(piece);
			this.recalculateStructureSize();
		}
	}
}
