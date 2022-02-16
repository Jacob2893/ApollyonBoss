package apollyon.apollyon;

import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * =====================================================================
 * Class Boss inside apollyon.apollyon
 * Created by Jakub Ćwik on 26.06.2021
 * For ApollyonBoss
 * =====================================================================
 */



public class Boss implements Listener {

    private static final String messagesSpawn[] = {
            "§c§l[Apollyon] §8» §f§lMUAHAHA! GŁUPCY!",
            "§c§l[Apollyon] §8» §f§lPRZYGOTUJCIE SIĘ NA ŚMIERĆ!"
    };
    private static final String messagesDeath[] = {
            "§c§l[Apollyon] §8» §f§lNIE! TO NIE MIAŁO TAK SIĘ SKOŃCZYĆ!",
            "§c§l[Apollyon] §8» §f§lJESZCZE MNIE ZOBACZYCIE!"
    };

    private Apollyon plugin;
    private Wither apollyon;
    private Location enchantingLocation;
    private boolean isSpawnedBoss = false;
    private Location campfireLocations[] = {
            new Location(Bukkit.getWorld("world_nether") , 329, 55, -81),
            new Location(Bukkit.getWorld("world_nether") , 329, 55, -101),
            new Location(Bukkit.getWorld("world_nether") , 309, 55, -101),
            new Location(Bukkit.getWorld("world_nether") , 309, 55, -81)
    };
    private Location fireLocations[] = {
            new Location(Bukkit.getWorld("world_nether") , 322, 45, -94),
            new Location(Bukkit.getWorld("world_nether") , 322, 45, -88),
            new Location(Bukkit.getWorld("world_nether") , 316, 45, -88),
            new Location(Bukkit.getWorld("world_nether") , 316, 45, -94)
    };
    private Location lavaLocations[] = {
            new Location(Bukkit.getWorld("world_nether") , 301, 53, -71),
            new Location(Bukkit.getWorld("world_nether") , 300, 53, -72),
            new Location(Bukkit.getWorld("world_nether") , 299, 53, -73),
            new Location(Bukkit.getWorld("world_nether") , 339, 53, -73),
            new Location(Bukkit.getWorld("world_nether") , 338, 53, -72),
            new Location(Bukkit.getWorld("world_nether") , 337, 53, -71),
            new Location(Bukkit.getWorld("world_nether") , 337, 53, -111),
            new Location(Bukkit.getWorld("world_nether") , 338, 53, -110),
            new Location(Bukkit.getWorld("world_nether") , 339, 53, -109),
            new Location(Bukkit.getWorld("world_nether") , 301, 53, -111),
            new Location(Bukkit.getWorld("world_nether") , 300, 53, -110),
            new Location(Bukkit.getWorld("world_nether") , 299, 53, -109)
    };
    private ArrayList<Player> playersInArena = new ArrayList<>();

