package com.alotofletters.smpmod.init.gen;

import com.alotofletters.smpmod.SMPMod;
import com.alotofletters.smpmod.gen.dungeon.DungeonPiece;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class SMPPieceRegistry {

	public static final IStructurePieceType SDUNGEON = register("sdungeon", DungeonPiece::new);

	private static IStructurePieceType register(String key, IStructurePieceType type) {
		return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(SMPMod.MODID, key), type);
	}
}
