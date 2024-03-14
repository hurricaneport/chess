package model.response;

import model.GameData;

import java.util.Set;

public record ListGamesResponse(Set<GameData> games) implements Response {
}
