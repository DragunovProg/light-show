package ua.dragunov.lightshow.command;

import ua.dragunov.lightshow.exceptions.LightShowException;

public interface Command<T> {

    T execute() throws LightShowException;
}
