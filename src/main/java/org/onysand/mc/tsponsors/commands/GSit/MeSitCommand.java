package org.onysand.mc.tsponsors.commands.GSit;

import dev.geco.gsit.api.GSitAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.onysand.mc.tsponsors.utils.Request;
import org.onysand.mc.tsponsors.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class MeSitCommand implements TabExecutor {
    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return sendFailMessage("Запрос может быть отправлен только игроком", commandSender);

        Player target = null;
        if (args.length == 0) {
            Entity targetEntity = ((Player) commandSender).getTargetEntity(6);
            if (targetEntity instanceof Player) {
                target = (Player) targetEntity;
            } else {
                player.sendMessage(Component.text("Для отправки запроса наведитесь или укажите ник игрока")
                        .color(NamedTextColor.DARK_GREEN));

                return false;
            }
        }


        if (args.length > 0 && args[0].equalsIgnoreCase("accept")) {
            Request request = MeSitRequests.getRequest(player.getUniqueId());
            if (request == null) return sendFailMessage("Не найдено запросов для принятия", player);

            Player sender = Bukkit.getPlayer(request.getSender());
            Player receiver = Bukkit.getPlayer(request.getReceiver());

            if (request.isExpired()) return sendFailMessage("Закончилось время принятия запроса", player);
            if (sender == null) return sendFailMessage("Игрок, отправивший запрос, оффлайн", player);

            Location receiverLoc = receiver.getLocation();
            Location senderLoc = sender.getLocation();

            if (receiverLoc.distance(senderLoc) > 6) return sendFailMessage("Игрок, отправивший запрос находится слишком далеко", player);
            GSitAPI.sitOnPlayer(receiver, sender);
            MeSitRequests.removeRequest(request);

            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("decline")) {
            Request request = MeSitRequests.getRequest(player.getUniqueId());
            if (request == null) return sendFailMessage("Не найдено запросов для принятия", player);

            Player sender = Bukkit.getPlayer(request.getSender());
            Player receiver = Bukkit.getPlayer(request.getReceiver());

            if (request.isExpired()) return sendFailMessage("Закончилось время принятия запроса", player);
            if (sender == null) return sendFailMessage("Игрок, отправивший запрос, оффлайн", player);

            sender.sendMessage(Component.text("Ваш запрос был отклонен")
                    .color(NamedTextColor.DARK_RED));
            receiver.sendMessage(Component.text("Вы отклонили запрос игрока " + sender.getName())
                    .color(NamedTextColor.GREEN));

            MeSitRequests.removeRequest(request);

            return true;
        }

        if (args.length == 1) target = Bukkit.getPlayer(args[0]);
        if (target == null) return Utils.returnMessage(player, "Игрок не найден", NamedTextColor.DARK_RED);
        if (target == commandSender) return Utils.returnMessage(player, "Вы не можете сесть на себя :|", NamedTextColor.DARK_RED);

        if (MeSitRequests.containsKey(target.getUniqueId())
                && !MeSitRequests.getRequest(target.getUniqueId()).isExpired()) return Utils.returnMessage(player, "Вы уже отправили запрос этому игроку!", NamedTextColor.DARK_RED);

        if (!commandSender.hasPermission("tsponsors.mesit.send")) {
            commandSender.sendMessage(Component.text("Нет доступа к данной команде").color(NamedTextColor.DARK_RED));
            return false;
        }

        if (target.getLocation().distance(player.getLocation()) > 6) return Utils.returnMessage(player, "Игрок " + args[0] + " слишком далеко", NamedTextColor.DARK_RED);

        MeSitRequests.addRequest(new Request(player.getUniqueId(), target.getUniqueId()));
        target.sendMessage(mm.deserialize("Игрок " + player.getName() + " <gray>предлагает сесть вам на свою голову.\n" +
                "     <green><click:run_command:'/mesit accept'>Принять</click></green> | <red><click:run_command:'/mesit decline'>Отменить</click></red></gray>"));
        player.sendMessage(Component.text("Вы предложили игроку " + target.getName() + " сесть на свою голову.")
                .color(NamedTextColor.GREEN));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = Arrays.asList("decline", "accept");
        List<String> returnList = commands.stream().filter(it -> it.startsWith(args[0])).toList();

        if (args.length == 1) return returnList.isEmpty() ? null : returnList;

        return null;
    }

    private boolean sendFailMessage (String message, CommandSender commandSender) {
        commandSender.sendMessage(Component.text(message).color(NamedTextColor.DARK_RED));
        return false;
    }
}
