package com.alotofletters.smpmod.init;

import com.alotofletters.smpmod.SMPMod;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Easy organization for easy registry.
 */
public class SMPBlocks {

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, SMPMod.MODID);

	public static final Block COMPRESSED_SHEEP = new Block(Properties.create(Material.ORGANIC, MaterialColor.PINK).sound(SoundType.SLIME));

	static {
		BLOCKS.register("compressed_sheep", () -> COMPRESSED_SHEEP);
	}

}
