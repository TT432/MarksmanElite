package io.github.tt432.marksmanelite.guns;

import io.github.tt432.marksmanelite.MarksmanElite;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = MarksmanElite.MODID)
public class TestingGun extends Item {
    public TestingGun() {
        super(new Properties().fireResistant().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }


    //TODO 还是改为客户端拦截鼠标左键 然后发包给服务端生成子弹

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        shoot(player);
        return true;
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().getItemInHand(event.getHand()).getItem() instanceof TestingGun gun) {
            event.setCanceled(true);
            gun.shoot(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity().getItemInHand(event.getHand()).getItem() instanceof TestingGun gun) {
            gun.shoot(event.getEntity());
        }
    }

    public void shoot(Player player) {
        if (!player.level().isClientSide) {
            Level level = player.level();

            Vec3 lookVec = player.getLookAngle();
            double speed = 5.0; // 子弹速度

            // 创建子弹实体
            BulletEntity bullet = new BulletEntity(level, player, lookVec.scale(speed));
            Vec3 eyePos = player.getEyePosition(0);//感觉有点偏高啊？
            bullet.setPos(eyePos.x, eyePos.y, eyePos.z);

            level.addFreshEntity(bullet);

            // 消耗耐久（如果需要）
            // stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
        }
    }


}
