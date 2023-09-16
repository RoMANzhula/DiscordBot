package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleCreationHandler extends ListenerAdapter {
    private final Map<String, Color> availableColors = new HashMap<>();

    public RoleCreationHandler() {
        availableColors.put("жовтий", Color.YELLOW);
        availableColors.put("рожевий", Color.PINK);
        availableColors.put("оранжевий", Color.ORANGE);
        availableColors.put("червоний", Color.RED);
        availableColors.put("чорний", Color.BLACK);
        availableColors.put("синій", Color.BLUE);
        availableColors.put("зелений", Color.GREEN);
        availableColors.put("сірий", Color.GRAY);
        availableColors.put("білий", Color.WHITE);
        availableColors.put("фіолетовий", Color.MAGENTA);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay().toLowerCase();

        if (content.startsWith(".createrole")) {
            String[] words = content.split(" ");
            if (words.length >= 3) {
                String roleName = words[1];
                String colorName = words[2];

                if (!availableColors.containsKey(colorName)) {
                    event.getChannel().sendMessage("Колір " + colorName + " не підтримується. Доступні кольори: " +
                            String.join(", ", availableColors.keySet())).queue();
                    return;
                }

                Guild guild = event.getGuild();
                List<Role> existingRoles = guild.getRolesByName(roleName, true);

                if (existingRoles.isEmpty()) {
                    guild.createRole()
                            .setName(roleName)
                            .setColor(availableColors.get(colorName))
                            .queue(role -> {
                                event.getChannel().sendMessage("Створено роль " + roleName +
                                        " з коліром " + colorName).queue();

                                // Додавання ролі до RoleManager
                                RoleManager.addRole(roleName, role);
                            });
                } else {
                    event.getChannel().sendMessage("Роль з іменем " + roleName + " вже існує.").queue();
                }
            }
        }
    }
}