    public Boss(Apollyon apollyon) {
        this.plugin = apollyon;
    }
    //================================ITEM PRZYZYWAJĄCY=================
    public static ItemStack summoningItem(){
        ItemStack itemStack = new ItemStack(Material.CRYING_OBSIDIAN, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lFragment ciemności"));
        itemMeta.setLore(Arrays.asList(" ",
                ChatColor.translateAlternateColorCodes('&', "&c&lUżywany do przyzywania pradawnego demona."),
                ChatColor.translateAlternateColorCodes('&', "&c&lNależy go złożyć w centrum mrocznej twierdzy.")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //================================WYPOSAŻENIE STRAŻNIKÓW============

    public static ItemStack guardHelmet(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_HELMET, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8§lHełm Strażnika Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack guardChestplate(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8§lNapierśnik Strażnika Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack guardLeggings(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8§lSpodnie Strażnika Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack guardBoots(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_BOOTS, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8§lButy Strażnika Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack guardSword(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_SWORD, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, false);
        itemMeta.addEnchant(Enchantment.KNOCKBACK, 2, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack guardBow(){
        ItemStack itemStack = new ItemStack(Material.BOW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, false);
        itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, false);
        itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //================================DROPY Z APOLLYONA============

    public static ItemStack apollyonHelmet(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_HELMET, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lHełm Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack apollyonChestplate(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lNapierśnik Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack apollyonLeggings(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lSpodnie Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack apollyonBoots(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_BOOTS, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lButy Apollyon'a");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack apollyonSword(){
        ItemStack itemStack = new ItemStack(Material.NETHERITE_SWORD, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lMiecz Apollyon'a");
        itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack apollyonBow(){
        ItemStack itemStack = new ItemStack(Material.BOW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lŁuk Apollyon'a");
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 6, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 3, false);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack apollyonShield(){
        ItemStack itemStack = new ItemStack(Material.SHIELD, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lTarcza Apollyon'a");
        itemMeta.addEnchant(Enchantment.DURABILITY, 10, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //==================================RESPIENIE BOSSA I MINIONY=======

    @EventHandler
    public void OnRightClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
                if(e.getClickedBlock().getLocation().equals(new Location(Bukkit.getWorld("world_nether"), 319, 44, -91))) {
                    Player player = e.getPlayer();
                    if (summoningItem().isSimilar(player.getInventory().getItemInMainHand())) {
                        player.getInventory().removeItem(summoningItem());
                        Bukkit.broadcastMessage("§f§l" + player.getName() + " wezwał §c§lApollyon'a");
                        e.setCancelled(true);
                        enchantingLocation = e.getClickedBlock().getLocation();
                        e.getClickedBlock().setType(Material.POLISHED_BLACKSTONE_BRICK_SLAB);
                        isSpawnedBoss = true;
                        for (Player player1 : Bukkit.getWorld("world_nether").getPlayers()) {
                            if (player1.getLocation().getBlockY() < 60 && player1.getLocation().getBlockY() > 42) {
                                if (player1.getLocation().distance(enchantingLocation) < 26) {
                                    playersInArena.add(player1);
                                }
                            }
                        }
                        for (Location loc : campfireLocations) {
                            loc.getBlock().setType(Material.CAMPFIRE);
                        }
                        for (Location loc : fireLocations) {
                            loc.getBlock().setType(Material.FIRE);
                        }
                        for (Location loc : lavaLocations) {
                            loc.getBlock().setType(Material.LAVA);
                        }
                        new BukkitRunnable() {
                            private int i = 56;

                            @Override
                            public void run() {
                                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -89).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -90).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -91).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -92).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -93).getBlock().setType(Material.IRON_BARS);
                                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -91), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                                new Location(Bukkit.getWorld("world_nether"), 317, (double) i, -65).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 318, (double) i, -65).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -65).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 320, (double) i, -65).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 321, (double) i, -65).getBlock().setType(Material.IRON_BARS);
                                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -65), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -89).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -90).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -91).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -92).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -93).getBlock().setType(Material.IRON_BARS);
                                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -91), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                                new Location(Bukkit.getWorld("world_nether"), 317, (double) i, -117).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 318, (double) i, -117).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -117).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 320, (double) i, -117).getBlock().setType(Material.IRON_BARS);
                                new Location(Bukkit.getWorld("world_nether"), 321, (double) i, -117).getBlock().setType(Material.IRON_BARS);
                                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -117), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                                if (i == 45) {
                                    playersInArena.clear();
                                    for (Player player1 : Bukkit.getWorld("world_nether").getPlayers()) {
                                        if (player1.getLocation().getBlockY() < 60 && player1.getLocation().getBlockY() > 42) {
                                            if (player1.getLocation().distance(enchantingLocation) < 26) {
                                                playersInArena.add(player1);
                                            }
                                        }
                                    }
                                    if(playersInArena.size() > 0) {
                                        spawnApollyon(player, e);
                                    } else {
                                        openGate(player);
                                    }
                                    this.cancel();
                                } else {
                                    i--;
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 15L);

                    } else {
                        e.setCancelled(true);
                    }
                }
            } else {
                if(summoningItem().isSimilar(e.getPlayer().getInventory().getItemInMainHand())){
                    e.setCancelled(true);
                }
            }
        }
    }

    private void spawnApollyon(Player player, PlayerInteractEvent e){
        //GRACZ KTÓRY WEZWAŁ AP
        e.getClickedBlock().getLocation().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 0.5f);
        for(Player player1 : playersInArena) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                private int i = 0;

                @Override
                public void run() {
                    if (i != messagesSpawn.length) {
                        player1.sendMessage(messagesSpawn[i]);
                        i++;
                    } else {
                        timer.cancel();
                    }
                }
            }, 0, 1000 * 2);
        }
        //==========APOLLYON=========

            Wither wither = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation().add(0.5, 1, 0.5), Wither.class);
            Attributable witherAttributable = (Attributable) wither;
            AttributeInstance attributeInstance = witherAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            attributeInstance.setBaseValue(1000.0 + (500 * (playersInArena.size() - 1)));
            wither.setHealth(1000 + (500 * (playersInArena.size() - 1)));
            wither.setCustomName("§c§lApollyon");
            wither.setCustomNameVisible(true);
            wither.getBossBar().setVisible(false);
            wither.setMetadata("Apollyon", new FixedMetadataValue(plugin, "apollyon"));
            apollyon = wither;
                for (Player player1 : playersInArena) {
                    BossBarManager.setMessage(player1.getUniqueId(), ChatColor.translateAlternateColorCodes('&', "&c&lAPOLLYON &f&l| &4" + (int) wither.getHealth() + " / " + (int) wither.getMaxHealth()), BarColor.RED, BarStyle.SEGMENTED_20, 1);
                }
                //============STRAŻNICY==========
                //WOJOWNICY
                for (int i = 0; i < 3; i++) {
                    WitherSkeleton witherSkeleton = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation().add(i * 2 + 5.5, -1, 0.5), WitherSkeleton.class);
                    witherSkeleton.setCustomName("§8§lStrażnik Apollyon'a");
                    witherSkeleton.setCustomNameVisible(true);
                    witherSkeleton.getEquipment().setItemInHand(guardSword());
                    witherSkeleton.getEquipment().setHelmet(guardHelmet());
                    witherSkeleton.getEquipment().setChestplate(guardChestplate());
                    witherSkeleton.getEquipment().setLeggings(guardLeggings());
                    witherSkeleton.getEquipment().setBoots(guardBoots());
                    witherSkeleton.setMetadata("StraznikApollyona", new FixedMetadataValue(plugin, "straznikapollyona"));
                }
                for (int i = 0; i < 3; i++) {
                    WitherSkeleton witherSkeleton = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation().add(i * 2 - 5.5, -1, 0.5), WitherSkeleton.class);
                    witherSkeleton.setCustomName("§8§lStrażnik Apollyon'a");
                    witherSkeleton.setCustomNameVisible(true);
                    witherSkeleton.getEquipment().setItemInHand(guardSword());
                    witherSkeleton.getEquipment().setHelmet(guardHelmet());
                    witherSkeleton.getEquipment().setChestplate(guardChestplate());
                    witherSkeleton.getEquipment().setLeggings(guardLeggings());
                    witherSkeleton.getEquipment().setBoots(guardBoots());
                    witherSkeleton.setMetadata("StraznikApollyona", new FixedMetadataValue(plugin, "straznikapollyona"));
                }
                //ŁUCZNICY
                for (int i = 0; i < 2; i++) {
                    WitherSkeleton witherSkeleton = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation().add(0.5, -1, i * 2 + 5.5), WitherSkeleton.class);
                    witherSkeleton.setCustomName("§8§lStrażnik Apollyon'a");
                    witherSkeleton.setCustomNameVisible(true);
                    witherSkeleton.getEquipment().setItemInHand(guardBow());
                    witherSkeleton.getEquipment().setHelmet(guardHelmet());
                    witherSkeleton.getEquipment().setChestplate(guardChestplate());
                    witherSkeleton.getEquipment().setLeggings(guardLeggings());
                    witherSkeleton.getEquipment().setBoots(guardBoots());
                    witherSkeleton.setMetadata("StraznikApollyona", new FixedMetadataValue(plugin, "straznikapollyona"));
                }
                for (int i = 0; i < 2; i++) {
                    WitherSkeleton witherSkeleton = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation().add(0.5, -1, i * 2 - 5.5), WitherSkeleton.class);
                    witherSkeleton.setCustomName("§8§lStrażnik Apollyon'a");
                    witherSkeleton.setCustomNameVisible(true);
                    witherSkeleton.getEquipment().setItemInHand(guardBow());
                    witherSkeleton.getEquipment().setHelmet(guardHelmet());
                    witherSkeleton.getEquipment().setChestplate(guardChestplate());
                    witherSkeleton.getEquipment().setLeggings(guardLeggings());
                    witherSkeleton.getEquipment().setBoots(guardBoots());
                    witherSkeleton.setMetadata("StraznikApollyona", new FixedMetadataValue(plugin, "straznikapollyona"));
                }
            }


    //=========================================ATAKI====================

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if((e.getEntity() instanceof Wither && e.getDamager() instanceof Player) || (e.getEntity() instanceof Wither && e.getDamager() instanceof Arrow)){
            if(e.getEntity().hasMetadata("Apollyon")){
                for (Player player1 : playersInArena){
                    BossBarManager.setMessage(player1.getUniqueId(), ChatColor.translateAlternateColorCodes('&', "&c&lAPOLLYON &f&l| &4" + (int)((Wither) e.getEntity()).getHealth() + " / " + (int)((Wither) e.getEntity()).getMaxHealth()), BarColor.RED, BarStyle.SEGMENTED_20, ((Wither) e.getEntity()).getHealth()/((Wither) e.getEntity()).getMaxHealth());
                }
                int random = ThreadLocalRandom.current().nextInt(99);
                if(random < 20){ //0 1 2 3 4
                    e.setCancelled(true);
                    Player player = (Player)e.getDamager();
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND,10,10);
                    player.sendMessage("§c§l[Apollyon] §8» §f§lHAHAHA! Twoje marne ataki nic mi nie robią!");
                }
                if(random > 19 && random < 29){
                    e.setCancelled(true);
                    Player player = (Player)e.getDamager();
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 10, 10);
                    player.setVelocity(new Vector(0,2,0));
                    player.sendMessage("§c§l[Apollyon] §8» §f§lOdsuń się!");
                }
                if(random > 28 && random < 34){
                    e.setCancelled(true);
                    Player player = (Player)e.getDamager();
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 10, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 2));
                    player.sendMessage("§c§l[Apollyon] §8» §f§lPrzepadnij!");
                }
                if(random > 33 && random < 39){
                    e.setCancelled(true);
                    Player player = (Player)e.getDamager();
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 10);
                    player.getActivePotionEffects().add(new PotionEffect(PotionEffectType.SLOW, 200, 999, false, false));
                    player.sendMessage("§c§l[Apollyon] §8» §f§lZabić go!");
                }
                if(random > 38 && random < 44){
                    e.setCancelled(true);
                    Player player = (Player)e.getDamager();
                    player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 10);
                    for (Player player1 : playersInArena) {
                        player1.sendMessage("§c§l[Apollyon] §8» §f§lStraż!!! Pozbyć się ich!!!");
                    }
                    if (e.getEntity() instanceof Wither) {
                        if(e.getEntity().hasMetadata("Apollyon")){
                            e.getEntity().teleport(new Location(e.getEntity().getLocation().getWorld(), 319, 55, -90));
                            ((Wither) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 999, false, false));
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        WitherSkeleton witherSkeleton = e.getEntity().getWorld().spawn(e.getEntity().getLocation(), WitherSkeleton.class);
                        witherSkeleton.setCustomName("§8§lStrażnik Apollyon'a");
                        witherSkeleton.setCustomNameVisible(true);
                        witherSkeleton.getEquipment().setItemInHand(guardSword());
                        witherSkeleton.getEquipment().setHelmet(guardHelmet());
                        witherSkeleton.getEquipment().setChestplate(guardChestplate());
                        witherSkeleton.getEquipment().setLeggings(guardLeggings());
                        witherSkeleton.getEquipment().setBoots(guardBoots());
                        witherSkeleton.setMetadata("StraznikApollyona", new FixedMetadataValue(plugin, "straznikapollyona"));
                    }
                    player.sendMessage("§c§l[Apollyon] §8» §f§lZabić go!");
                }
            }
        }
    }

    //=========================================DROPY====================

    @EventHandler
    public void onBreak(EntityChangeBlockEvent event){
        if (event.getEntity().getType() == EntityType.WITHER_SKULL) {
            event.setCancelled(true);
        }
        if (event.getEntity().getType() == EntityType.WITHER && event.getEntity().hasMetadata("Apollyon")){
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Wither) {
            if(e.getEntity().hasMetadata("Apollyon")){
                Bukkit.broadcastMessage("§c§lApollyon §f§lzostał pokonany...");
                for (Player player1 : playersInArena) {
                    player1.playSound(player1.getLocation(), Sound.ENTITY_WITHER_DEATH, 10, 0.5f);
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        private int i = 0;
                        @Override
                        public void run() {
                            if(i != messagesDeath.length){
                                player1.sendMessage(messagesDeath[i]);
                                player1.playSound(player1.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 0.5f);
                                i++;
                            } else {
                                timer.cancel();
                            }
                        }
                    }, 0, 1000 * 2);
                }
                e.getDrops().clear();
                int rando = ThreadLocalRandom.current().nextInt(6);
                if(rando == 0) {
                    e.getDrops().add(apollyonHelmet());
                }
                if(rando == 1) {
                    e.getDrops().add(apollyonChestplate());
                }
                if(rando == 2) {
                    e.getDrops().add(apollyonLeggings());
                }
                if(rando == 3) {
                    e.getDrops().add(apollyonBoots());
                }
                if(rando == 4) {
                    e.getDrops().add(apollyonSword());
                }
                if(rando == 5) {
                    e.getDrops().add(apollyonBow());
                }
                if(rando == 6) {
                    e.getDrops().add(apollyonShield());
                }


                isSpawnedBoss = false;
                for(Player player : playersInArena){
                    BossBarManager.remove(player.getUniqueId());
                }
                playersInArena = new ArrayList<>();
                for(Location loc : campfireLocations){
                    loc.getBlock().setType(Material.SPRUCE_SLAB);
                }
                for(Location loc : fireLocations){
                    loc.getBlock().setType(Material.AIR);
                }
                for (Location loc : lavaLocations) {
                    loc.getBlock().setType(Material.POLISHED_BLACKSTONE_BRICKS);
                }
                new BukkitRunnable()
                {
                    private int i = 45;
                    @Override
                    public void run()
                    {
                        new Location(Bukkit.getWorld("world_nether") , 293, (double) i, -89).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 293, (double) i, -90).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 293, (double) i, -91).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 293, (double) i, -92).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 293, (double) i, -93).getBlock().setType(Material.AIR);
                        Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether") , 293, (double) i, -91), Sound.BLOCK_CHAIN_PLACE, 10, 0.75f);
                        new Location(Bukkit.getWorld("world_nether") , 317, (double) i, -65).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 318, (double) i, -65).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 319, (double) i, -65).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 320, (double) i, -65).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 321, (double) i, -65).getBlock().setType(Material.AIR);
                        Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether") , 319, (double) i, -65), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                        new Location(Bukkit.getWorld("world_nether") , 345, (double) i, -89).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 345, (double) i, -90).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 345, (double) i, -91).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 345, (double) i, -92).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 345, (double) i, -93).getBlock().setType(Material.AIR);
                        Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether") , 345, (double) i, -91), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                        new Location(Bukkit.getWorld("world_nether") , 317, (double) i, -117).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 318, (double) i, -117).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 319, (double) i, -117).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 320, (double) i, -117).getBlock().setType(Material.AIR);
                        new Location(Bukkit.getWorld("world_nether") , 321, (double) i, -117).getBlock().setType(Material.AIR);
                        Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether") , 319, (double) i, -117), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                        if(i == 55){
                            enchantingLocation.getBlock().setType(Material.ENCHANTING_TABLE);
                            this.cancel();
                        } else {
                            i++;
                        }
                    }
                }.runTaskTimer(plugin, 20L,  15L);
            }
        }
        if(e.getEntity() instanceof Player){
            if(playersInArena.contains((Player) e.getEntity())){
                playersInArena.remove((Player) e.getEntity());
                BossBarManager.remove(e.getEntity().getUniqueId());
                if(playersInArena.size() == 0){
                    apollyon.remove();
                    openGate((Player) e.getEntity());
                }
            }
        }
        if(e.getEntity() instanceof WitherSkeleton) {
            if(e.getEntity().hasMetadata("StraznikApollyona")){
                e.getDrops().clear();
                int rand = ThreadLocalRandom.current().nextInt(99);
                if(rand == 0) {
                    e.getDrops().add(guardHelmet());
                }
                if(rand == 1) {
                    e.getDrops().add(guardChestplate());
                }
                if(rand == 2) {
                    e.getDrops().add(guardLeggings());
                }
                if(rand == 3) {
                    e.getDrops().add(guardBoots());
                }
            } else {
                int rand = ThreadLocalRandom.current().nextInt(19);
                if(rand == 0) {
                    e.getDrops().add(summoningItem());
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(playersInArena.contains(e.getPlayer())){
            playersInArena.remove(e.getPlayer());
            BossBarManager.remove(e.getPlayer().getUniqueId());
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 349.5, 45, -90.5));
            if(playersInArena.size() == 0){
                apollyon.remove();
                openGate(e.getPlayer());
            }
        }
    }

    public void openGate(Player player) {
        for(Entity e : player.getWorld().getEntities()){
            if(e.hasMetadata("StraznikApollyona")) {
                e.remove();
            }
        }
        Bukkit.broadcastMessage("§c§lApollyon §f§lznika...");
        new BukkitRunnable() {
            private int i = 45;

            @Override
            public void run() {
                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -89).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -90).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -91).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -92).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -93).getBlock().setType(Material.AIR);
                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 293, (double) i, -91), Sound.BLOCK_CHAIN_PLACE, 10, 0.75f);
                new Location(Bukkit.getWorld("world_nether"), 317, (double) i, -65).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 318, (double) i, -65).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -65).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 320, (double) i, -65).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 321, (double) i, -65).getBlock().setType(Material.AIR);
                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -65), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -89).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -90).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -91).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -92).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -93).getBlock().setType(Material.AIR);
                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 345, (double) i, -91), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                new Location(Bukkit.getWorld("world_nether"), 317, (double) i, -117).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 318, (double) i, -117).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -117).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 320, (double) i, -117).getBlock().setType(Material.AIR);
                new Location(Bukkit.getWorld("world_nether"), 321, (double) i, -117).getBlock().setType(Material.AIR);
                Bukkit.getWorld("world_nether").playSound(new Location(Bukkit.getWorld("world_nether"), 319, (double) i, -117), Sound.BLOCK_CHAIN_PLACE, 6, 0.75f);
                if (i == 55) {
                    enchantingLocation.getBlock().setType(Material.ENCHANTING_TABLE);
                    this.cancel();
                } else {
                    i++;
                }
            }
        }.runTaskTimer(plugin, 20L, 15L);

        playersInArena = new ArrayList<>();
        for (Location loc : campfireLocations) {
            loc.getBlock().setType(Material.SPRUCE_SLAB);
        }
        for (Location loc : fireLocations) {
            loc.getBlock().setType(Material.AIR);
        }
        for (Location loc : lavaLocations) {
            loc.getBlock().setType(Material.POLISHED_BLACKSTONE_BRICKS);
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