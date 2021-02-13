package moe.gensoukyo.lib.util.cnpc

import moe.gensoukyo.lib.constants.ModIds
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.common.Optional
import noppes.npcs.api.IDamageSource
import noppes.npcs.api.INbt
import noppes.npcs.api.IWorld
import noppes.npcs.api.NpcAPI
import noppes.npcs.api.entity.IEntity
import noppes.npcs.api.entity.IEntityLivingBase
import noppes.npcs.api.entity.IPlayer
import noppes.npcs.api.item.IItemStack

// Entity

val Entity.npcApi: IEntity<*>
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = NpcAPI.Instance().getIEntity(this)

val EntityLivingBase.npcApi: IEntityLivingBase<*>
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = (this as Entity).npcApi as IEntityLivingBase<*>

val EntityPlayer.npcApi: IPlayer<*>
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = (this as Entity).npcApi as IPlayer<*>

// Misc

val ItemStack.npcApi: IItemStack
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = NpcAPI.Instance().getIItemStack(this)

val World.npcApi: IWorld
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = if (this is WorldServer) {
        NpcAPI.Instance().getIWorld(this)
    } else {
        throw IllegalStateException("Getting NpcApi wrapped world from client.")
    }

val NBTTagCompound.npcApi: INbt
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = NpcAPI.Instance().getINbt(this)

val DamageSource.npcApi: IDamageSource
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = NpcAPI.Instance().getIDamageSource(this)