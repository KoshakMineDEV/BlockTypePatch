/*
 * This file is part of Ignite, licensed under the MIT License (MIT).
 *
 * Copyright (c) vectrix.space <https://vectrix.space/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
  @Shadow private BlockType type;
  @Shadow public abstract int getId();
  @Shadow public abstract int getDamage();
  @Shadow public abstract boolean isAir();
  @Shadow public abstract BlockType getBlockType();

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
    if(this.getBlockType() != null) {
      cir.setReturnValue(this.getBlockType().getIdentifier());
    } else {
      cir.setReturnValue("");
    }
  }
}
