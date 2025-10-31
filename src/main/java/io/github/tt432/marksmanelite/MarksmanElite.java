package io.github.tt432.marksmanelite;

import com.mojang.serialization.MapCodec;
import io.github.tt432.marksmanelite.accessory.BasicAccessory;
import io.github.tt432.marksmanelite.accessory.MuzzleAccessory;
import io.github.tt432.marksmanelite.accessory.ScopeAccessory;
import io.github.tt432.marksmanelite.guns.BulletEntity;
import io.github.tt432.marksmanelite.guns.TestingGun;
import io.github.tt432.marksmanelite.player.PlayerGunState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(MarksmanElite.MODID)
public class MarksmanElite {
    public static final String MODID = "marksman_elite";

    public MarksmanElite(IEventBus eventBus) {
        ACCESSORY_REG.register(eventBus);
        ENTITY_TYPE_REG.register(eventBus);
        ITEM_REG.register(eventBus);
        ATTACHMENT_REG.register(eventBus);
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


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REG = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MarksmanElite.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BULLET_ENTITY =
            ENTITY_TYPE_REG.register("bullet", () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f)
                    .clientTrackingRange(4)
                    .updateInterval(1)
                    .build("bullet"));


    public static final DeferredRegister.Items ITEM_REG = DeferredRegister.createItems(MarksmanElite.MODID);

    public static final DeferredItem<TestingGun> TEST_GUN = ITEM_REG.register("test_gun", TestingGun::new);


    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_REG = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MarksmanElite.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerGunState>> PLAYER_GUN_STATE_ATTACHMENT =
            ATTACHMENT_REG.register("player_gun_state", () -> AttachmentType.<PlayerGunState>builder(PlayerGunState::new).build());
}
