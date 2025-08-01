package io.github.gaming32.bingo.game.persistence;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;

public class PersistenceTypes {
    // DFU crashes if we have no recursive types
    public static final DSL.TypeReference HACK_UNUSED_RECURSIVE = reference("hack_unused_recursive");

    public static final DSL.TypeReference ITEM_TAG = reference("item_tag");
    public static final DSL.TypeReference ITEM_TAG_OR_REFERENCE = reference("item_tag_or_reference");
    public static final DSL.TypeReference ITEM_TAG_PREDICATE = reference("item_tag_predicate");
    public static final DSL.TypeReference HOMOGENEOUS_ITEM = reference("homogeneous_item");

    public static final DSL.TypeReference CRITERION = reference("criterion");
    public static final DSL.TypeReference ICON = reference("icon");
    public static final DSL.TypeReference ACTIVE_GOAL = reference("active_goal");
    public static final DSL.TypeReference BOARD = reference("board");
    public static final DSL.TypeReference GAME = reference("game");

    public static DSL.TypeReference reference(String name) {
        return References.reference("bingo:" + name);
    }
}
