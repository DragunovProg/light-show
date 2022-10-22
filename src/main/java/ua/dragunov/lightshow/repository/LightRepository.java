package ua.dragunov.lightshow.repository;

import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Light;

import java.util.List;

public interface LightRepository {

    void save(Light light) throws LightShowException;

    Light findByLabel(String label) throws LightShowException;

    void update(Light light) throws LightShowException;

    void remove(Light light) throws LightShowException;
}
