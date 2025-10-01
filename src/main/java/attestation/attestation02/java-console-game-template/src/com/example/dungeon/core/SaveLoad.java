package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SaveLoad {
    private static final Path SAVE = Paths.get("save.txt");
    private static final Path SCORES = Paths.get("scores.csv");
    public static void save(GameState s, Map<String, Room> allRooms) {
        try (BufferedWriter w = Files.newBufferedWriter(SAVE)) {
            Player p = s.getPlayer();


            w.write("player;" + p.getName() + ";" + p.getHp() + ";" + p.getAttack());
            w.newLine();

            w.write("hangar_unlocked;" + s.isHangarUnlocked());
            w.newLine();

            // Сохраняем инвентарь
            String inv = p.getInventory().stream()
                    .map(i -> i.getClass().getSimpleName() + ":" + i.getName())
                    .collect(Collectors.joining(","));
            w.write("inventory;" + (inv.isEmpty() ? "empty" : inv));
            w.newLine();

            // Сохраняем текущую комнату
            w.write("current_room;" + s.getCurrent().getName());
            w.newLine();

            // Сохраняем очки
            w.write("score;" + s.getScore());
            w.newLine();

            System.out.println("Сохранено в " + SAVE.toAbsolutePath());
            writeScore(p.getName(), s.getScore());

        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    public static void load(GameState s) {
        if (!Files.exists(SAVE)) {
            System.out.println("Сохранение не найдено.");
            return;
        }

        try (BufferedReader r = Files.newBufferedReader(SAVE)) {
            Map<String, String> data = new HashMap<>();

            // Читаем все данные из файла
            for (String line; (line = r.readLine()) != null; ) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                }
            }

            // Восстанавливаем игрока
            Player p = s.getPlayer();
            String playerData = data.getOrDefault("player", "Герой;20;5");
            String[] playerParts = playerData.split(";");

            if (playerParts.length >= 3) {
                p.setName(playerParts[0]);
                p.setHp(Integer.parseInt(playerParts[1]));
                p.setAttack(Integer.parseInt(playerParts[2]));
            }

            // Восстанавливаем инвентарь
            p.getInventory().clear();
            String invData = data.getOrDefault("inventory", "empty");
            if (!invData.equals("empty")) {
                for (String itemStr : invData.split(",")) {
                    String[] itemParts = itemStr.split(":");
                    Item item = createItem(itemParts[0], itemParts[1]);
                    if (item != null) {
                        p.getInventory().add(item);
                    }
                }
            }

            // Восстанавливаем текущую комнату - ВАЖНО: делаем это ДО установки игрока
            String currentRoomName = data.getOrDefault("current_room", "Площадь");
            Room currentRoom = s.getRoomByName(currentRoomName);

            if (currentRoom != null) {
                s.setCurrent(currentRoom);
                System.out.println("Загружена комната: " + currentRoomName);
            } else {
                System.out.println("Комната '" + currentRoomName + "' не найдена, используется стартовая");
            }

            // Восстанавливаем очки
            try {
                int savedScore = Integer.parseInt(data.getOrDefault("score", "0"));
                // Сбрасываем и устанавливаем сохраненные очки
                s.addScore(savedScore - s.getScore());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка загрузки очков, используется 0");
            }


            System.out.println("Игра загружена. Текущее HP: " + p.getHp() + ", очки: " + s.getScore());

        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось загрузить игру", e);
        }
    }

    // Вспомогательный метод для создания предметов
    private static Item createItem(String type, String name) {
        return switch (type) {
            case "Potion" -> new Potion(name, 5);
            case "Key" -> new Key(name);
            case "Weapon" -> new Weapon(name, 3);
            default -> null;
        };
    }
    public static void printScores() {
        if (!Files.exists(SCORES)) {
            System.out.println("Пока нет результатов.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SCORES)) {
            System.out.println("Таблица лидеров (топ-10):");
            r.lines().skip(1).map(l -> l.split(",")).map(a -> new Score(a[1], Integer.parseInt(a[2])))
                    .sorted(Comparator.comparingInt(Score::score).reversed()).limit(10)
                    .forEach(s -> System.out.println(s.player() + " — " + s.score()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    private static void writeScore(String player, int score) {
        try {
            boolean header = !Files.exists(SCORES);
            try (BufferedWriter w = Files.newBufferedWriter(SCORES, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (header) {
                    w.write("ts,player,score");
                    w.newLine();
                }
                w.write(LocalDateTime.now() + "," + player + "," + score);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    private record Score(String player, int score) {
    }
}
