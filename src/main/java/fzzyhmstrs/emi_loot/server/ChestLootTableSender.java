package fzzyhmstrs.emi_loot.server;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ChestLootTableSender implements LootSender<ChestLootPoolBuilder> {

    public ChestLootTableSender(Identifier id){
        this.id = id;
    }

    private final Identifier id;
    private final List<ChestLootPoolBuilder> builderList = new LinkedList<>();

    @Override
    public void send(ServerPlayerEntity player) {
        HashMap<ItemStack, Float> floatMap = new HashMap<>();
        builderList.forEach((builder) -> {
            builder.build();
            builder.builtMap.forEach((item,weight)->{
                if (!item.isOf(Items.AIR)) {
                    if (floatMap.containsKey(item)) {
                        float oldWeight = floatMap.getOrDefault(item, 0f);
                        floatMap.put(item, oldWeight + weight);
                    } else {
                        floatMap.put(item, weight);
                    }
                }
            });
        });

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(id);
        buf.writeShort(floatMap.size());
        floatMap.forEach((item, floatWeight)->{
            buf.writeItemStack(item);
            buf.writeFloat(floatWeight);
        });
        ServerPlayNetworking.send(player,ChestLootPoolBuilder.CHEST_SENDER, buf);
    }

    @Override
    public void addBuilder(ChestLootPoolBuilder builder) {
        builderList.add(builder);
    }
}
