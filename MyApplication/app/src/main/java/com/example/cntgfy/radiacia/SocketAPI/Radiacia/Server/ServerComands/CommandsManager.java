package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

import java.util.Arrays;

/**
 * Created by Cntgfy on 04.07.2016.
 * Позволяет преобразовывать класс GameObject в строку и обратно.
 */
public abstract class CommandsManager<A> {
    public String getCommandWithArgument(String command, double argument) {
        return command + String.valueOf(argument);
    }

    public void performCommand(String commandWithArgument, A a) {
        char[] cWA = commandWithArgument.toCharArray();
        performCommand(cWA, a);
    }

    /*
    * Устанавливает значения поля по переданной команде
    * */
    public void performCommand(char[] commandWithArgument, A a) {
        int endOfCommand = endOfCommand(commandWithArgument, 0);

        String command = String.valueOf(commandWithArgument, 0, endOfCommand);
        String argument = String.valueOf(commandWithArgument, endOfCommand, commandWithArgument.length - endOfCommand);

        performCommand(command, argument, a);
    }

    /*
    * Реализует передачу аргументов в объект а в зависимости от команды
    *
    * @return false если комманда не известна
    *          true если комманда известна
    * */
    public abstract boolean performCommand(String command, String argument, A a);

    /*
    * Устанавливает значения полей, по переданной строке, содержащей команды
    * */
    public void performCommands(String commandsWithArgument, A a) {
        char[] csWA = commandsWithArgument.toCharArray();
        int endOfCommand = -1;

        while (endOfCommand < csWA.length) {
            int beginOfCommand = endOfCommand + 1;
            endOfCommand = endOfCommandWithArgument(csWA, beginOfCommand);
            char[] commandWithArgument = Arrays.copyOfRange(csWA, beginOfCommand, endOfCommand);
            performCommand(commandWithArgument, a);
        }
    }

    /*
    * Находит конец команды по символу '=' и возвращает позицию следующего символа
    * */
    private int endOfCommand(char[] commandWithArgument, int beginOfCommand) {
        int i;
        for (i = beginOfCommand; i < commandWithArgument.length; i++) {
            if (commandWithArgument[i] == '=') return ++i;
        }
        return i;
    }

    /*
    * Находит конец команды вместе с аргуметом по символу ' ' и возвращает позицию ' '
    * */
    private int endOfCommandWithArgument(char[] commandWithArgument, int beginOfCommand) {
        int i;
        for (i = beginOfCommand; i < commandWithArgument.length; i++) {
            if (commandWithArgument[i] == ' ') break;
        }

        return i;
    }
}
