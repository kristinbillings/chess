package results;

import model.GameData;

import java.util.Map;

public record ListResult(
        Map<Integer, GameData> games) {
}
