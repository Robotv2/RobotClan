package fr.robotv2.robotclan.command;

import fr.robotv2.robotclan.RobotClan;
import fr.robotv2.robotclan.manager.ClanManager;
import fr.robotv2.robotclan.objects.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Command({"clan", "robotclan"})
public class ClanChatCommand implements Listener {

    public ClanChatCommand() {
        Bukkit.getServer().getPluginManager().registerEvents(this, RobotClan.get());
    }

    @Dependency
    private ClanManager clanManager;

    private final List<UUID> chat = new ArrayList<>();

    private void toggleClanChat(Player player) {

        final UUID playerUUID = player.getUniqueId();
        final String mode = chat.contains(playerUUID) ? ChatColor.RED + "OFF" : ChatColor.GREEN + "ON";

        if(chat.contains(playerUUID)) {
            chat.remove(playerUUID);
        } else {
            chat.add(playerUUID);
        }

        player.sendMessage(ChatColor.GREEN + "Clan Chat Activated: " + mode);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();
        final String message = event.getMessage();

        if(this.chat.contains(player.getUniqueId()) && this.clanManager.hasClan(player)) {
            event.setCancelled(true);
            Objects.requireNonNull(this.clanManager.getClan(player)).chat(player, message);
        }
    }

    @Subcommand("chat")
    @Usage("chat [<message>]")
    public void onChat(BukkitCommandActor actor, @Optional String message) {
        final Player player = actor.requirePlayer();

        if(message == null) {
            this.toggleClanChat(player);
            return;
        }

        if(!clanManager.hasClan(player)) {
            actor.reply(ChatColor.RED + "You aren't in any clan.");
            return;
        }

        final Clan clan = Objects.requireNonNull(clanManager.getClan(player));
        clan.chat(player, message);
    }
}
