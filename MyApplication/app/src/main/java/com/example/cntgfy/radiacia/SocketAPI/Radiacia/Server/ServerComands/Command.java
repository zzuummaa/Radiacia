package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Server.ServerComands;

/**
 * Created by Cntgfy on 06.07.2016.
 * Содержит константы, обозначающие комманды.
 * Пример1: public static final String NAME = "name=";
 * Пример2: public static final String EXIT = "exit";
 *
 * Предполагается, что комманда может содержать аргумент, стоящий без пробела сразу после нее.
 * Тогда чтение команды из строки производится до символа '=', а аргумент либо до конца строки,
 * либо до символа пробела.
 */
public interface Command {

}
