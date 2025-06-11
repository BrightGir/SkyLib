package me.bright.skylib.utils;

import me.bright.skylib.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();

    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        // item.d((short)durability);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
        meta.addEnchant(ench,level,false);
        return this;
    }

    public ItemBuilder setColoredName(String name) {
        meta.setDisplayName((Messenger.color(name)));
        return this;
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName((name));
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addLore(String... strings) {
        List<String> list = new ArrayList<>();
        for (String string: strings) {
            list.add(Messenger.color(string));
        }
        meta.setLore(list);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        item.getItemMeta().addItemFlags(flags);
        return this;
    }

    public ItemBuilder setAction(Game game, Consumer<PlayerInteractEvent> action) {
        try {
            item.setItemMeta(meta);
            game.addItemAction(item, action);
        } catch (NullPointerException ex) {
            Bukkit.getLogger().info("NULLPOINTEREX");
            Bukkit.getLogger().info("ITEMNAME = " + meta.getDisplayName());
            Bukkit.getLogger().info("game null == " + (game == null));
            Bukkit.getLogger().info("action null == " + (action == null));
        }
        return this;
    }

    public ItemBuilder setAttackAction(Game game, Consumer<EntityDamageByEntityEvent> action) {
        try {
            item.setItemMeta(meta);
            game.addAttackAction(item, action);
        } catch(NullPointerException ex) {
            Bukkit.getLogger().info("NULLPOINTEREX 2");
            Bukkit.getLogger().info("ITEMNAME = " + meta.getDisplayName());
            Bukkit.getLogger().info("game null == " + (game == null));
            Bukkit.getLogger().info("action null == " + (action == null));
        }
        return this;
    }

    public ItemStack create() {
        item.setItemMeta(meta);
        return item;
    }
}
