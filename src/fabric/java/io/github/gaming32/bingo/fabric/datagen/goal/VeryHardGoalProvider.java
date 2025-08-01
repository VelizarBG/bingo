package io.github.gaming32.bingo.fabric.datagen.goal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import io.github.gaming32.bingo.conditions.HasOnlyBeenDamagedByCondition;
import io.github.gaming32.bingo.data.BingoDifficulties;
import io.github.gaming32.bingo.data.BingoTags;
import io.github.gaming32.bingo.data.goal.BingoGoal;
import io.github.gaming32.bingo.data.goal.GoalBuilder;
import io.github.gaming32.bingo.data.icons.CycleIcon;
import io.github.gaming32.bingo.data.icons.EntityIcon;
import io.github.gaming32.bingo.data.icons.InstrumentCycleIcon;
import io.github.gaming32.bingo.data.icons.ItemIcon;
import io.github.gaming32.bingo.data.icons.ItemTagCycleIcon;
import io.github.gaming32.bingo.data.progresstrackers.AchievedRequirementsProgressTracker;
import io.github.gaming32.bingo.data.subs.BingoSub;
import io.github.gaming32.bingo.data.tags.bingo.BingoBlockTags;
import io.github.gaming32.bingo.data.tags.bingo.BingoItemTags;
import io.github.gaming32.bingo.triggers.BeaconEffectTrigger;
import io.github.gaming32.bingo.triggers.CompleteMapTrigger;
import io.github.gaming32.bingo.triggers.DifferentPotionsTrigger;
import io.github.gaming32.bingo.triggers.EntityDieNearPlayerTrigger;
import io.github.gaming32.bingo.triggers.ExperienceChangeTrigger;
import io.github.gaming32.bingo.triggers.ItemPickedUpTrigger;
import io.github.gaming32.bingo.triggers.PartyParrotsTrigger;
import io.github.gaming32.bingo.triggers.PowerConduitTrigger;
import io.github.gaming32.bingo.triggers.ZombifyPigTrigger;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;

public class VeryHardGoalProvider extends DifficultyGoalProvider {
    public VeryHardGoalProvider(BiConsumer<ResourceLocation, BingoGoal> goalAdder, HolderLookup.Provider registries) {
        super(BingoDifficulties.VERY_HARD, goalAdder, registries);
    }

