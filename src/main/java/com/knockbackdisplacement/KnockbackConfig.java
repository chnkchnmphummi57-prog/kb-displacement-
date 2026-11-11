package com.knockbackdisplacement;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class KnockbackConfig {
    public static float angle = 180.0f;
    public static Mode mode = Mode.SINGLE;

    public enum Mode {
        SINGLE("Single (1-Tick)"),
        HOLD("Hold to Repeat"),
        TOGGLE("Toggle");

        private final String name;
        Mode(String name) { this.name = name; }
        public String getName() { return name; }
    }

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Knockback Displacement Settings"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .option(Option.<Float>createBuilder()
                                .name(Text.literal("Rotation Angle"))
                                .description(OptionDescription.of(Text.literal("Angle to snap camera")))
                                .binding(180.0f, () -> angle, val -> angle = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.0f, 360.0f)
                                        .step(1.0f))
                                .build())
                        .option(Option.<Mode>createBuilder()
                                .name(Text.literal("Activation Mode"))
                                .description(OptionDescription.of(Text.literal("Single, Hold, or Toggle")))
                                .binding(Mode.SINGLE, () -> mode, val -> mode = val)
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(Mode.class))
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }
}
