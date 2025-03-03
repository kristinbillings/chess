package results;

import model.GameData;

import java.util.Map;

public record ListResult(
        Map<String, GameData> games) {
}
