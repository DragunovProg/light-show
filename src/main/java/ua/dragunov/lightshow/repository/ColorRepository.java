package ua.dragunov.lightshow.repository;

import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Color;
import ua.dragunov.lightshow.model.Light;

import java.util.List;

public interface ColorRepository {

    void save(Color color) throws LightShowException;

    Color findByName(String name) throws LightShowException;

    List<Color> findAll() throws LightShowException;

    void remove(Color color) throws LightShowException;
}
