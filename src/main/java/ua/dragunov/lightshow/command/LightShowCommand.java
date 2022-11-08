package ua.dragunov.lightshow.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.dragunov.lightshow.command.data.CreateLightShowRequest;
import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Color;
import ua.dragunov.lightshow.model.ColorHistoryRecord;
import ua.dragunov.lightshow.model.Light;
import ua.dragunov.lightshow.repository.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LightShowCommand implements Command<String>{
    private static final Logger logger = LogManager.getLogger(LightShowCommand.class.getName());
    private final LightRepository lightRepository;
    private final ColorRepository colorRepository;
    private final ColorHistoryRecordRepository colorHistoryRecordRepository;

    private final CreateLightShowRequest context;

    public LightShowCommand(LightRepository lightRepository,
                            ColorRepository colorRepository,
                            ColorHistoryRecordRepository colorHistoryRecordRepository,
                            CreateLightShowRequest context) {

        this.lightRepository = lightRepository;
        this.colorRepository = colorRepository;
        this.colorHistoryRecordRepository = colorHistoryRecordRepository;
        this.context = context;
    }

    @Override
    public String execute() throws LightShowException {
        StringBuilder switchingHistory = new StringBuilder();
        List<Color> allColorsByUser = findAllColorsByUser();
        Light light = lightRepository.findByLabel(context.label());


            if (light == null) {
                light = new Light();
                light.setLabel(context.label());
                light.setColor(getRandomColor(light, allColorsByUser));
                light.setEnabled(false);
                lightRepository.save(light);
            }

            if (light.isEnabled() == true) {
                throw new LightShowException("light already enabled, please turn off");
            }

            if (context.switchingInterval() < 1) {
                throw new LightShowException("switching interval less than 1 second");
            }

            if (context.amountSwitching() < 1) {
                throw new LightShowException("amount switching less than 1 ");
            }

            light.setEnabled(true);

            switchingHistory.append(String.format("Light %s changed color '%s'", light.getLabel(), light.getColor().getName()));

            for (int i = 0; i < context.amountSwitching(); i++) {
                try {
                    Color randomColor = getRandomColor(light, allColorsByUser);
                    ColorHistoryRecord colorHistoryRecord = new ColorHistoryRecord();
                    colorHistoryRecord.setLight(light);
                    colorHistoryRecord.setOldColor(light.getColor());
                    colorHistoryRecord.setNewColor(randomColor);
                    colorHistoryRecord.setChangedAt(Instant.now());

                    light.setColor(randomColor);
                    switchingHistory.append(String.format(" => '%s'", light.getColor().getName()));
                    colorHistoryRecordRepository.save(colorHistoryRecord);
                    logger.info(String.format("Light '%s' changed color from ‘%s’ to ‘%s’ at %s", light.getLabel()
                            , colorHistoryRecord.getOldColor().getName()
                            , colorHistoryRecord.getNewColor().getName()
                            , Instant
                                    .now().toString()));

                    Thread.sleep(context.switchingInterval() * 1000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            light.setEnabled(false);
            lightRepository.save(light);

            return switchingHistory.toString();

    }

    List<Color> findAllColorsByUser() throws LightShowException {
        List<Color> allColors = colorRepository.findAll();
        List<Color> colorsByUser = new ArrayList<>();
        for (String colorName : context.colorList()) {
            colorsByUser.add(allColors.stream()
                    .filter(color -> color.getName().equals(colorName))
                    .findFirst()
                    .orElseThrow(() -> new LightShowException(String.format("Color name %s does not exist!!", colorName))));
        }

        return colorsByUser;
    }

    Color getRandomColor(Light light, List<Color> colors) {
        List<Color> randomColors = new ArrayList<>(colors);
        randomColors.remove(light.getColor());
        return colors.get(ThreadLocalRandom.current().nextInt(colors.size()));
    }
}
