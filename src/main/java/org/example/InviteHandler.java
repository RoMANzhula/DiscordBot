package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InviteHandler extends ListenerAdapter { //клас, який відповідає за запрошення користувача за його айдішником
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) { //метод, що читає всі повідомлення в Діскорді

        String message = event.getMessage().getContentRaw(); //у строкову змінну читаємо всі повідомлення з сервера

        // Перевіряємо, чи є команда для надсилання запрошення
        if (message.startsWith("!sendInvite")) { //якщо повідомлення починається з "!sendInvite", то

            String[] commandArgs = message.split(" "); //розбиваємо строку за допомогою пробілу на елементи і кладемо в масив
            if (commandArgs.length == 2) { //якщо масив складається з двох елементів (команда та номер айдішника), то

                String userId = commandArgs[1]; //отримуємо ID користувача

                //знаходимо користувача за ID
                event.getJDA().retrieveUserById(userId).queue(
                        user -> {
                            //користувач знайдений, виконуємо дії з ним
                            //отримуємо приватний чат користувача
                            user.openPrivateChannel().queue(
                                    privateChannel -> {
                                        //отримуємо об'єкт Guild
                                        Guild guild = event.getGuild();
                                        //отримуємо посилання на запрошення сервера
                                        Objects.requireNonNull(guild.getDefaultChannel()).createInvite().queue(
                                                invite -> {
                                                    //отримали посилання на запрошення
                                                    //відправляємо його у приватний чат користувача
                                                    privateChannel.sendMessage("Вас запрошують на сервер! Приєднуйтесь за посиланням: " + invite.getUrl()).queue(
                                                            success -> {
                                                                //надсилаємо підтвердження у загальний чат
                                                                event.getChannel().sendMessage("Запрошення було надіслано користувачу з ID " + userId).queue();
                                                            },
                                                            error -> {
                                                                //помилка надсилання запрошення
                                                                event.getChannel().sendMessage("Помилка надсилання запрошення користувачу з ID " + userId).queue();
                                                            }
                                                    );
                                                },
                                                error -> {
                                                    //помилка створення посилання на запрошення
                                                    event.getChannel().sendMessage("Помилка створення посилання на запрошення для сервера.").queue();
                                                }
                                        );
                                    },
                                    error -> {
                                        //помилка відкриття приватного чату
                                        event.getChannel().sendMessage("Не вдалося відкрити приватний чат з користувачем з ID " + userId).queue();
                                    }
                            );
                        },
                        error -> {
                            //помилка пошуку користувача
                            event.getChannel().sendMessage("Користувача з ID " + userId + " не знайдено.").queue();
                        }
                );
            } else {
                //неправильний формат команди, надсилаємо повідомлення з інструкцією
                event.getChannel().sendMessage("Неправильний формат команди! Використовуйте !sendInvite <userID>").queue();
            }
        }
    }
}
