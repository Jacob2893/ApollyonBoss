package apollyon.apollyon;;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.UUID;

public class BossBarManager {

    private static HashMap<UUID, BossBar> map = new HashMap<UUID, BossBar>();


    /**
     *
     * @param playerUUID
     * @param title
     * @param color
     * @param style
     * @return Ustawia bossbar dla gracza z tytulem, kolorem i stylem
     */
    public static void setMessage(UUID playerUUID, String title, BarColor color, BarStyle style, double progress){
        Bukkit.getPlayer(playerUUID);
        if(map.containsKey(playerUUID)){
            map.get(playerUUID).setColor(color);
            map.get(playerUUID).setStyle(style);
            map.get(playerUUID).setTitle(ChatColor.translateAlternateColorCodes('&', title));
            map.get(playerUUID).setProgress(progress);
        }else{
            BossBar bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', title), color, style);
            map.put(playerUUID, bar);
            bar.addPlayer(Bukkit.getPlayer(playerUUID));
        }
    }

    /**
     *
     * @param playerUUID
     * @param title
     * @return Ustawia bossbar dla gracza z tytułem
     *
     */
    public static void setMessage(UUID playerUUID, String title){
        Bukkit.getPlayer(playerUUID);
        if(map.containsKey(playerUUID)){
            map.get(playerUUID).setTitle(ChatColor.translateAlternateColorCodes('&', title));
        }else{
            BossBar bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', title), BarColor.BLUE, BarStyle.SOLID);
            map.put(playerUUID, bar);
            bar.addPlayer(Bukkit.getPlayer(playerUUID));
        }
    }


    /**
     *
     * @param playerUUID
     * @retur Usuwa bossbar graczu | Do PlayerQuitEvent to dodaj
     */
    public static void remove(UUID playerUUID){
        if(Bukkit.getPlayer(playerUUID)==null){
            map.remove(playerUUID);
        }else{
            if(map.containsKey(playerUUID)){
                map.get(playerUUID).removePlayer(Bukkit.getPlayer(playerUUID));
                map.remove(playerUUID);
            }
        }
    }

    /**
     *
     * @retur Usuwa bossbar wszystkim graczą | Przydatnę do wyłączania pluginu lub reloadu
     */
    public static void removeAll(){
        for(UUID id : map.keySet()){
            remove(id);
        }
    }
}
