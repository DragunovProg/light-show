package ua.dragunov.lightshow.command;

import ua.dragunov.lightshow.command.data.CreateLightShowRequest;
import ua.dragunov.lightshow.repository.*;


public class CommandFactory {
    private final LightRepository lightRepository;
    private final ColorRepository colorRepository;
    private final ColorHistoryRecordRepository colorHistoryRecordRepository;

    public CommandFactory(LightRepository lightRepository, ColorRepository colorRepository, ColorHistoryRecordRepository colorHistoryRecordRepository) {
        this.lightRepository = lightRepository;
        this.colorRepository = colorRepository;
        this.colorHistoryRecordRepository = colorHistoryRecordRepository;
    }


    public Command<String> newLightShowCommand(CreateLightShowRequest context) {
        return new LightShowCommand(lightRepository, colorRepository, colorHistoryRecordRepository, context);
    }
}
