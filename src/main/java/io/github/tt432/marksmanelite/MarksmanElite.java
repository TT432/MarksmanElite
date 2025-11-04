package io.github.tt432.marksmanelite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.tt432.marksmanelite.accessory.*;
import io.github.tt432.marksmanelite.guns.BulletEntity;
import io.github.tt432.marksmanelite.guns.GunDataComponent;
import io.github.tt432.marksmanelite.player.PlayerGunState;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

@Mod(MarksmanElite.MODID)
public class MarksmanElite {
    public static final String MODID = "marksman_elite";

    public MarksmanElite(IEventBus eventBus) {
        ACCESSORY_REG.register(eventBus);
        ENTITY_TYPE_REG.register(eventBus);
        ITEM_REG.register(eventBus);
        ATTACHMENT_REG.register(eventBus);
        COMPONENT_REG.register(eventBus);
        ATTRIBUTE_REG.register(eventBus);
    }

    public static ResourceLocation byPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(MarksmanElite.MODID, path);
    }

    private static final DeferredRegister<MapCodec<? extends BasicAccessory<?>>> ACCESSORY_REG =
            DeferredRegister.create(NewRegistry.ACCESSORY_TYPE, MarksmanElite.MODID);

    public static final DeferredHolder<MapCodec<? extends BasicAccessory<?>>, MapCodec<ScopeAccessory>> SCOPE_ACCESSORY =
            ACCESSORY_REG.register("scope", () -> ScopeAccessory.CODEC);

    public static final DeferredHolder<MapCodec<? extends BasicAccessory<?>>, MapCodec<MuzzleAccessory>> MUZZLE_ACCESSORY =
            ACCESSORY_REG.register("muzzle", () -> MuzzleAccessory.CODEC);

    public static final DeferredHolder<MapCodec<? extends BasicAccessory<?>>, MapCodec<LowerHangingAccessory>> LOWER_HANGING_ACCESSORY =
            ACCESSORY_REG.register("lower_hanging", () -> LowerHangingAccessory.CODEC);

    public static final DeferredHolder<MapCodec<? extends BasicAccessory<?>>, MapCodec<MagazineAccessory>> MAGAZINE_ACCESSORY =
            ACCESSORY_REG.register("magazine", () -> MagazineAccessory.CODEC);


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REG = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MarksmanElite.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BULLET_ENTITY =
            ENTITY_TYPE_REG.register("bullet", () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("bullet"));


    public static final DeferredRegister.Items ITEM_REG = DeferredRegister.createItems(MarksmanElite.MODID);

//    public static final DeferredItem<AbstractGun> TEST_GUN = ITEM_REG.register("test_gun", AbstractGun::new);


    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_REG = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MarksmanElite.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerGunState>> PLAYER_GUN_STATE_ATTACHMENT =
            ATTACHMENT_REG.register("player_gun_state", () -> AttachmentType.<PlayerGunState>builder(PlayerGunState::new).build());


    public static final DeferredRegister.DataComponents COMPONENT_REG = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MarksmanElite.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunDataComponent>> GUN_DATA_COMPONENT =
            COMPONENT_REG.register("gun_data", () -> DataComponentType.<GunDataComponent>builder().persistent(GunDataComponent.CODEC).networkSynchronized(GunDataComponent.STREAM_CODEC).build());


    public static final DeferredRegister<Attribute> ATTRIBUTE_REG = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MarksmanElite.MODID);

    public static final DeferredHolder<Attribute, PercentageAttribute> MANEUVERABILITY =
            ATTRIBUTE_REG.register("maneuverability", () -> new PercentageAttribute("marksman_elite.attribute.maneuverability.description", 0, 0, 100));

    public static final DeferredHolder<Attribute, PercentageAttribute> STABILITY =
            ATTRIBUTE_REG.register("stability", () -> new PercentageAttribute("marksman_elite.attribute.stability.description", 0, 0, 100));

    public static final DeferredHolder<Attribute, RangedAttribute> MAX_AMMO =
            ATTRIBUTE_REG.register("max_ammo_count", () -> new RangedAttribute("marksman_elite.attribute.maneuverability.description", 0, 0, Double.MAX_VALUE));


    public static <T> Codec<T> byNameCodec(ResourceKey<Registry<T>> key) {
        return getRegistry(key).byNameCodec();
    }

    public static <T> @NotNull Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
        return ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(key);
    }
}
