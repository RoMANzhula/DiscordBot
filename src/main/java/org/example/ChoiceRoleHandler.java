package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class ChoiceRoleHandler extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById("Here ID for server where you want bot working");

        if (guild != null) {
            List<Role> rolesOnServer = guild.getRoles();
            RoleManager.initializeRoles(rolesOnServer);
        } else {
            System.out.println("Помилка: Сервер не знайдено!");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay().toLowerCase();

        if (content.startsWith(".role")) {
            String[] words = content.split(" ");
            if (words.length >= 2) {
                String roleName = words[1];

                if (RoleManager.getAvailableRoles().containsKey(roleName)) {
                    Role targetRole = RoleManager.getAvailableRoles().get(roleName);
                    Member member = event.getMember();
                    List<Role> currentRoles = member.getRoles();

                    for (Role currentRole : currentRoles) {
                        if (RoleManager.getAvailableRoles().containsValue(currentRole)) {
                            event.getGuild().removeRoleFromMember(member, currentRole).queue();
                        }
                    }

                    event.getGuild().addRoleToMember(member, targetRole).queue();
                    targetRole.getManager().setColor(targetRole.getColor()).queue();

                    event.getChannel().sendMessage("Ви вибрали роль " + targetRole.getName() + " з коліром " +
                            targetRole.getColor() + "!").queue();
                } else {
                    event.getChannel().sendMessage("Роль з іменем " + roleName + " не знайдено.").queue();
                }
            }
        }
    }
}
