package ua.dragunov.lightshow.command.data;

public record CreateLightShowRequest(
        String label,
        String[] colorList,
        int switchingInterval,
        int amountSwitching
) {
}
