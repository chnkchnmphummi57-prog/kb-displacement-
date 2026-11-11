package com.knockbackdisplacement;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KnockbackMod implements ClientModInitializer {
    private static KeyBinding actionKeyBinding;
    private static KeyBinding menuKeyBinding;

    private static boolean active = false;
    private static boolean toggleState = false;
    private static int state = 0; // 0 = Idle, 1 = Rotated
    private static float originalYaw = 0.0f;

    @Override
    public void onInitializeClient() {
        // Action Keybind (Default: R)
        actionKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.knockbackdisplacement.action",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.knockbackdisplacement.general"
        ));

        // Settings Menu Keybind (Default: Right Shift)
        menuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.knockbackdisplacement.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.knockbackdisplacement.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Open YACL settings screen
            while (menuKeyBinding.wasPressed()) {
                client.setScreen(KnockbackConfig.createScreen(client.currentScreen));
            }

            KnockbackConfig.Mode mode = KnockbackConfig.mode;

            if (mode == KnockbackConfig.Mode.TOGGLE) {
                while (actionKeyBinding.wasPressed()) {
                    toggleState = !toggleState;
                    if (toggleState) {
                        active = true;
                        state = 0;
                    } else {
                        active = false;
                    }
                }
            } else if (mode == KnockbackConfig.Mode.HOLD) {
                active = actionKeyBinding.isPressed();
                if (!active) state = 0;
            } else { // SINGLE
                while (actionKeyBinding.wasPressed()) {
                    if (!active) {
                        active = true;
                        state = 0;
                    }
                }
            }

            // Core state machine logic
            if (active) {
                if (state == 0) {
                    // Tick 1: Rotate camera by configured angle
                    originalYaw = client.player.getYaw();
                    client.player.setYaw(originalYaw + KnockbackConfig.angle);
                    state = 1;
                } else if (state == 1) {
                    // Tick 2: Rotate back and attack (Left Mouse Button) in the same tick
                    client.player.setYaw(originalYaw);

                    if (client.interactionManager != null && client.player != null) {
                        client.interactionManager.attackEntity(client.player, client.targetedEntity);
                        client.player.swingHand(client.player.getActiveHand());
                    }

                    if (mode == KnockbackConfig.Mode.SINGLE) {
                        active = false;
                    }
                    state = 0;
                }
            }
        });
    }
}
