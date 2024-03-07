package service.response;

import model.GameData;

import java.util.List;
import java.util.Set;

public record ListGamesResponse(Set<GameData> games) implements Response {
}
