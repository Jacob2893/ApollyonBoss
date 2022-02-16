package apollyon.apollyon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

/**
 * =====================================================================
 * Class WaterElemental inside apollyon.apollyon
 * Created by Jakub Ćwik on 28.09.2021
 * For ApollyonBoss
 * =====================================================================
 */

public class DarkKnight implements Listener {
    private Apollyon plugin;
    public DarkKnight(Apollyon apollyon){
        this.plugin = apollyon;
    }
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if(e.getBlock().getType()== Material.EMERALD_BLOCK){
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
            WitherSkeleton skeleton = e.getBlock().getWorld().spawn(e.getBlock().getLocation().add(0.5,0,0.5), WitherSkeleton.class);
            skeleton.setCustomName("§4§lDark Knight");
            skeleton.setCustomNameVisible(true);
            skeleton.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            skeleton.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            skeleton.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            skeleton.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            skeleton.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            skeleton.setMetadata("DarkKnight",new FixedMetadataValue(plugin,"darkknight"));
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof WitherSkeleton && e.getDamager() instanceof Player){
            if(e.getEntity().hasMetadata("DarkKnight")){
                int random = ThreadLocalRandom.current().nextInt(10);
                if(random<5){ //0 1 2 3 4
                    e.setCancelled(true);
                    Player player = (Player)e.getDamager();
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND,10,10);
                    player.sendMessage("§c§lYour attack was blocked!");
                }
            }
        }
        if(e.getDamager() instanceof WitherSkeleton && e.getEntity() instanceof Player){
            if(e.getDamager().hasMetadata("DarkKnight")){
                int random = ThreadLocalRandom.current().nextInt(10);
                if(random>5){
                    e.setCancelled(true);
                    Player player = (Player)e.getEntity();
                    player.setVelocity(new Vector(0,2,0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,2));
                    player.sendMessage("§4§lYou were punched into the air!");
                }
            }
        }
    }
}

/*
 * +-------------------------------+
 * | §0    black                   |
 * | §1    dark_blue               |
 * | §2    dark_green              |
 * | §3    dark_aqua               |
 * | §4    dark_red                |
 * | §5    dark_purple             |
 * | §6    gold                    |
 * | §7    gray                    |
 * | §8    dark_gray               |
 * | §9    blue                    |
 * | §a    green                   |
 * | §b    aqua                    |
 * | §c    red                     |
 * | §d    light_purple            |
 * | §e    yellow                  |
 * | §f    white                   |
 * |                               |
 * | §k    Obfuscated (MAGIC)      |
 * | §l    Bold                    |
 * | §m    Strikethrough           |
 * | §n    Underline               |
 * | §o    Italic                  |
 * | §r    Reset                   |
 * +-------------------------------+
 */