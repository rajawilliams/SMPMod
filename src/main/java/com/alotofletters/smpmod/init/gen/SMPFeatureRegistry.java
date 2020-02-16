package com.alotofletters.smpmod.init.gen;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.gen.dungeon.DungeonStructure;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
	@ObjectHolder(MODID + ":sdungeon")
	public static Structure<NoFeatureConfig> DUNGEON_FEATURE;

	/**
	 * Registry event when Features are registered.
	 * @param event Event from subscription.
	 */
	@SubscribeEvent
	public static void onRegisterFeature(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(new DungeonStructure(NoFeatureConfig::deserialize).setRegistryName(MODID + ":sdungeon"));
	}

	public static void applyFeatures() {

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
}
