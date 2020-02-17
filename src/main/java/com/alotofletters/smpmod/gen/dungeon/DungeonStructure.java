package com.alotofletters.smpmod.gen.dungeon;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Uses templates to use the "data/smpmod/structures/" folder. Insane, right?
 */
public class DungeonStructure extends ScatteredStructure<NoFeatureConfig> {

	public static final Hashtable<Biome, List<WeightedDungeonBiome>> DUNGEON_BIOME_HASHTABLE = new Hashtable<>();

	/**
	 * Confusing constructor for a confusing generational algorithm.
	 * @param function A function? Not referenced directly in mod code.
	 */
	public DungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> function) {
		super(function);
	}

	/**
	 * Get starting position.
	 * @param chunkGenerator World generation algorithm.
	 * @param random Random for generation
	 * @param x start chunk pos x
	 * @param z start chunk pos y
	 * @param spacingOffsetsX How spaced, x-wise?
	 * @param spacingOffsetsZ How spaced, y-wise?
	 * @return Ending chunk position
	 */
	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ) {
		random.setSeed(this.getSeedModifier());
		final int distance = 20;
		final int separation = this.getSize() + 3;
		int x1 = x + distance * spacingOffsetsX;
		int z1 = z + distance * spacingOffsetsZ;
		int x2 = x1 < 0 ? x1 - distance + 1 : x1;
		int z2 = z1 < 0 ? z1 - distance + 1 : z1;
		int x3 = x2 / distance;
		int z3 = z2 / distance;
		((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), x3, z3, this.getSeedModifier());
		x3 = x3 * distance;
		z3 = z3 * distance;
		x3 = x3 + random.nextInt(distance - separation);
		z3 = z3 + random.nextInt(distance - separation);

		return new ChunkPos(x3, z3);
	}

	/**
	 * Seed modifier. Not sure what it is.
	 * @return int that is a seed modifier??
	 */
	@Override
	protected int getSeedModifier() {
		return 784400;
	}

	/**
	 * Seems to create a generator factory? Not sure.
	 * @return Constructor for DungeonStructure.Start.
	 * @see DungeonStructure.Start
	 */
	@Override
	public IStartFactory getStartFactory() {
		return Start::new;
	}

	/**
	 * The name of the structure for /locate among other things.
	 * Has to be human readable, aka display name in the locate
	 * command.
	 * @return String of the name of the structure.
	 */
	@Override
	public String getStructureName() {
		return "Improved_Dungeon";
	}

	/**
	 * I'll assume this is about how much the size should be.
	 * @return Size of the structure.
	 */
	@Override
	public int getSize() {
		return 5;
	}

	public static class Start extends StructureStart {

		/**
		 * Constructor for StructureStart. Required for some dumb reason.
		 */
		public Start(Structure<?> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		/**
		 * Actually generate the structure!
		 * @param generator Similar to IWorld I believe.
		 * @param templateManagerIn TemplateManager to get the actual templates.
		 * @param chunkX Chunk-relative x position.
		 * @param chunkZ Chunk-relative z position
		 * @param biomeIn The biome the structure is being generated in.
		 */
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
			checkBiomeHashtable();
			int x = (chunkX << 4) + 2;
			int z = (chunkZ << 4) + 2;

			Rotation rotation = Rotation.NONE;

			int i1 = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
			int y = i1 - this.rand.nextInt(i1 + 12) - 10;

			DungeonPiece piece;
			DungeonBiome biome = WeightedRandom.getRandomItem(rand, DUNGEON_BIOME_HASHTABLE.get(biomeIn)).biome;
			piece = new DungeonPiece(templateManagerIn, new BlockPos(x, y, z), rotation, biome);

			this.components.add(piece); // add to components list
			this.recalculateStructureSize(); // recalculate structure size
		}
	}

	private static void checkBiomeHashtable() {
		for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
			if (!DUNGEON_BIOME_HASHTABLE.contains(biome)) {
				registerBiome(biome, DungeonBiome.PLAINS, 1); // TODO: not default to plains eventually?
			}
		}
	}

	private static void registerBiome(Biome biome, DungeonBiome dungeonBiome, int weight) {
		if (DUNGEON_BIOME_HASHTABLE.get(biome) != null) {
			DUNGEON_BIOME_HASHTABLE.get(biome).add(new WeightedDungeonBiome(weight, dungeonBiome));
		} else {
			DUNGEON_BIOME_HASHTABLE.put(biome, Lists.newArrayList(new WeightedDungeonBiome(weight, dungeonBiome)));
		}
	}

	static {
		// FLOWER
		registerBiome(Biomes.FLOWER_FOREST, DungeonBiome.FLOWER,1);
		registerBiome(Biomes.SUNFLOWER_PLAINS, DungeonBiome.FLOWER,1);

		// DESERT
		registerBiome(Biomes.DESERT, DungeonBiome.DESERT,2);
		registerBiome(Biomes.DESERT_HILLS, DungeonBiome.DESERT,2);
		registerBiome(Biomes.DESERT_LAKES, DungeonBiome.DESERT,2);
		registerBiome(Biomes.BEACH, DungeonBiome.DESERT,2);
		registerBiome(Biomes.SAVANNA, DungeonBiome.DESERT,2);
		registerBiome(Biomes.SAVANNA_PLATEAU, DungeonBiome.DESERT,2);
		registerBiome(Biomes.SHATTERED_SAVANNA, DungeonBiome.DESERT,2);
		registerBiome(Biomes.SHATTERED_SAVANNA_PLATEAU, DungeonBiome.DESERT,2);

		// DESERT_SPIDER
		registerBiome(Biomes.DESERT, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.DESERT_HILLS, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.DESERT_LAKES, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.BEACH, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.SAVANNA, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.SAVANNA_PLATEAU, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.SHATTERED_SAVANNA, DungeonBiome.DESERT_SPIDER,1);
		registerBiome(Biomes.SHATTERED_SAVANNA_PLATEAU, DungeonBiome.DESERT_SPIDER,1);

		// SPRUCE_GENERAL
		registerBiome(Biomes.TAIGA, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.TAIGA_HILLS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.TAIGA_MOUNTAINS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.GIANT_SPRUCE_TAIGA_HILLS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.GIANT_TREE_TAIGA_HILLS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.SNOWY_TAIGA_HILLS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.SNOWY_TAIGA_MOUNTAINS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.MOUNTAINS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.MOUNTAIN_EDGE, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.GRAVELLY_MOUNTAINS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.WOODED_MOUNTAINS, DungeonBiome.SPRUCE_GENERAL,1);
		registerBiome(Biomes.MODIFIED_GRAVELLY_MOUNTAINS, DungeonBiome.SPRUCE_GENERAL,1);
	}

	public static class WeightedDungeonBiome extends WeightedRandom.Item {

		public final DungeonBiome biome;

		public WeightedDungeonBiome(int itemWeightIn, DungeonBiome biome) {
			super(itemWeightIn);
			this.biome = biome;
		}

		@Override
		public boolean equals(Object target)
		{
			return target instanceof WeightedDungeonBiome && this.biome.equals(((WeightedDungeonBiome)target).biome);
		}
	}

}
