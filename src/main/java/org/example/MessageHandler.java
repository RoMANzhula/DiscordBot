package org.example;


import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageHandler extends ListenerAdapter {
    private String prohibited = "XXX";
    // Визначте змінну для планувальника завдань
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMember() != null && event.getChannel() != null) { //перевірка на наявність користувача

            //З ВИВОДОМ В КОНСОЛЬ
//            if (!event.getMember().isOwner()) { //якщо користувач не Адмін
//                if (event.getMessage().getContentDisplay().contains("XXX")) { //якщо користувач введе слово "XXX" в
//                    //голосовому чаті, то
//                    event.getMember().deafen(true) //глушимо його (виключаємо йому навушники)
//                            .timeout(5, TimeUnit.SECONDS) //встановлюємо таймаут на 5 секунд
//                            .submit() //запускаємо
//                            .thenAccept(v -> {
//                                System.out.println("Користувач " + event.getAuthor().getAsTag() + " був заглушений через використання слова \"XXX\".");
//                            })
//                            .exceptionally(e -> {
//                                System.err.println("Помилка при заглушенні користувача: " + e.getMessage());
//                                return null;
//                            });
//                }
//            }

            //З ВИВОДОМ В ЧАТ
            if (!event.getMember().isOwner()) { //якщо користувач не Адмін
                if (event.getMember().getVoiceState() == null || event.getMember().getVoiceState().isDeafened()) {

                    if (event.getMessage().getContentDisplay().contains(prohibited)) {

                        event.getMember().mute(true)
                                .timeout(5, TimeUnit.SECONDS)
                                .submit()
                                .thenAccept(v -> {
                                    System.out.println("Користувач " + event.getAuthor().getAsMention() +
                                            " був заглушений через використання слова \"XXX\".");

                                    event.getChannel().sendMessage("Користувач " +
                                            event.getAuthor().getAsMention() +
                                            " був заглушений через використання слова \"XXX\".").queue();

                                    //видалення повідомлення після 1 секунди
                                    scheduler.schedule(() -> event.getMessage().delete().queue(), 1, TimeUnit.SECONDS);
                                    System.out.println("Повідомлення було вдало видалено!");
                                })
                                .exceptionally(e -> {
                                    System.err.println("Помилка при заглушенні користувача: " + e.getMessage());
                                    return null;
                                });
                        event.getMember().deafen(true)
                                .submit();
                    }
                }
            }


            if (event.isFromType(ChannelType.PRIVATE)) {
                System.out.printf("[PM] %s: %s\n",
                        event.getAuthor().getName(),
                        event.getMessage().getContentDisplay()
                );
            } else {
                System.out.printf("[%s][%s] %s: %s\n",
                        event.getGuild().getName(),
                        event.getChannel(),
                        Objects.requireNonNull(event.getMember()).getEffectiveName(),
                        event.getMessage().getContentDisplay()
                );
            }

        }
    }
}
