package com.koshakmine.blocktypepatch.mixin;

import cn.nukkit.block.Block;
import cn.nukkit.block.customblock.CustomBlock;
import cn.nukkit.block.material.BlockType;
import cn.nukkit.block.material.BlockTypes;
import cn.nukkit.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Block.class)
public abstract class BlockMixin {
    @Shadow
    private BlockType type;

    @Shadow
    public abstract int getId();

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract boolean isAir();

    @Shadow
    public abstract BlockType getBlockType();

    @Inject(method = "getBlockType", at = @At("HEAD"), cancellable = true)
    private void getBlockType(CallbackInfoReturnable<BlockType> cir) {
        if (this.type != null) {
            cir.setReturnValue(this.type);
            return;
        }

        if (this instanceof CustomBlock customBlock) {
            this.type = BlockTypes.get(customBlock.getIdentifier());
        } else if (this.isAir()) {
            this.type = BlockTypes.AIR;
        } else {
            this.type = BlockTypes.get(Registries.BLOCK_TO_ITEM.get(this.getId() > 255 ? 255 - this.getId() : this.getId(), this.getDamage()));
        }

        cir.setReturnValue(this.type);
    }

    @Inject(method = "getIdentifier", at = @At("HEAD"), cancellable = true)
    private void getIdentifier(CallbackInfoReturnable<String> cir) {
        if (this.getBlockType() != null) {
            cir.setReturnValue(this.getBlockType().getIdentifier());
        } else {
            cir.setReturnValue("");
        }
    }
}
