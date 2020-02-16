package com.alotofletters.smpmod.gen.dungeon;

import net.minecraft.entity.EntityType;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Dungeon hooks; but better.
 */
public class DesertDungeonHooks
{
	private static ArrayList<DesertDungeonMob> dungeonMobs = new ArrayList<DesertDungeonMob>();

	public static float addDungeonMob(EntityType<?> type, int rarity)
	{
		if (rarity <= 0)
		{
			throw new IllegalArgumentException("Rarity must be greater then zero");
		}

		Iterator<DesertDungeonMob> itr = dungeonMobs.iterator();
		while (itr.hasNext())
		{
			DesertDungeonMob mob = itr.next();
			if (type == mob.type)
			{
				itr.remove();
				rarity = mob.itemWeight + rarity;
				break;
			}
		}

		dungeonMobs.add(new DesertDungeonMob(rarity, type));
		return rarity;
	}

	public static int removeDungeonMob(EntityType<?> name)
	{
		for (DesertDungeonMob mob : dungeonMobs)
		{
			if (name == mob.type)
			{
				dungeonMobs.remove(mob);
				return mob.itemWeight;
			}
		}
		return 0;
	}

	/**
	 * Gets a random mob name from the list.
	 * @param rand World generation random number generator
	 * @return The mob name
	 */
	public static EntityType<?> getRandomDungeonMob(Random rand)
	{
		DesertDungeonMob mob = WeightedRandom.getRandomItem(rand, dungeonMobs);
		return mob.type;
	}


	public static class DesertDungeonMob extends WeightedRandom.Item
	{
		public final EntityType<?> type;
		public DesertDungeonMob(int weight, EntityType<?> type)
		{
			super(weight);
			this.type = type;
		}

		@Override
		public boolean equals(Object target)
		{
			return target instanceof DesertDungeonMob && type.equals(((DesertDungeonMob)target).type);
		}
	}

	static
	{
		addDungeonMob(EntityType.HUSK,		100);
		addDungeonMob(EntityType.STRAY,		200);
		addDungeonMob(EntityType.SPIDER,	100);
	}
}