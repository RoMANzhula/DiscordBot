package org.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MuteAllHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //беремо строку введену користувачем за допомогою пробілу і
        if (event.getMessage().getContentDisplay().split(" ")[0].equalsIgnoreCase(".muteAll")) { //якщо
            //перша частина речення буде = ".muteAll", то
            event.getJDA().getCategories().forEach( //беремо об'єкт getJDA та всі його категорії
                    category -> // і для кожної категорії
                            category.getVoiceChannels().forEach(voiceChannel -> //проходимо по всім голосовим каналам
                                    voiceChannel.getMembers().forEach(member -> //і для кожного голосового каналу
                                            member.mute(true).timeout(5, TimeUnit.SECONDS).submit()))); //відключаємо звук
            //в даному коді можно замінити команду mute на - deafen(глушимо), ban(банимо), kick(видаляємо)
        }
    }
}
