package io.github.gaming32.bingo.fabric.datagen.goal;

import io.github.gaming32.bingo.conditions.BlockPatternCondition;
import io.github.gaming32.bingo.conditions.PillarCondition;
import io.github.gaming32.bingo.conditions.StairwayToHeavenCondition;
import io.github.gaming32.bingo.data.BingoDifficulties;
import io.github.gaming32.bingo.data.BingoTags;
import io.github.gaming32.bingo.data.goal.BingoGoal;
import io.github.gaming32.bingo.data.icons.BlockIcon;
import io.github.gaming32.bingo.data.icons.CycleIcon;
import io.github.gaming32.bingo.data.icons.EntityIcon;
import io.github.gaming32.bingo.data.subs.BingoSub;
import io.github.gaming32.bingo.data.tags.bingo.BingoDamageTypeTags;
import io.github.gaming32.bingo.data.tags.bingo.BingoFeatureTags;
import io.github.gaming32.bingo.data.tags.bingo.BingoItemTags;
import io.github.gaming32.bingo.triggers.BreakBlockTrigger;
import io.github.gaming32.bingo.triggers.CompleteMapTrigger;
import io.github.gaming32.bingo.triggers.EnchantedItemTrigger;
import io.github.gaming32.bingo.triggers.EntityKilledPlayerTrigger;
import io.github.gaming32.bingo.triggers.EntityTrigger;
import io.github.gaming32.bingo.triggers.EquipItemTrigger;
import io.github.gaming32.bingo.triggers.GrowFeatureTrigger;
import io.github.gaming32.bingo.triggers.ItemPickedUpTrigger;
import io.github.gaming32.bingo.triggers.KillItemTrigger;
import io.github.gaming32.bingo.triggers.RelativeStatsTrigger;
import io.github.gaming32.bingo.triggers.TotalCountInventoryChangeTrigger;
import io.github.gaming32.bingo.util.BlockPattern;
import io.github.gaming32.bingo.util.ResourceLocations;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FilledBucketTrigger;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.component.predicates.JukeboxPlayablePredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class HardGoalProvider extends DifficultyGoalProvider {
    public HardGoalProvider(BiConsumer<ResourceLocation, BingoGoal> goalAdder, HolderLookup.Provider registries) {
        super(BingoDifficulties.HARD, goalAdder, registries);
    }

    @Override
    public void addGoals() {
        final var bannerPatterns = registries.lookupOrThrow(Registries.BANNER_PATTERN);
        final var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        final var items = registries.lookupOrThrow(Registries.ITEM);
        final var entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        final var blocks = registries.lookupOrThrow(Registries.BLOCK);

        addGoal(BingoGoal.builder(id("level_10_enchant"))
            .criterion("enchant", EnchantedItemTrigger.builder().requiredLevels(MinMaxBounds.Ints.atLeast(10)).build())
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .name("level_10_enchant")
            .icon(new ItemStack(Items.ENCHANTING_TABLE, 10)));
        addGoal(BingoGoal.builder(id("milk_mooshroom"))
            .criterion("obtain", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(items, Items.BUCKET),
                Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityType.MOOSHROOM).build()))
            ))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name("milk_mooshroom")
            .icon(EntityIcon.ofSpawnEgg(EntityType.MOOSHROOM))
            .infrequency(2)
        );
        addGoal(BingoGoal.builder(id("shear_mooshroom"))
            .criterion("obtain", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(items, Items.SHEARS),
                Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityType.MOOSHROOM).build()))
            ))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name("shear_mooshroom")
            .icon(EntityIcon.ofSpawnEgg(EntityType.COW))
            .infrequency(2)
        );
        addGoal(obtainItemGoal(id("sea_lantern"), items, Items.SEA_LANTERN, 2, 5)
            .tags(BingoTags.OVERWORLD, BingoTags.OCEAN));
        addGoal(obtainItemGoal(id("sponge"), items, Items.SPONGE)
            .tooltip("sponge")
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("listen_to_music"))
            .criterion("obtain", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(blocks, Blocks.JUKEBOX)),
                ItemPredicate.Builder.item()
                    .withComponents(DataComponentMatchers.Builder.components()
                        .partial(DataComponentPredicates.JUKEBOX_PLAYABLE, JukeboxPlayablePredicate.any())
                        .build()
                    )
            ))
            .tags(BingoTags.ITEM)
            .name("listen_to_music")
            .icon(Items.JUKEBOX));
        addGoal(obtainSomeItemsFromTag(id("different_flowers"), ConventionalItemTags.FLOWERS, "bingo.goal.different_flowers", 11, 14)
            .antisynergy("flowers")
            .infrequency(3)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("diamond_block"), items, Items.DIAMOND_BLOCK, 2, 4)
            .infrequency(2));
        addGoal(BingoGoal.builder(id("zombified_piglin_sword"))
            .criterion("pickup", ItemPickedUpTrigger.TriggerInstance.pickedUpFrom(
                ItemPredicate.Builder.item().of(items, Items.GOLDEN_SWORD).build(),
                EntityPredicate.Builder.entity().of(entityTypes, EntityType.ZOMBIFIED_PIGLIN).build()
            ))
            .reactant("pacifist")
            .tags(BingoTags.ITEM, BingoTags.COMBAT, BingoTags.NETHER)
            .name("zombified_piglin_sword")
            .icon(Items.GOLDEN_SWORD));
        // TODO: finish by launching fireworks of n different colors
        addGoal(BingoGoal.builder(id("nametag_enderman"))
            .criterion("nametag", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(items, Items.NAME_TAG),
                Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityType.ENDERMAN).build()))
            ))
            .tags(BingoTags.ACTION, BingoTags.COMBAT, BingoTags.OVERWORLD)
            .name("nametag_enderman")
            .icon(Items.NAME_TAG));
        addGoal(BingoGoal.builder(id("finish_on_blaze_spawner"))
            .criterion("spawner", PlayerTrigger.TriggerInstance.located(EntityPredicate.Builder.entity()
                .steppingOn(LocationPredicate.Builder.location()
                    .setBlock(spawnerPredicate(blocks, entityTypes, EntityType.BLAZE))
                )
            ))
            .tags(BingoTags.ACTION, BingoTags.NETHER, BingoTags.COMBAT, BingoTags.FINISH)
            .name(Component.translatable(
                "bingo.goal.finish_on_top_of_a",
                Component.translatable(
                    "bingo.spawner",
                    EntityType.BLAZE.getDescription(),
                    Blocks.SPAWNER.getName()
                )
            ))
            .icon(Blocks.SPAWNER)
        );
        addGoal(obtainSomeItemsFromTag(id("wool"), ItemTags.WOOL, "bingo.goal.wool", 12, 14)
            .antisynergy("wool_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("terracotta"), ItemTags.TERRACOTTA, "bingo.goal.terracotta", 12, 14)
            .reactant("use_furnace")
            .antisynergy("terracotta_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("glazed_terracotta"), ConventionalItemTags.GLAZED_TERRACOTTAS, "bingo.goal.glazed_terracotta", 11, 14)
            .reactant("use_furnace")
            .antisynergy("glazed_terracotta_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("concrete"), ConventionalItemTags.CONCRETES, "bingo.goal.concrete", 11, 14)
            .antisynergy("concrete_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(bedRowGoal(id("bed_row"), 11, 14));
        addGoal(BingoGoal.builder(id("poison_parrot"))
            .criterion("poison", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(items, Items.COOKIE),
                Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityType.PARROT).build()))
            ))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name("poison_parrot")
            .icon(Items.COOKIE)
            .infrequency(2)
            .reactant("pacifist"));
        addGoal(BingoGoal.builder(id("tame_parrot"))
            .criterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(entityTypes, EntityType.PARROT)))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name("tame_parrot")
            .icon(EntityIcon.ofSpawnEgg(EntityType.PARROT))
            .infrequency(2)
        );
        addGoal(BingoGoal.builder(id("ice_on_magma"))
            .criterion("obtain", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                AnyOfCondition.anyOf(
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ICE),
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.MAGMA_BLOCK)
                ),
                BlockPatternCondition.builder().aisle("i", "m")
                    .where('i', BlockPredicate.Builder.block().of(blocks, Blocks.ICE))
                    .where('m', BlockPredicate.Builder.block().of(blocks, Blocks.MAGMA_BLOCK))
                    .rotations(BlockPattern.Rotations.NONE)))
            .tags(BingoTags.ITEM, BingoTags.BUILD, BingoTags.OVERWORLD)
            .name("ice_on_magma")
            .icon(Items.BASALT));
        addGoal(obtainLevelsGoal(id("levels"), 27, 37));
        addGoal(blockCubeGoal(id("ice_cube"), Items.ICE, BlockTags.ICE, Blocks.ICE.getName()));
        addGoal(BingoGoal.builder(id("stairway_to_heaven"))
            .criterion("stairway", CriteriaTriggers.LOCATION.createCriterion(
                new PlayerTrigger.TriggerInstance(
                    Optional.of(ContextAwarePredicate.create(
                        LocationCheck.checkLocation(
                            LocationPredicate.Builder.location().setY(MinMaxBounds.Doubles.atLeast(319))
                        ).build(),
                        StairwayToHeavenCondition.INSTANCE
                    ))
                )
            ))
            .name("stairway_to_heaven")
            .tooltip("stairway_to_heaven")
            .tags(BingoTags.BUILD, BingoTags.OVERWORLD, BingoTags.FINISH)
            .icon(Blocks.COBBLESTONE_STAIRS)
        );
        addGoal(BingoGoal.builder(id("kill_ghast_in_overworld"))
            .criterion("murder", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity()
                .of(entityTypes, EntityType.GHAST)
                .located(LocationPredicate.Builder.inDimension(Level.OVERWORLD))
            ))
            .name("kill_ghast_in_overworld")
            .tags(BingoTags.ACTION, BingoTags.NETHER, BingoTags.OVERWORLD)
            .icon(Items.GHAST_TEAR)
            .reactant("pacifist"));
        addGoal(obtainItemGoal(id("enchanted_golden_apple"), items, Items.ENCHANTED_GOLDEN_APPLE));

        addGoal(BingoGoal.builder(id("never_wear_armor_or_use_shields"))
            .criterion("equip", EquipItemTrigger.builder()
                .newItem(ItemPredicate.Builder.item().of(items, ConventionalItemTags.ARMORS).build())
                .slots(EquipmentSlot.Type.HUMANOID_ARMOR)
                .build()
            )
            .criterion("use", CriteriaTriggers.USING_ITEM.createCriterion(
                new UsingItemTrigger.TriggerInstance(
                    Optional.empty(),
                    Optional.of(ItemPredicate.Builder.item().of(items, ConventionalItemTags.SHIELD_TOOLS).build())
                )
            ))
            .requirements(AdvancementRequirements.Strategy.OR)
            .tags(BingoTags.NEVER)
            .name("never_wear_armor_or_use_shields")
            .tooltip("never_wear_armor_or_use_shields")
            .icon(makeItemWithGlint(Items.SHIELD))
            .antisynergy("never_wear_armor")
            .catalyst("wear_armor")
        );
        addGoal(BingoGoal.builder(id("full_iron_mob"))
            .criterion("kill", CriteriaTriggers.PLAYER_KILLED_ENTITY.createCriterion(
                new KilledTrigger.TriggerInstance(
                    Optional.empty(),
                    Optional.of(ContextAwarePredicate.create(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity().of(entityTypes, EntityType.PLAYER).build()
                        ).invert().build(),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment()
                                .head(ItemPredicate.Builder.item().of(items, Items.IRON_HELMET))
                                .chest(ItemPredicate.Builder.item().of(items, Items.IRON_CHESTPLATE))
                                .legs(ItemPredicate.Builder.item().of(items, Items.IRON_LEGGINGS))
                                .feet(ItemPredicate.Builder.item().of(items, Items.IRON_BOOTS))
                                .build()
                            )
                        ).build()
                    )),
                    Optional.empty()
                )
            ))
            .tags(BingoTags.ACTION, BingoTags.COMBAT)
            .reactant("pacifist")
            .name("full_iron_mob")
            .icon(makeItemWithGlint(Items.IRON_SWORD))
        );
        addGoal(BingoGoal.builder(id("enchant_5_items"))
            .criterion("enchant", RelativeStatsTrigger.builder()
                .stat(Stats.ENCHANT_ITEM, MinMaxBounds.Ints.atLeast(5))
                .build()
            )
            .progress("enchant")
            .name("enchant_5_items")
            .icon(makeItemWithGlint(new ItemStack(Items.STICK, 5)))
            .antisynergy("enchant")
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
        );
        addGoal(BingoGoal.builder(id("never_use_buckets"))
            .criterion("filled_bucket", CriteriaTriggers.FILLED_BUCKET.createCriterion(
                new FilledBucketTrigger.TriggerInstance(Optional.empty(), Optional.empty())
            ))
            .criterion("placed_block", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ConventionalItemTags.BUCKETS))))
            .criterion("use_on_entity", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(items, ConventionalItemTags.BUCKETS), Optional.empty()))
            .criterion("consume", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(items, ConventionalItemTags.BUCKETS)))
            .requirements(AdvancementRequirements.Strategy.OR)
            .tags(BingoTags.NEVER)
            .catalyst("use_buckets")
            .name("never_use_buckets")
            .icon(Items.BUCKET));
        addGoal(obtainItemGoal(id("conduit"), items, Items.CONDUIT)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("dead_coral_blocks"), BingoItemTags.DEAD_CORAL_BLOCKS, "bingo.goal.dead_coral_blocks", 2, 5)
            .tags(BingoTags.OCEAN, BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("sea_pickle"), items, Items.SEA_PICKLE, 16, 32)
            .tags(BingoTags.OCEAN, BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("void_death_to_entity"))
            .criterion("death", EntityKilledPlayerTrigger.builder()
                .source(DamageSourcePredicate.Builder.damageType()
                    .tag(TagPredicate.is(BingoDamageTypeTags.VOID))
                    .build()
                )
                .build()
            )
            .tags(BingoTags.ACTION)
            .name("void_death_to_entity")
            .icon(Blocks.BLACK_CONCRETE)
        );
        addGoal(obtainItemGoal(id("cookie"), items, Items.COOKIE)
            .tags(BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("mega_jungle_tree"))
            .criterion("grow", GrowFeatureTrigger.builder()
                .feature(BingoFeatureTags.MEGA_JUNGLE_TREES)
                .build()
            )
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name("mega_jungle_tree")
            .icon(new ItemStack(Blocks.JUNGLE_SAPLING, 4))
        );
        addGoal(obtainItemGoal(id("prismarine_shard"), items, Items.PRISMARINE_SHARD, 2, 10)
            .infrequency(2)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("jungle_log"), items, Items.JUNGLE_LOG, 16, 32)
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("jungle_wood"), items, Items.JUNGLE_WOOD, 11, 20)
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("stripped_jungle_wood"), items, Items.STRIPPED_JUNGLE_WOOD, 11, 20)
            .reactant("axe_use")
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("stripped_jungle_log"), items, Items.STRIPPED_JUNGLE_LOG, 11, 20)
            .reactant("axe_use")
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("diamond_in_name"), BingoItemTags.DIAMOND_IN_NAME, "bingo.goal.diamond_in_name", 5, 7)
            .antisynergy("diamond_items")
            .tooltip("diamond_in_name"));
        addGoal(BingoGoal.builder(id("destroy_spawner"))
            .criterion("destroy", BreakBlockTrigger.builder().block(blocks, Blocks.SPAWNER).build())
            .name("destroy_spawner")
            .icon(Blocks.SPAWNER)
            .tags(BingoTags.ACTION, BingoTags.COMBAT));
        addGoal(obtainItemGoal(id("popped_chorus_fruit"), items, Items.POPPED_CHORUS_FRUIT, 32, 64)
            .tags(BingoTags.END));
        addGoal(BingoGoal.builder(id("villager_in_end"))
            .criterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity(
                EntityPredicate.Builder.entity()
                    .of(entityTypes, EntityType.VILLAGER)
                    .located(LocationPredicate.Builder.inDimension(Level.END))
            ))
            .tags(BingoTags.ACTION, BingoTags.END, BingoTags.VILLAGE, BingoTags.COMBAT)
            .reactant("pacifist")
            .name(Component.translatable("bingo.goal.villager_in_end", EntityType.VILLAGER.getDescription()))
            .icon(Blocks.CAULDRON)
        );
        addGoal(obtainItemGoal(id("dragon_breath"), items, Items.DRAGON_BREATH, 5, 16)
            .tags(BingoTags.COMBAT, BingoTags.END));
        addGoal(obtainItemGoal(id("dragon_egg"), items, Items.DRAGON_EGG)
            .tags(BingoTags.COMBAT, BingoTags.END));
        addGoal(BingoGoal.builder(id("complete_full_size_map"))
            .criterion("complete", CompleteMapTrigger.TriggerInstance.completeMap(
                MinMaxBounds.Ints.atLeast(MapItemSavedData.MAX_SCALE)
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .name("complete_full_size_map")
            .icon(Items.FILLED_MAP)
            .antisynergy("complete_map"));
        addGoal(BingoGoal.builder(id("die_to_villager"))
            .criterion("die", EntityKilledPlayerTrigger.builder()
                .creditedEntity(EntityPredicate.Builder.entity().of(entityTypes, EntityType.VILLAGER).build())
                .build()
            )
            .tags(BingoTags.VILLAGE, BingoTags.OVERWORLD, BingoTags.STAT, BingoTags.COMBAT)
            .name(Component.translatable("bingo.goal.die_to_villager", EntityType.VILLAGER.getDescription()))
            .tooltip("die_to_villager")
            .icon(EntityIcon.ofSpawnEgg(EntityType.VILLAGER))
        );
        addGoal(BingoGoal.builder(id("pop_totem"))
            .criterion("totem", UsedTotemTrigger.TriggerInstance.usedTotem(items, Items.TOTEM_OF_UNDYING))
            .name("pop_totem")
            .icon(Items.TOTEM_OF_UNDYING)
            .tags(BingoTags.ITEM, BingoTags.OVERWORLD));
        addGoal(obtainAllItemsFromTag(ItemTags.SWORDS, "swords")
            .tags(BingoTags.NETHER)
            .tooltip("all_somethings.tools")
        );
        addGoal(obtainAllItemsFromTag(ItemTags.PICKAXES, "pickaxes")
            .tags(BingoTags.NETHER)
            .tooltip("all_somethings.tools")
        );
        addGoal(BingoGoal.builder(id("pacifist"))
            .criterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity())
            .tags(BingoTags.NEVER, BingoTags.STAT)
            .name("pacifist")
            .tooltip("pacifist")
            .icon(Items.DIAMOND_SWORD)
            .catalyst("pacifist"));
        addGoal(BingoGoal.builder(id("scaffolding_tower"))
            .criterion("destroy", BreakBlockTrigger.builder()
                .location(
                    new LocationCheck(
                        Optional.of(LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.atMost(-58)).build()),
                        BlockPos.ZERO
                    ),
                    new PillarCondition(379, Optional.of(BlockPredicate.Builder.block().of(blocks, Blocks.SCAFFOLDING).build()))
                )
                .build()
            )
            .tags(BingoTags.BUILD, BingoTags.OVERWORLD, BingoTags.FINISH)
            .name("scaffolding_tower")
            .tooltip("scaffolding_tower")
            .icon(makeItemWithGlint(Blocks.SCAFFOLDING))
        );
        addGoal(BingoGoal.builder(id("feed_panda_cake"))
            .criterion("feed", CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.createCriterion(
                new PickedUpItemTrigger.TriggerInstance(
                    Optional.empty(),
                    Optional.of(ItemPredicate.Builder.item().of(items, Blocks.CAKE).build()),
                    Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity().of(entityTypes, EntityType.PANDA).build()))
                )
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.RARE_BIOME)
            .name(Component.translatable(
                "bingo.goal.feed_panda_cake",
                EntityType.PANDA.getDescription(), Blocks.CAKE.getName()
            ))
            .icon(EntityIcon.ofSpawnEgg(EntityType.PANDA))
        );
        addGoal(BingoGoal.builder(id("breed_pandas"))
            .criterion("breed", BredAnimalsTrigger.TriggerInstance.bredAnimals(
                EntityPredicate.Builder.entity().of(entityTypes, EntityType.PANDA)
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.RARE_BIOME)
            .name("breed_pandas")
            .icon(Items.BAMBOO)
        );
        addGoal(BingoGoal.builder(id("disarm_pillager"))
            .criterion("break", EntityTrigger.TriggerInstance.brokeCrossbow(
                EntityPredicate.Builder.entity().of(entityTypes, EntityType.PILLAGER).build()
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.COMBAT, BingoTags.RARE_BIOME)
            .name(Component.translatable(
                "bingo.goal.disarm_pillager",
                EntityType.PILLAGER.getDescription()
            ))
            .icon(Items.CROSSBOW)
        );
        addGoal(BingoGoal.builder(id("stun_ravager"))
            .criterion("stun", EntityTrigger.TriggerInstance.stunnedRavager())
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.COMBAT, BingoTags.VILLAGE)
            .name(Component.translatable(
                "bingo.goal.stun_ravager",
                EntityType.RAVAGER.getDescription()
            ))
            .icon(EntityIcon.ofSpawnEgg(EntityType.RAVAGER))
        );
        addGoal(BingoGoal.builder(id("hero_of_the_village"))
            .criterion("raid_won", PlayerTrigger.TriggerInstance.raidWon())
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.COMBAT, BingoTags.VILLAGE)
            .reactant("pacifist")
            .name(Component.translatable("advancements.adventure.hero_of_the_village.title"))
            .icon(Raid.getOminousBannerInstance(bannerPatterns))
        );
        addGoal(BingoGoal.builder(id("ocelot_trust"))
            .criterion("trust", TameAnimalTrigger.TriggerInstance.tamedAnimal(
                EntityPredicate.Builder.entity().of(entityTypes, EntityType.OCELOT)
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.RARE_BIOME)
            .name(Component.translatable(
                "bingo.goal.ocelot_trust",
                EntityType.OCELOT.getDescription()
            ))
            .icon(Items.SALMON)
        );
        addGoal(obtainItemGoal(id("ender_eye"), items, Items.ENDER_EYE, 12, 12)
            .reactant("pacifist")
            .tags(BingoTags.NETHER, BingoTags.COMBAT)
            .tooltip(Component.translatable("bingo.goal.ender_eye.hard.tooltip")));
        addGoal(obtainItemGoal(id("netherite_ingot"), items, Items.NETHERITE_INGOT)
            .tags(BingoTags.NETHER));
        addGoal(obtainItemGoal(id("wither_skeleton_skull"), items, Items.WITHER_SKELETON_SKULL)
            .reactant("pacifist")
            .tags(BingoTags.NETHER, BingoTags.COMBAT, BingoTags.RARE_BIOME));
        addGoal(obtainItemGoal(id("gilded_blackstone"), items, Items.GILDED_BLACKSTONE)
            .tags(BingoTags.NETHER, BingoTags.RARE_BIOME));
        addGoal(BingoGoal.builder(id("use_lodestone"))
            .criterion("use", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                LocationPredicate.Builder.location().setBlock(
                    BlockPredicate.Builder.block().of(blocks, Blocks.LODESTONE)
                ),
                ItemPredicate.Builder.item().of(items, Items.COMPASS)
            ))
            .tags(BingoTags.ACTION, BingoTags.NETHER)
            .name(Component.translatable(
                "bingo.goal.use_lodestone",
                Items.COMPASS.getName(), Blocks.LODESTONE.getName()
            ))
            .icon(Blocks.LODESTONE)
        );
        addGoal(BingoGoal.builder(id("piglin_brute_axe"))
            .criterion("give", CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.createCriterion(
                new PickedUpItemTrigger.TriggerInstance(
                    Optional.empty(),
                    Optional.of(ItemPredicate.Builder.item()
                        .of(items, ItemTags.AXES)
                        .withComponents(DataComponentMatchers.Builder.components()
                            .partial(DataComponentPredicates.ENCHANTMENTS, createAnyEnchantmentsRequirement())
                            .build()
                        )
                        .build()
                    ),
                    Optional.of(EntityPredicate.wrap(EntityPredicate.Builder.entity()
                        .of(entityTypes, EntityType.PIGLIN_BRUTE)
                        .build()
                    ))
                )
            ))
            .tags(BingoTags.ACTION, BingoTags.NETHER, BingoTags.COMBAT)
            .name(Component.translatable("bingo.goal.piglin_brute_axe", EntityType.PIGLIN_BRUTE.getDescription()))
            .icon(makeItemWithGlint(Items.GOLDEN_AXE))
        );
        addGoal(BingoGoal.builder(id("6x6scaffolding"))
            .criterion("obtain", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SCAFFOLDING),
                BlockPatternCondition.builder().aisle(
                    "########",
                    "#      #",
                    "#      #",
                    "#      #",
                    "#      #",
                    "#      #",
                    "#      #"
                ).where('#', BlockPredicate.Builder.block().of(blocks, Blocks.SCAFFOLDING))))
            .tags(BingoTags.OVERWORLD, BingoTags.BUILD)
            .name("6x6scaffolding")
            .tooltip("6x6scaffolding")
            .tooltipIcon(ResourceLocations.bingo("textures/gui/tooltips/6x6scaffolding.png"))
            .icon(Items.SCAFFOLDING));
        addGoal(obtainItemGoal(id("honey_block"), items, Items.HONEY_BLOCK, 2, 5)
            .setAntisynergy("honey")
            .infrequency(2)
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("honeycomb_block"), items, Items.HONEYCOMB_BLOCK, 6, 15)
            .setAntisynergy("honeycomb")
            .infrequency(2)
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("kill_wandering_trader"))
            .criterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityTypes, EntityType.WANDERING_TRADER)))
            .name("kill_wandering_trader")
            .icon(EntityIcon.ofSpawnEgg(EntityType.WANDERING_TRADER))
            .reactant("pacifist")
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.COMBAT));
        addGoal(BingoGoal.builder(id("cure_zombie_villager"))
            .criterion("cure", CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager())
            .name("cure_zombie_villager")
            .icon(Items.GOLDEN_APPLE)
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("burn_mending_book"))
            .criterion("obtain", KillItemTrigger.builder()
                .item(ItemPredicate.Builder.item()
                    .of(items, Items.ENCHANTED_BOOK)
                    .withComponents(DataComponentMatchers.Builder.components()
                        .partial(
                            DataComponentPredicates.STORED_ENCHANTMENTS,
                            EnchantmentsPredicate.storedEnchantments(List.of(
                                new EnchantmentPredicate(enchantments.getOrThrow(Enchantments.MENDING), MinMaxBounds.Ints.ANY)
                            ))
                        )
                        .build()
                    )
                    .build()
                )
                .damage(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FIRE))).build())
                .build()
            )
            .name("burn_mending_book")
            .icon(Blocks.FIRE, Items.ENCHANTED_BOOK)
            .tags(BingoTags.ACTION));
        // TODO: never smelt with furnaces
        addGoal(BingoGoal.builder(id("huge_fungus_in_overworld"))
            .criterion("grow", GrowFeatureTrigger.builder()
                .feature(BingoFeatureTags.HUGE_FUNGI)
                .location(LocationPredicate.Builder.inDimension(Level.OVERWORLD).build())
                .build()
            )
            .name("huge_fungus_in_overworld")
            .icon(Items.WARPED_FUNGUS)
            .antisynergy("grow_fungus")
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.NETHER));
        addGoal(BingoGoal.builder(id("dirt_netherrack_end_stone"))
            .sub("count", BingoSub.random(32, 64))
            .criterion("items", TotalCountInventoryChangeTrigger.builder()
                .items(
                    ItemPredicate.Builder.item().of(items, Blocks.DIRT).withCount(MinMaxBounds.Ints.atLeast(0)).build(),
                    ItemPredicate.Builder.item().of(items, Blocks.NETHERRACK).withCount(MinMaxBounds.Ints.atLeast(0)).build(),
                    ItemPredicate.Builder.item().of(items, Blocks.END_STONE).withCount(MinMaxBounds.Ints.atLeast(0)).build()
                )
                .build(),
                subber -> subber.sub("conditions.items.*.count.min", "count")
            )
            .progress("items")
            .tags(BingoTags.ITEM, BingoTags.OVERWORLD, BingoTags.NETHER, BingoTags.END)
            .name(
                Component.translatable(
                    "bingo.goal.dirt_netherrack_end_stone",
                    0, Blocks.DIRT.getName(), Blocks.NETHERRACK.getName(), Blocks.END_STONE.getName()
                ),
                subber -> subber.sub("with.0", "count")
            )
            .icon(new CycleIcon(
                BlockIcon.ofBlock(Blocks.DIRT),
                BlockIcon.ofBlock(Blocks.NETHERRACK),
                BlockIcon.ofBlock(Blocks.END_STONE)
            ), subber -> subber.sub("icons.*.item.count", "count"))
        );
        addGoal(BingoGoal.builder(id("tame_mule"))
            .criterion("obtain", TameAnimalTrigger.TriggerInstance.tamedAnimal(
                EntityPredicate.Builder.entity().of(entityTypes, EntityType.MULE)
            ))
            .name("tame_mule")
            .icon(EntityIcon.ofSpawnEgg(EntityType.MULE))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
        );
        addGoal(BingoGoal.builder(id("carrot_stick_to_rod"))
            .criterion("break", ItemDurabilityTrigger.TriggerInstance.changedDurability(
                Optional.of(ItemPredicate.Builder.item().of(items, Items.CARROT_ON_A_STICK).build()),
                MinMaxBounds.Ints.atMost(0)
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .name(Component.translatable(
                "bingo.goal.carrot_stick_to_rod",
                Items.CARROT_ON_A_STICK.getName(),
                Items.FISHING_ROD.getName()
            ))
            .icon(Items.CARROT_ON_A_STICK));
        addGoal(obtainItemGoal(id("skull_banner_pattern"), items, Items.SKULL_BANNER_PATTERN)
            .tags(BingoTags.NETHER, BingoTags.COMBAT, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name(Component.translatable(
                "bingo.goal.skull_banner_pattern",
                Component.translatable("item.minecraft.skull_banner_pattern.desc"),
                Component.translatable("item.minecraft.skull_banner_pattern")
            ))
            .icon(makeBannerWithPattern(Items.WHITE_BANNER, bannerPatterns.getOrThrow(BannerPatterns.SKULL), DyeColor.BLACK)));
        addGoal(obtainItemGoal(id("turtle_helmet"), items, Items.TURTLE_HELMET)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("sniffer_egg"), items, Items.SNIFFER_EGG)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("armor_trims"), BingoItemTags.TRIM_TEMPLATES, "bingo.goal.armor_trims", 3, 3)
            .antisynergy("armor_trims"));
        addGoal(obtainSomeItemsFromTag(id("bonemealable_blocks"), BingoItemTags.BONEMEALABLE, "bingo.goal.bonemealable", 15, 25)
            .antisynergy("bonemealable"));
        addGoal(obtainSomeItemsFromTag(id("food"), ConventionalItemTags.FOODS, "bingo.goal.food", 15, 20)
            .antisynergy("food"));
        addGoal(obtainItemGoal(id("mace"), items, Items.MACE)
            .tags(BingoTags.OVERWORLD));
    }
}
