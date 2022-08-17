package br.com.acenetwork.commons.constants;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;

public enum EntityCategory
{
	UNDEAD, ARTHROPOD, OTHER;
	
	public static EntityCategory getCategory(Entity e)
	{
		switch(e.getType())
		{
		case HORSE:
			Horse horse = (Horse) e;
			
			switch(horse.getVariant())
			{
			case UNDEAD_HORSE:
			case SKELETON_HORSE:
				break;
			default:
				return OTHER;
			}
		case ZOMBIE:
		case PIG_ZOMBIE:
		case WITHER:
		case SKELETON:
		case GIANT:
			return UNDEAD;
		case SPIDER:
		case CAVE_SPIDER:
		case SILVERFISH:
		case ENDERMITE:
			return ARTHROPOD;
		default:
			return OTHER;
		}
	}
}