package awa.Aether_254.create_unrestricted_vault.mixin;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.logistics.vault.ItemVaultCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemVaultCTBehaviour.class)
abstract class ItemVaultCTBehaviourMixin {
    @Inject(method = "getShift", at = @At("HEAD"), cancellable = true, remap = false)
    private void createUnrestrictedVault$selectVerticalTexture(
        BlockState state,
        Direction direction,
        @Nullable TextureAtlasSprite sprite,
        CallbackInfoReturnable<CTSpriteShiftEntry> cir
    ) {
        if (ItemVaultBlock.getVaultBlockAxis(state) != Direction.Axis.Y)
            return;

        boolean small = !ItemVaultBlock.isLarge(state);
        if (direction == Direction.UP)
            cir.setReturnValue(AllSpriteShifts.VAULT_FRONT.get(small));
        else if (direction == Direction.DOWN)
            cir.setReturnValue(AllSpriteShifts.VAULT_BOTTOM.get(small));
        else
            cir.setReturnValue(AllSpriteShifts.VAULT_SIDE.get(small));
    }

    @Inject(method = "getUpDirection", at = @At("HEAD"), cancellable = true, remap = false)
    private void createUnrestrictedVault$orientVerticalTextureUp(
        BlockAndTintGetter reader,
        BlockPos pos,
        BlockState state,
        Direction face,
        CallbackInfoReturnable<Direction> cir
    ) {
        if (ItemVaultBlock.getVaultBlockAxis(state) == Direction.Axis.Y && face.getAxis().isHorizontal())
            cir.setReturnValue(Direction.UP);
    }

    @Inject(method = "getRightDirection", at = @At("HEAD"), cancellable = true, remap = false)
    private void createUnrestrictedVault$orientVerticalTextureRight(
        BlockAndTintGetter reader,
        BlockPos pos,
        BlockState state,
        Direction face,
        CallbackInfoReturnable<Direction> cir
    ) {
        if (ItemVaultBlock.getVaultBlockAxis(state) == Direction.Axis.Y && face.getAxis().isHorizontal())
            cir.setReturnValue(face.getAxis() == Direction.Axis.X ? Direction.SOUTH : Direction.WEST);
    }
}
