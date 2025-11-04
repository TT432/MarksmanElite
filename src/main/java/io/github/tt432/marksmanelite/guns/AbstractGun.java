package io.github.tt432.marksmanelite.guns;

import io.github.tt432.marksmanelite.MarksmanElite;
import io.github.tt432.marksmanelite.player.PlayerGunState;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.function.BiConsumer;

@EventBusSubscriber(modid = MarksmanElite.MODID)
public class AbstractGun extends Item {
    public final AmmoBehavior ammoBehavior;
    public final InitialAccessory initialAccessory;

    public AbstractGun(AmmoBehavior ammoBehavior, InitialAccessory initialAccessory) {
        super(new Properties().fireResistant().stacksTo(1).component(MarksmanElite.GUN_DATA_COMPONENT, new GunDataComponent(initialAccessory)));
        this.ammoBehavior = ammoBehavior;
        this.initialAccessory = initialAccessory;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.fail(player.getItemInHand(usedHand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return true;
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().getItemInHand(event.getHand()).getItem() instanceof AbstractGun) {
            event.setCanceled(true);
        }
    }


    public void startShooting(Player player, ItemStack stack, PlayerGunState state) {
    }

    public void onShootingTick(Player player, ItemStack stack, PlayerGunState state, int shootingTicker) {
        double attachSpeed = player.getAttribute(Attributes.ATTACK_SPEED).getValue();
        if ((int) (shootingTicker / attachSpeed) > (int) ((shootingTicker - 1) / attachSpeed)) {//射速处理器
            shoot(player, stack);
        }
    }

    public void stopShooting(Player player, ItemStack stack, PlayerGunState state) {
    }

    public void startAiming(Player player, ItemStack stack, PlayerGunState state) {
    }

    public void finishAiming(Player player, ItemStack stack, PlayerGunState state) {
    }

    public void stopAiming(Player player, ItemStack stack, PlayerGunState state, boolean aimingFinished) {
    }

    public void shoot(Player player, ItemStack gun) {
        //TODO 在这里附加视角动画效果
        //TODO 扣除子弹
        if (player.level().isClientSide) return;//只在服务端创建子弹
        this.ammoBehavior.behavior.accept(player, gun);
    }


    public enum AmmoBehavior {
        NO_GRAVITY((player, stack) -> {
            Level level = player.level();
            Vec3 lookVec = player.getLookAngle();
            double speed = 20.0; // 子弹速度

            // 创建子弹实体
            BulletEntity bullet = new BulletEntity(level, player, lookVec.scale(speed), player.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            Vec3 eyePos = player.getEyePosition(0);//TODO 应为模型位置
            bullet.setPos(eyePos.x, eyePos.y, eyePos.z);

            level.addFreshEntity(bullet);
        });
//        GRAVITY,
//        DIRECTLY;

        public final BiConsumer<Player, ItemStack> behavior;

        AmmoBehavior(BiConsumer<Player, ItemStack> behavior) {
            this.behavior = behavior;
        }
    }

    @Override
    public DataComponentMap components() {
        return super.components();
    }

    public record InitialAccessory(ResourceLocation scope, ResourceLocation muzzle, ResourceLocation lowerHanging,
                                   ResourceLocation magazine) {
    }
}
