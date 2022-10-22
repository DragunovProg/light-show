package ua.dragunov.lightshow.command;

import ua.dragunov.lightshow.command.data.CreateLightShowRequest;
import ua.dragunov.lightshow.repository.ColorHistoryRecordRepositoryImpl;
import ua.dragunov.lightshow.repository.ColorRepositoryImpl;
import ua.dragunov.lightshow.repository.LightRepositoryImpl;

public class CommandFactory {
    private final LightRepositoryImpl lightRepository;
    private final ColorRepositoryImpl colorRepository;
    private final ColorHistoryRecordRepositoryImpl colorHistoryRecordRepository;

    public CommandFactory(LightRepositoryImpl lightRepository, ColorRepositoryImpl colorRepository, ColorHistoryRecordRepositoryImpl colorHistoryRecordRepository) {
        this.lightRepository = lightRepository;
        this.colorRepository = colorRepository;
        this.colorHistoryRecordRepository = colorHistoryRecordRepository;
    }


    public Command<String> newLightShowCommand(CreateLightShowRequest context) {
        return new LightShowCommand(lightRepository, colorRepository, colorHistoryRecordRepository, context);
    }
}
