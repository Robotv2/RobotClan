package fr.robotv2.robotclan.ui;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GuiManager implements Listener {

    private static final HashMap<Class<? extends Gui>, Gui> menus = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        InventoryHolder holder = e.getInventory().getHolder();

        if(item == null) return;
        if(!(holder instanceof Gui menu)) return;

        e.setCancelled(true);
        menu.onClick(player, e.getInventory(), item, e.getRawSlot(), e.getClick());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        InventoryHolder holder = e.getInventory().getHolder();
        if(holder instanceof Gui menu) {
            menu.onClose(player, e);
        }
    }

    public void addMenu(Gui gui){
        menus.put(gui.getClass(), gui);
    }

    public void open(Player player, Class<? extends Gui> gClass, Object... objects){
        if(!menus.containsKey(gClass)) {
            throw new IllegalArgumentException("gui not registered");
        }

        Gui menu = menus.get(gClass);
        Inventory inv = Bukkit.createInventory(menu, menu.getSize(), ColorUtil.color(menu.getName(player, objects)));
        menu.startContents(player, inv, objects);

        Bukkit.getScheduler().runTaskLater(RobotClan.get(), () -> {
            player.openInventory(inv);
        }, 2L);
    }
}
