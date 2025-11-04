package io.github.tt432.marksmanelite;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.tt432.marksmanelite.player.MouseClickPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
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

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(MarksmanElite.BULLET_ENTITY.get(), NoopRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton.Post event) {
        if (Minecraft.getInstance().getConnection() != null && Minecraft.getInstance().player != null &&
                (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            MouseClickPayload payload = new MouseClickPayload(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT, event.getAction() == InputConstants.PRESS);
            Minecraft.getInstance().player.getData(MarksmanElite.PLAYER_GUN_STATE_ATTACHMENT).handle(payload, Minecraft.getInstance().player);//TODO MAYBE?
            PacketDistributor.sendToServer(payload);
        }
    }

    @SubscribeEvent
    public static void onGunAiming(ComputeFovModifierEvent event) {
        event.setNewFovModifier(event.getPlayer().getData(MarksmanElite.PLAYER_GUN_STATE_ATTACHMENT).getScalingFactor() * event.getNewFovModifier());
    }
}