    @Override
    public void addGoals() {
        final var structures = registries.lookupOrThrow(Registries.STRUCTURE);
        final var items = registries.lookupOrThrow(Registries.ITEM);
        final var entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        final var potions = registries.lookupOrThrow(Registries.POTION);

        addGoal(obtainSomeItemsFromTag(id("ores"), ConventionalItemTags.ORES, "bingo.goal.ores", 5, 7)
            .tooltip("ores")
            .tags(BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("different_potions"))
            .sub("count", BingoSub.random(12, 15))
            .criterion("potions",
                DifferentPotionsTrigger.TriggerInstance.differentPotions(1),
                subber -> subber.sub("conditions.min_count", "count")
            )
            .progress("potions")
            .tags(BingoTags.ITEM, BingoTags.NETHER, BingoTags.COMBAT, BingoTags.OVERWORLD)
            .reactant("pacifist")
            .name(
                Component.translatable("bingo.goal.different_potions", 0),
                subber -> subber.sub("with.0", "count")
            )
            .tooltip("different_potions")
            .icon(
                createPotionsIcon(potions, Items.POTION),
                subber -> subber.sub("icons.*.item.count", "count")
            )
        );
        addGoal(obtainAllItemsFromTag(ItemTags.CHEST_ARMOR, "chestplates")
            .tags(BingoTags.NETHER)
            .tooltip("all_somethings.armor")
        );
        addGoal(obtainItemGoal(
            id("any_head"),
            items,
            new ItemStack(Items.ZOMBIE_HEAD),
            ItemPredicate.Builder.item().of(
                items,
                Items.SKELETON_SKULL,
                Items.PLAYER_HEAD,
                Items.ZOMBIE_HEAD,
                Items.CREEPER_HEAD,
                Items.DRAGON_HEAD,
                Items.PIGLIN_HEAD
            ))
            .tags(BingoTags.COMBAT, BingoTags.OVERWORLD)
            .name("any_head")
            .tooltip("any_head"));

        addGoal(BingoGoal.builder(id("all_dyes"))
            .criterion("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(
                Arrays.stream(DyeColor.values()).map(DyeItem::byColor).toArray(ItemLike[]::new)))
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD)
            .name("all_dyes")
            .tooltip(Component.translatable(
                "bingo.sixteen_bang",
                Arrays.stream(DyeColor.values())
                    .map(color -> Component.translatable("color.minecraft." + color.getName()))
                    .toArray(Object[]::new)
            ))
            .icon(new CycleIcon(
                Arrays.stream(DyeColor.values())
                    .map(DyeItem::byColor)
                    .map(i -> new ItemStack(i, 16))
                    .map(ItemIcon::new)
                    .collect(ImmutableList.toImmutableList())
            ))
            .antisynergy("every_color")
            .reactant("use_furnace")
        );
        addGoal(BingoGoal.builder(id("levels"))
            .criterion("obtain", ExperienceChangeTrigger.builder().levels(MinMaxBounds.Ints.atLeast(50)).build())
            .tags(BingoTags.STAT)
            .name(Component.translatable("bingo.goal.levels", 50))
            .icon(new ItemStack(Items.EXPERIENCE_BOTTLE, 50))
            .infrequency(2)
            .antisynergy("levels"));
        addGoal(obtainItemGoal(id("tipped_arrow"), items, Items.TIPPED_ARROW, 16, 32)
            .tags(BingoTags.NETHER, BingoTags.OVERWORLD)
            .icon(
                createPotionsIcon(potions, Items.TIPPED_ARROW),
                subber -> subber.sub("icons.*.item.count", "count")
            )
        );
        addGoal(mineralPillarGoal(id("all_mineral_blocks"), BingoBlockTags.ALL_MINERAL_BLOCKS)
            .name("all_mineral_blocks")
            .tooltip("all_mineral_blocks")
            .tags(BingoTags.OVERWORLD, BingoTags.NETHER)
            .icon(new ItemTagCycleIcon(BingoItemTags.ALL_MINERAL_BLOCKS))
        );
        addGoal(BingoGoal.builder(id("sleep_in_mansion"))
            .criterion("sleep", CriteriaTriggers.SLEPT_IN_BED.createCriterion(
                new PlayerTrigger.TriggerInstance(
                    Optional.of(EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .located(LocationPredicate.Builder.inStructure(structures.getOrThrow(BuiltinStructures.WOODLAND_MANSION)))
                            .build()
                    ))
                )
            ))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name("sleep_in_mansion")
            .icon(Items.BROWN_BED));
        addGoal(obtainItemGoal(id("mycelium"), items, Items.MYCELIUM, 10, 32)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("coral_blocks"))
            .criterion("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(
                Items.TUBE_CORAL_BLOCK, Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK,
                Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK
            ))
            .tags(BingoTags.ITEM, BingoTags.RARE_BIOME, BingoTags.OCEAN, BingoTags.OVERWORLD)
            .name("coral_blocks")
            .icon(new CycleIcon(
                ItemIcon.ofItem(Items.TUBE_CORAL_BLOCK),
                ItemIcon.ofItem(Items.BRAIN_CORAL_BLOCK),
                ItemIcon.ofItem(Items.BUBBLE_CORAL_BLOCK),
                ItemIcon.ofItem(Items.FIRE_CORAL_BLOCK),
                ItemIcon.ofItem(Items.HORN_CORAL_BLOCK)
            ))
        );
        addGoal(obtainItemGoal(id("blue_ice"), items, Items.BLUE_ICE, 32, 64)
            .tags(BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("full_power_conduit"))
            .criterion("power", PowerConduitTrigger.TriggerInstance.powerConduit(MinMaxBounds.Ints.exactly(6)))
            .tags(BingoTags.BUILD, BingoTags.OCEAN, BingoTags.OVERWORLD)
            .name("full_power_conduit")
            .icon(Items.CONDUIT));
        addGoal(BingoGoal.builder(id("all_diamond_craftables"))
            .criterion("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(
                Items.DIAMOND_BLOCK, Items.DIAMOND_AXE, Items.DIAMOND_BOOTS,
                Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_HOE,
                Items.DIAMOND_LEGGINGS, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL,
                Items.DIAMOND_SWORD, Items.ENCHANTING_TABLE, Items.FIREWORK_STAR, Items.JUKEBOX
            ))
            .name("all_diamond_craftables")
            .tooltip("all_diamond_craftables")
            .icon(new CycleIcon(
                ItemIcon.ofItem(Items.DIAMOND_BLOCK),
                ItemIcon.ofItem(Items.DIAMOND_AXE),
                ItemIcon.ofItem(Items.DIAMOND_BOOTS),
                ItemIcon.ofItem(Items.DIAMOND_CHESTPLATE),
                ItemIcon.ofItem(Items.DIAMOND_HELMET),
                ItemIcon.ofItem(Items.DIAMOND_HOE),
                ItemIcon.ofItem(Items.DIAMOND_LEGGINGS),
                ItemIcon.ofItem(Items.DIAMOND_PICKAXE),
                ItemIcon.ofItem(Items.DIAMOND_SHOVEL),
                ItemIcon.ofItem(Items.DIAMOND_SWORD),
                ItemIcon.ofItem(Items.ENCHANTING_TABLE),
                ItemIcon.ofItem(Items.FIREWORK_STAR),
                ItemIcon.ofItem(Items.JUKEBOX)
            ))
            .antisynergy("diamond_items")
        );
        addGoal(BingoGoal.builder(id("shulker_in_overworld"))
            .criterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity(
                EntityPredicate.Builder.entity()
                    .of(entityTypes, EntityType.SHULKER)
                    .located(LocationPredicate.Builder.inDimension(Level.OVERWORLD))
            ))
            .tags(BingoTags.ACTION, BingoTags.COMBAT, BingoTags.END, BingoTags.OVERWORLD)
            .name("shulker_in_overworld")
            .icon(Items.SHULKER_SHELL)
            .reactant("pacifist"));
        addGoal(obtainItemGoal(id("diamond_block"), items, Items.DIAMOND_BLOCK, 5, 10)
            .infrequency(2));
        addGoal(BingoGoal.builder(id("complete_full_size_end_map"))
            .criterion("complete", CompleteMapTrigger.TriggerInstance.completeMap(
                MinMaxBounds.Ints.atLeast(MapItemSavedData.MAX_SCALE),
                LocationPredicate.Builder.inDimension(Level.END).build()
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.END)
            .name("complete_full_size_end_map")
            .icon(Items.FILLED_MAP)
            .antisynergy("complete_map"));
        addGoal(obtainItemGoal(id("wither_rose"), items, Items.WITHER_ROSE, 32, 64)
            .reactant("pacifist")
            .tags(BingoTags.NETHER, BingoTags.COMBAT));
        addGoal(BingoGoal.builder(id("panda_slime_ball"))
            // Currently untested. They have a 1/175,000 or a 1/2,100,000 chance to drop one on a tick.
            .criterion("pickup", ItemPickedUpTrigger.TriggerInstance.pickedUpFrom(
                ItemPredicate.Builder.item().of(items, Items.SLIME_BALL).build(),
                EntityPredicate.Builder.entity().of(entityTypes, EntityType.PANDA).build()
            ))
            .tags(BingoTags.ITEM, BingoTags.OVERWORLD, BingoTags.RARE_BIOME)
            .name("panda_slime_ball")
            .icon(Items.SLIME_BALL));
        addGoal(obtainItemGoal(id("netherite_block"), items, Items.NETHERITE_BLOCK, 2, 2)
            .tags(BingoTags.NETHER));
        addGoal(BingoGoal.builder(id("full_netherite_armor_and_tools"))
            .criterion("obtain", InventoryChangeTrigger.TriggerInstance.hasItems(
                Items.NETHERITE_BOOTS, Items.NETHERITE_LEGGINGS, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET,
                Items.NETHERITE_SWORD, Items.NETHERITE_SHOVEL, Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_HOE
            ))
            .tags(BingoTags.ITEM, BingoTags.NETHER)
            .name("full_netherite_armor_and_tools")
            .icon(new CycleIcon(
                ItemIcon.ofItem(Items.NETHERITE_BOOTS),
                ItemIcon.ofItem(Items.NETHERITE_LEGGINGS),
                ItemIcon.ofItem(Items.NETHERITE_CHESTPLATE),
                ItemIcon.ofItem(Items.NETHERITE_HELMET),
                ItemIcon.ofItem(Items.NETHERITE_SWORD),
                ItemIcon.ofItem(Items.NETHERITE_SHOVEL),
                ItemIcon.ofItem(Items.NETHERITE_PICKAXE),
                ItemIcon.ofItem(Items.NETHERITE_AXE),
                ItemIcon.ofItem(Items.NETHERITE_HOE)
            ))
        );
        addGoal(BingoGoal.builder(id("zombify_pig"))
            .criterion("channeling", ZombifyPigTrigger.zombifyPig()
                .direct(true)
                .build()
            )
            .criterion("nearby", ZombifyPigTrigger.zombifyPig()
                .pig(EntityPredicate.Builder.entity()
                    .distance(DistancePredicate.absolute(MinMaxBounds.Doubles.atMost(16)))
                    .build()
                )
                .direct(false)
                .build()
            )
            .requirements(AdvancementRequirements.Strategy.OR)
            .name("zombify_pig")
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .icon(Items.COOKED_PORKCHOP));
        addGoal(obtainItemGoal(id("trident"), items, Items.TRIDENT)
            .tags(BingoTags.OCEAN, BingoTags.COMBAT, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("party_parrots"))
            .criterion("party", PartyParrotsTrigger.TriggerInstance.partyParrots())
            .name("party_parrots")
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD, BingoTags.RARE_BIOME)
            .icon(Items.MUSIC_DISC_CAT));
        addGoal(bedRowGoal(id("bed_row"), 16, 16)
            .reactant("use_furnace")
            .antisynergy("every_color")
            .infrequency(2)
            .tags(BingoTags.ACTION)
            .tooltip(Component.translatable(
                "bingo.sixteen_bang",
                Arrays.stream(DyeColor.values())
                    .map(color -> Component.translatable("color.minecraft." + color.getName()))
                    .toArray(Object[]::new)
            ))
        );
        addGoal(BingoGoal.builder(id("kill_enderman_with_endermites"))
            .criterion("kill", EntityDieNearPlayerTrigger.builder()
                .entity(ContextAwarePredicate.create(
                    LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(entityTypes, EntityType.ENDERMAN)).build(),
                    HasOnlyBeenDamagedByCondition.builder().entityType(EntityType.ENDERMITE).build()
                ))
                .killingBlow(DamagePredicate.Builder.damageInstance().sourceEntity(EntityPredicate.Builder.entity().of(entityTypes, EntityType.ENDERMITE).build()).build())
                .build()
            )
            .name("kill_enderman_with_endermites")
            .tooltip("kill_enderman_with_endermites")
            .icon(EntityIcon.ofSpawnEgg(EntityType.ENDERMITE))
            .tags(BingoTags.ACTION, BingoTags.COMBAT, BingoTags.END));
        addGoal(BingoGoal.builder(id("beacon_regen"))
            .criterion("effect", BeaconEffectTrigger.TriggerInstance.effectApplied(MobEffects.REGENERATION))
            .tags(BingoTags.ITEM, BingoTags.NETHER, BingoTags.OVERWORLD, BingoTags.COMBAT)
            .name("beacon_regen")
            .icon(Blocks.BEACON)
            .reactant("pacifist"));
        addGoal(obtainSomeItemsFromTag(id("armor_trims"), BingoItemTags.TRIM_TEMPLATES, "bingo.goal.armor_trims", 5, 5)
            .antisynergy("armor_trims"));
        addGoal(obtainAllGoatHorns());
        addGoal(obtainAllItemsFromTag(BingoItemTags.CLIMBABLE, "climbables")
            .tags(BingoTags.NETHER, BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
    }

    @SuppressWarnings("deprecation")
    private GoalBuilder obtainAllGoatHorns() {
        final var items = registries.lookupOrThrow(Registries.ITEM);

        final var sortedInstruments = registries.lookupOrThrow(Registries.INSTRUMENT)
            .listElements()
            .collect(ImmutableSortedSet.toImmutableSortedSet(
                Comparator.comparing(e -> e.key().location())
            ));

        final GoalBuilder builder = BingoGoal.builder(id("all_goat_horns"));
        for (final var instrument : sortedInstruments) {
            final var location = instrument.key().location();
            builder.criterion(
                "obtain_" + location.getNamespace() + "_" + location.getPath(),
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item()
                        .of(items, Items.GOAT_HORN)
                        .withComponents(DataComponentMatchers.Builder.components()
                            .exact(DataComponentExactPredicate.builder()
                                .expect(DataComponents.INSTRUMENT, new InstrumentComponent(instrument))
                                .build()
                            )
                            .build()
                        )
                        .build()
                )
            );
        }

        return builder
            .tags(BingoTags.ITEM, BingoTags.OVERWORLD)
            .name("all_goat_horns")
            .tooltip(ComponentUtils.formatList(
                sortedInstruments,
                ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR,
                holder -> holder.value().description()
            ))
            .progress(AchievedRequirementsProgressTracker.INSTANCE)
            .icon(new InstrumentCycleIcon(Items.GOAT_HORN.builtInRegistryHolder()))
            .antisynergy("goat_horn");
    }
}
