package com.example.dungeon.model;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private Player player;
    private Room current;
    private int score;
    private boolean hangarUnlocked = false; // ДОБАВИТЬ ЭТО
    private final Map<String, Room> rooms = new HashMap<>(); // И ЭТО ТОЖЕ

    public Player getPlayer() {
        return player;
    }
    public boolean isHangarUnlocked() {
        return hangarUnlocked;
    }

    public void setHangarUnlocked(boolean unlocked) {
        this.hangarUnlocked = unlocked;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Room getCurrent() {
        return current;
    }

    public void setCurrent(Room r) {
        this.current = r;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int d) {
        this.score += d;
    }
    public Map<String, Room> getAllRooms() {
        return rooms;
    }

    public void updateRooms(Map<String, Room> newRooms) {
        rooms.clear();
        rooms.putAll(newRooms);
    }

    public Room getRoomByName(String name) {
        return rooms.get(name);
    }
}
