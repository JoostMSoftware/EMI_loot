package fzzyhmstrs.emi_loot.server;

import fzzyhmstrs.emi_loot.EMILoot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class ChestLootPoolBuilder implements LootBuilder {

    public ChestLootPoolBuilder(float rollWeight){
        this.rollWeight = rollWeight;
    }

    private final HashMap<ItemStack, Integer> map = new HashMap<>();
    private final float rollWeight;
    private Integer totalWeight = 0;
    HashMap<ItemStack, Float> builtMap = new HashMap<>();
    public static Identifier CHEST_SENDER = new Identifier(EMILoot.MOD_ID,"chest_sender");

    public void addItem(ItemStack item, int weight){
        totalWeight += weight;
        if (map.containsKey(item)){
            int oldWeight = map.getOrDefault(item,0);
            map.put(item,oldWeight + weight);
        } else {
            map.put(item,weight);
        }
    }

    @Override
    public void build() {
        HashMap<ItemStack, Float> floatMap = new HashMap<>();
        map.forEach((item, itemWeight)-> floatMap.put(item,(itemWeight.floatValue()/totalWeight * 100F * rollWeight)));
        builtMap = floatMap;
    }
}
