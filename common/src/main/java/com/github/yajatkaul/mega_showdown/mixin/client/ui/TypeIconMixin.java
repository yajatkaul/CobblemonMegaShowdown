package com.github.yajatkaul.mega_showdown.mixin.client.ui;

import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.client.gui.TypeIcon;
import com.github.yajatkaul.mega_showdown.MegaShowdown;
import com.github.yajatkaul.mega_showdown.datapack.CustomTypeRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

@Mixin(value = TypeIcon.class, remap = false)
public class TypeIconMixin {
    @Unique
    private final int TYPE_ICON_DIAMETER = 36;
    @Unique
    private final float SCALE = 0.5F;

    @Unique
    private final ResourceLocation defaultTypesResource = cobblemonResource("textures/gui/types.png");
    @Unique
    private final ResourceLocation defaultSmallTypesResource = cobblemonResource("textures/gui/types_small.png");

    @Shadow @Final private Number x;
    @Shadow @Final private Number y;
    @Shadow @Final private ElementalType type;
    @Shadow @Final @Nullable private ElementalType secondaryType;
    @Shadow @Final private boolean centeredX;
    @Shadow @Final private boolean small;
    @Shadow @Final private float secondaryOffset;
    @Shadow @Final private float doubleCenteredOffset;
    @Shadow @Final private float opacity;

    // Cache these per instance since type is @Final
    @Unique
    private ResourceLocation primaryTexture;
    @Unique
    private ResourceLocation primarySmallTexture;
    @Unique
    private float primaryU;
    @Unique
    private float primaryWidth;
    @Unique
    private boolean primaryIsCustom;

    @Unique
    private ResourceLocation secondaryTexture;
    @Unique
    private ResourceLocation secondarySmallTexture;
    @Unique
    private float secondaryU;
    @Unique
    private float secondaryWidth;
    @Unique
    private boolean secondaryIsCustom;

    @Unique
    private boolean initialized = false;

    @Unique
    private void initializeTextures() {
        if (initialized) return;

        float diameter = small ? (TYPE_ICON_DIAMETER / 2f) : TYPE_ICON_DIAMETER;

        // Initialize primary type
        primaryIsCustom = CustomTypeRegistry.INSTANCE.customTypes.contains(type);
        if (primaryIsCustom) {
            primaryTexture = ResourceLocation.fromNamespaceAndPath(
                    MegaShowdown.MOD_ID,
                    "textures/gui/types/" + type.getShowdownId().toLowerCase() + ".png"
            );
            primarySmallTexture = ResourceLocation.fromNamespaceAndPath(
                    MegaShowdown.MOD_ID,
                    "textures/gui/types/" + type.getShowdownId().toLowerCase() + "_small.png"
            );
            primaryU = 0;
            primaryWidth = diameter;
        } else {
            primaryTexture = defaultTypesResource;
            primarySmallTexture = defaultSmallTypesResource;
            primaryU = diameter * type.getTextureXMultiplier() + 0.1f;
            primaryWidth = diameter * 18;
        }

        // Initialize secondary type if exists
        if (secondaryType != null) {
            secondaryIsCustom = CustomTypeRegistry.INSTANCE.customTypes.contains(secondaryType);
            if (secondaryIsCustom) {
                secondaryTexture = ResourceLocation.fromNamespaceAndPath(
                        MegaShowdown.MOD_ID,
                        "textures/gui/types/" + secondaryType.getShowdownId().toLowerCase() + ".png"
                );
                secondarySmallTexture = ResourceLocation.fromNamespaceAndPath(
                        MegaShowdown.MOD_ID,
                        "textures/gui/types/" + secondaryType.getShowdownId().toLowerCase() + "_small.png"
                );
                secondaryU = 0;
                secondaryWidth = diameter;
            } else {
                secondaryTexture = defaultTypesResource;
                secondarySmallTexture = defaultSmallTypesResource;
                secondaryU = diameter * secondaryType.getTextureXMultiplier() + 0.1f;
                secondaryWidth = diameter * 18;
            }
        }

        initialized = true;
    }

    /**
     * @author YajatKaul
     * @reason Please dont judge me i cant do this big of a project alone now
     */
    @Overwrite(remap = true)
    public final void render(GuiGraphics context) {
        initializeTextures();

        float diameter = small ? (TYPE_ICON_DIAMETER / 2f) : TYPE_ICON_DIAMETER;
        float offsetX = centeredX
                ? (((diameter / 2f) * SCALE) + (secondaryType != null ? doubleCenteredOffset : 0f))
                : 0f;

        // Render secondary type if exists
        if (secondaryType != null) {
            GuiUtilsKt.blitk(
                    context.pose(),
                    small ? secondarySmallTexture : secondaryTexture,
                    (x.floatValue() + secondaryOffset - offsetX) / SCALE,
                    y.floatValue() / SCALE,
                    diameter,
                    diameter,
                    secondaryU,
                    0,
                    secondaryWidth,
                    diameter,
                    0,
                    1,
                    1,
                    1,
                    opacity,
                    true,
                    SCALE
            );
        }

        // Render primary type
        GuiUtilsKt.blitk(
                context.pose(),
                small ? primarySmallTexture : primaryTexture,
                (x.floatValue() - offsetX) / SCALE,
                y.floatValue() / SCALE,
                diameter,
                diameter,
                primaryU,
                0,
                primaryWidth,
                diameter,
                0,
                1,
                1,
                1,
                opacity,
                true,
                SCALE
        );
    }
}