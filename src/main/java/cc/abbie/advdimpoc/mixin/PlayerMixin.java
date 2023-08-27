package cc.abbie.advdimpoc.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
    private void no(Level level, BlockPos pos, GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (level.dimension() == Level.END && gameMode == GameType.SURVIVAL)
            cir.setReturnValue(true);
    }

    @Inject(method = "mayUseItemAt", at = @At("HEAD"), cancellable = true)
    private void nono(BlockPos pos, Direction facing, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        GameType gameType;
        if (level().isClientSide) {
            gameType = Minecraft.getInstance().gameMode.getPlayerMode();
        } else {
            gameType = ((ServerPlayer)(Object)this).gameMode.getGameModeForPlayer();
        }
        if (level().dimension() == Level.END && gameType == GameType.SURVIVAL)
            cir.setReturnValue(false);
    }

    @Inject(method = "mayBuild", at = @At("HEAD"), cancellable = true)
    private void nonono(CallbackInfoReturnable<Boolean> cir) {
        GameType gameType;
        if (level().isClientSide) {
            gameType = Minecraft.getInstance().gameMode.getPlayerMode();
        } else {
            gameType = ((ServerPlayer)(Object)this).gameMode.getGameModeForPlayer();
        }
        if (level().dimension() == Level.END && gameType == GameType.SURVIVAL)
            cir.setReturnValue(false);
    }
}
