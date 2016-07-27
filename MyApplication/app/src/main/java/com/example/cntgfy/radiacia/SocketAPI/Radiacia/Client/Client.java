package com.example.cntgfy.radiacia.SocketAPI.Radiacia.Client;

/**
 * Created by Cntgfy on 04.07.2016.
 * Реализует сообщение двух объектов класса A.
 *
 * Назовем первым объектом тот, в котором мы находимся, вторым, тот, с которым мы связаны, тогда
 * Для того, чтобы второй объект получил значения полей первого объекта используется метод write()
 * Для того, чтобы оба объекта получили новые значения полей используется метод write(A a)
 * Для получения значений полей второго объекта используется read()
 */
public interface Client<A> {
    /*
    * передает свое состояние связанному объекту
    * */
    public void write();

    /*
    * передает состояние объекта класса А, связанному объекту
    * */
    public void write(A a);

    /*
    * передает команду связанному объекту
    * */
    public void writeCommand(String command);

    public void read();

    public void disconnect();
}