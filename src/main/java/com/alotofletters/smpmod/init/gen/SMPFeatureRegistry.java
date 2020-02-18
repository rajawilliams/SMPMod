package com.alotofletters.smpmod.init.gen;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.gen.dungeon.DungeonStructure;
import com.alotofletters.smpmod.gen.fakewell.FakeWellStructure;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import static com.alotofletters.smpmod.SMPMod.MODID;

/**
 * Make sure the structures are registered; otherwise...
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD) // subscribe to the MOD bus
@ObjectHolder(MODID)
public class SMPFeatureRegistry {

	/**
	 * Dungeon structure feature.
	 */
	@ObjectHolder(MODID + ":dungeon")
	public static Structure<NoFeatureConfig> DUNGEON_FEATURE;

	@ObjectHolder(MODID + ":fake_well")
	public static Structure<NoFeatureConfig> FAKE_WELL_FEATURE;

	/**
	 * Registry event when Features are registered.
	 * @param event Event from subscription.
	 */
	@SubscribeEvent
	public static void onRegisterFeature(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(new DungeonStructure(NoFeatureConfig::deserialize).setRegistryName(MODID + ":dungeon"));
		event.getRegistry().register(new FakeWellStructure(NoFeatureConfig::deserialize).setRegistryName(MODID + ":fake_well"));
	}

	/**
	 * Apply features to terrain.
	 */
	public static void applyFeatures() {
		for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
			if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
				addStructure(biome, GenerationStage.Decoration.UNDERGROUND_STRUCTURES, DUNGEON_FEATURE);
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
						Feature.ORE.withConfiguration(
								new OreFeatureConfig(
										OreFeatureConfig.FillerBlockType.NATURAL_STONE,
										Blocks.IRON_ORE.getDefaultState(),
										25))
								.func_227228_a_(Placement.COUNT_VERY_BIASED_RANGE.func_227446_a_(new CountRangeConfig(1, 10, 0, 64))));
			}
		}
		addStructure(Biomes.DESERT, GenerationStage.Decoration.SURFACE_STRUCTURES, FAKE_WELL_FEATURE);
	}

	/**
	 * Ripped from Tinker's Construct. Love those guys.
	 * @param biome Biome the structure can generate in.
	 * @param structure Structure to be generated.
	 */
	private static void addStructure(Biome biome, GenerationStage.Decoration stage, Structure<NoFeatureConfig> structure) {
		biome.addFeature(stage, structure
				.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
				.func_227228_a_(Placement.NOPE.func_227446_a_(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
	}

	private static void addCaveStructure(Biome biome, GenerationStage.Decoration stage, Structure<NoFeatureConfig> structure) {
		biome.addFeature(stage, structure
				.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
				.func_227228_a_(Placement.CARVING_MASK.func_227446_a_(new CaveEdgeConfig(GenerationStage.Carving.AIR, 1f))));
		biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
	}
}
