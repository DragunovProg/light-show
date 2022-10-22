package ua.dragunov.lightshow.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.dragunov.lightshow.command.data.CreateLightShowRequest;
import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Color;
import ua.dragunov.lightshow.model.ColorHistoryRecord;
import ua.dragunov.lightshow.model.Light;
import ua.dragunov.lightshow.repository.ColorHistoryRecordRepositoryImpl;
import ua.dragunov.lightshow.repository.ColorRepositoryImpl;
import ua.dragunov.lightshow.repository.LightRepositoryImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LightShowCommand implements Command<String>{
    private static final Logger logger = LogManager.getLogger(LightShowCommand.class.getName());
    private final LightRepositoryImpl lightRepository;
    private final ColorRepositoryImpl colorRepository;
    private final ColorHistoryRecordRepositoryImpl colorHistoryRecordRepository;

    private final CreateLightShowRequest context;

    public LightShowCommand(LightRepositoryImpl lightRepository,
                            ColorRepositoryImpl colorRepository,
                            ColorHistoryRecordRepositoryImpl colorHistoryRecordRepository,
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

                    Thread.sleep(context.switchingInterval() * 1000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            light.setEnabled(false);
            lightRepository.update(light);

            return switchingHistory.toString();

    }

    List<Color> findAllColorsByUser() throws LightShowException {
        String[] colorNames = context.colorList().trim().split(",");
        List<Color> allColors = colorRepository.findAll();
        List<Color> colorsByUser = new ArrayList<>();
        for (String colorName : colorNames) {
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
