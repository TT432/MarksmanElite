package io.github.tt432.marksmanelite.guns;

import io.github.tt432.marksmanelite.MarksmanElite;
import io.github.tt432.marksmanelite.player.PlayerGunState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
        if (event.getEntity().getItemInHand(event.getHand()).getItem() instanceof TestingGun) {
            event.setCanceled(true);
        }
    }

    public void onLeftPress(Player player, ItemStack stack, PlayerGunState state){
    }

    public void onLeftPressTick(Player player, ItemStack stack, PlayerGunState state,int deltaTicker){
        shoot(player);
    }

    public void onLeftRelease(Player player, ItemStack stack, PlayerGunState state){
    }

    public void shoot(Player player) {
        Level level = player.level();

        Vec3 lookVec = player.getLookAngle();
        double speed = 20.0; // 子弹速度

        // 创建子弹实体
        BulletEntity bullet = new BulletEntity(level, player, lookVec.scale(speed));
        Vec3 eyePos = player.getEyePosition(0);//感觉有点偏高啊？
        bullet.setPos(eyePos.x, eyePos.y, eyePos.z);

        level.addFreshEntity(bullet);
    }


}
