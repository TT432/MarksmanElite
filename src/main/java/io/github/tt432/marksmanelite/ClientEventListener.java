package io.github.tt432.marksmanelite;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.tt432.marksmanelite.guns.TestingGun;
import io.github.tt432.marksmanelite.player.MouseClickPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventListener {
    private static int aimingTicker = 0;
    private static ItemStack lastUsing = ItemStack.EMPTY;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(MarksmanElite.BULLET_ENTITY.get(), NoopRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton.Post event) {
        if (Minecraft.getInstance().getConnection() != null && (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            PacketDistributor.sendToServer(new MouseClickPayload(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT, event.getAction() == InputConstants.PRESS));
        }
    }

    @SubscribeEvent
    public static void onGunAiming(ComputeFovModifierEvent event) {
        ItemStack stack = event.getPlayer().getUseItem();
        if (stack.getItem() instanceof TestingGun gun) {
            if (stack != lastUsing) {//如果直接切枪，快速重置缩放
                aimingTicker = 0;
                lastUsing = stack;
            }
            float fovFactor = 0.8F;
            int aimingTime = 10;//TODO get form gun
            event.setNewFovModifier(event.getNewFovModifier() * (1 - (1 - fovFactor) * aimingTicker / aimingTime));
            if (aimingTicker < aimingTime) {
                aimingTicker++;
            }
        } else if (aimingTicker > 0) {
            float fovFactor = 0.8F;
            int aimingTime = 20;//TODO get form gun
            event.setNewFovModifier(event.getNewFovModifier() * (1 - (1 - fovFactor) * aimingTicker / aimingTime));
            aimingTicker--;
        }
    }
}
