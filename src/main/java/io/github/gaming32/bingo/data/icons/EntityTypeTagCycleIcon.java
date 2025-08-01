package io.github.gaming32.bingo.data.icons;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public record EntityTypeTagCycleIcon(
    TagKey<EntityType<?>> tag, Optional<Holder<Item>> baseItem, int count
) implements GoalIcon.WithoutContext {
    public static final MapCodec<EntityTypeTagCycleIcon> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            TagKey.codec(Registries.ENTITY_TYPE).fieldOf("tag").forGetter(EntityTypeTagCycleIcon::tag),
            Item.CODEC.optionalFieldOf("base_item").forGetter(EntityTypeTagCycleIcon::baseItem),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(EntityTypeTagCycleIcon::count)
        ).apply(instance, EntityTypeTagCycleIcon::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeTagCycleIcon> STREAM_CODEC = StreamCodec.composite(
        TagKey.streamCodec(Registries.ENTITY_TYPE), EntityTypeTagCycleIcon::tag,
        ByteBufCodecs.holderRegistry(Registries.ITEM).apply(ByteBufCodecs::optional), EntityTypeTagCycleIcon::baseItem,
        ByteBufCodecs.VAR_INT, EntityTypeTagCycleIcon::count,
        EntityTypeTagCycleIcon::new
    );

    public EntityTypeTagCycleIcon {
        Preconditions.checkArgument(count > 0, "count must be positive");
    }

    public EntityTypeTagCycleIcon(TagKey<EntityType<?>> tag, Holder<Item> baseItem, int count) {
        this(tag, Optional.of(baseItem), count);
    }

    public EntityTypeTagCycleIcon(TagKey<EntityType<?>> tag, int count) {
        this(tag, Optional.empty(), count);
    }

    public EntityTypeTagCycleIcon(TagKey<EntityType<?>> tag, Holder<Item> baseItem) {
        this(tag, Optional.of(baseItem), 1);
    }

    public EntityTypeTagCycleIcon(TagKey<EntityType<?>> tag) {
        this(tag, Optional.empty(), 1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getFallback() {
        return new ItemStack(
            baseItem.orElseGet(() ->
                StreamSupport.stream(BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(tag).spliterator(), false)
                    .map(holder -> SpawnEggItem.byId(holder.value()))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(Item::builtInRegistryHolder)
                    .orElseGet(Items.AIR::builtInRegistryHolder)
            ),
            count
        );
    }

    @Override
    public GoalIconType<?> type() {
        return GoalIconType.ENTITY_TYPE_TAG_CYCLE.get();
    }
}
