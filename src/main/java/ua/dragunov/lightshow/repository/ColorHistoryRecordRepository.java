package ua.dragunov.lightshow.repository;

import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.ColorHistoryRecord;


public interface ColorHistoryRecordRepository {

    void save(ColorHistoryRecord colorHistoryRecord) throws LightShowException;

    ColorHistoryRecord findById(Long id) throws LightShowException;

    void remove(ColorHistoryRecord colorHistoryRecord) throws LightShowException;
}
