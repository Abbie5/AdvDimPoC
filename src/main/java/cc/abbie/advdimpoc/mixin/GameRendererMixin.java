package cc.abbie.advdimpoc.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final
    Minecraft minecraft;

    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void no(CallbackInfoReturnable<Boolean> cir) {
        if (!(minecraft.cameraEntity instanceof Player p)) return;
        if (p.level().dimension() == Level.END && minecraft.gameMode.getPlayerMode() == GameType.SURVIVAL)
            cir.setReturnValue(false);
    }
}
