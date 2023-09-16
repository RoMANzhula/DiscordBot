
package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class App {
    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault("MTEzNjczMjU1ODQ2NjQ5NDU2NQ.G_tdyb.Ssbssd72i9HgyEBYpoCnbXSensUFiMdTurjKSk",
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new OnGuildMemberJoin()) //вітання від бота з переліком ролей на сервері
                .addEventListeners(new MessageHandler())
                .addEventListeners(new InviteHandler()) //метод, що надсилає запрошення користувачу за його айдішником
                .addEventListeners(new ChoiceRoleHandler()) //метод, що відповідає за встановлення певної ролі для користувачів
                .addEventListeners(new RoleCreationHandler())
                .setActivity(Activity.listening("Music Light"))
                .build();

        jda.awaitReady(); //чекаємо, поки бот завантажиться повністю

        //надсилаємо привітання після запуску бота
        jda.getTextChannels().get(0).sendMessage("Hello my friend!").queue();
    }

}