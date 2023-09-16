package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnGuildMemberJoin extends ListenerAdapter { //клас, що вітає нового користувача, та пропонує йому обрати роль
    private Map<String, Role> availableRoles = new HashMap<>(); //карта з доступними ролями, які є на сервері

    @Override //перевизначаємо метод батьківського класу
    public void onReady(ReadyEvent event) { //метод для виконання подій, коли бот готовий до роботи
        Guild guild = event.getJDA().getGuildById("1137351938703360111"); //отримання об'єкту (сервер) за ідентифікатором
        //нашого сервера, на якому ми хочемо, щоб бот виконав дії

        if (guild != null) { //якщо об'єкт, в нашому випадку сервер за айдішником, знайдено, то
            List<Role> rolesOnServer = guild.getRoles(); //отримуємо список ролей з сервера
            updateAvailableRoles(rolesOnServer); //для даного списка застосовуємо метод для обробки ролей
        } else { //інакше
            System.out.println("Помилка: Сервер не знайдено!"); //виводимо повідомлення, що сервер не знайдено
        }
    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        //отримуємо перелік доступних ролей та їх кольорів з мапи availableRoles
        StringBuilder rolesList = new StringBuilder();
        int roleCounter = 1;
        for (String roleName : availableRoles.keySet()) {
            //пропускаємо ролі "@everyone", "bazatraineetestbot", "нова роль"
            if (roleName.equals("@everyone") || roleName.equals("bazatraineetestbot") || roleName.equals("нова роль")) {
                continue;
            }

            //додаємо роль з порядковим номером до переліку
            rolesList.append(roleCounter).append(". ").append(roleName).append("\n");
            roleCounter++;
        }

        //відправляємо привітальне повідомлення з переліком ролей
        String welcomeMessage = "Вітаємо учасника " + member.getAsMention() + " у великій родині гільдії MyTestBot!\n" +
                "Виберіть свою спеціальність:\n" +
                rolesList.toString() +
                "Напишіть вашу спеціальність у чаті, щоб обрати для вас відповідну роль у нашій родині.\n" +
                "Приклад введення вашої спеціалізації: .role роль_зі_списку";

        //отримуємо канал, на якому надсилаємо повідомлення
        List<TextChannel> textChannels = guild.getTextChannels();
        if (!textChannels.isEmpty()) {
            TextChannel defaultChannel = textChannels.get(0);
            defaultChannel.sendMessage(welcomeMessage).queue();
        }
    }

    private void updateAvailableRoles(List<Role> rolesOnServer) { //метод для оновлення доступних ролей у бота
        availableRoles.clear(); //очищаємо мапу з ролями
        //додаємо ролі з сервера та їх об'єкти до доступних ролей
        for (Role role : rolesOnServer) { //проходимо по кожному об'єкту у списку ролей на сервері
            availableRoles.put(role.getName().toLowerCase(), role); //додаємо в мапу ролі у вигляді: ключ - назва ролі у
            //нижньому регістрі, значення - сам об'єкт-роль з сервера
        }
    }
}






