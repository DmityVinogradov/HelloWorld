package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        bootstrapWorld();
        registerCommands();
    }

    private void registerCommands() {

        /*
        // Ошибка компиляции (раскомментируйте для демонстрации):
        // String s = 123; // несовместимые типы
        // Ошибка выполнения (ArithmeticException):
        // int x = 10 / 0; // деление на ноль
        Обработка исключений в игре:
        - InvalidCommandException - пользовательские ошибки (обрабатываются)
        - RuntimeException - системные ошибки (логируются)
        */

        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
        });
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));
        commands.put("move", this::UserInput);
        commands.put("take", this::TakeItem);
        commands.put("inventory", this::Inventory);
        commands.put("use", this::UseItem);
        commands.put("fight",this::Fight);
        commands.put("save", (ctx, a) -> SaveLoad.save(ctx, ctx.getAllRooms()));
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        commands.put("exit", (ctx,a) -> {
            System.out.println("Пока!");
            System.exit(0);
        });
    }

    private void Fight(GameState ctx, List<String> args) {
        Room current = ctx.getCurrent();
        Player hero = ctx.getPlayer();
        Monster monster = current.getMonster();

        if (monster == null) {
            throw new InvalidCommandException("В этой комнате нет монстров");
        }

        while (hero.getHp() > 0 && monster.getHp() > 0 ) {
            int playerDamage = hero.getAttack();
            int monsterDamage = monster.getLevel();

            monster.setHp(monster.getHp() - playerDamage);
            System.out.println("Вы бьёте " + monster.getName() + " на " + playerDamage +
                    ". HP монстра: " + Math.max(0, monster.getHp()));
            if (monster.getHp() <= 0) {
                System.out.println("Монстр побежден!");
                if (monster.getName().equalsIgnoreCase("Волк")) {
                    current.getItems().add(new Potion("Среднее зелье здоровья", 10));
                    break;
                }
                current.setMonster(null);
                ctx.addScore(10);
            }
            hero.setHp(hero.getHp() - monsterDamage);
            System.out.println("Монстр нападает в ответ, вы получаете урон -" + monsterDamage  +
                    ". Ваше HP: " + Math.max(0, hero.getHp()));
            if (hero.getHp() <= 0) {
                System.out.println("Вы погибли! Игра окончена.");
                System.exit(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void UseItem(GameState ctx, List<String> args) {
        if (args.isEmpty()) {
            throw new InvalidCommandException("Укажите название предмета");
        }
        String itemName = String.join(" ", args).trim();
        Player hero = ctx.getPlayer();

        Item foundItems = null;
        for (Item item: hero.getInventory()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                foundItems = item;
                break;
            }
        }
        if (foundItems == null) {
            throw new InvalidCommandException("Предмет не найден в инвентаре: " + itemName);
        }
        foundItems.apply(ctx);
    }

    private void Inventory(GameState ctx, List<String> args) {
        Player hero = ctx.getPlayer();
        if (hero.getInventory().isEmpty()) {
            System.out.println("Инвентарь пуст");
            return;
        }

        Map<String, List<Item>> groupItems = hero.getInventory().stream()
                .collect(Collectors.groupingBy(item -> item.getClass().getSimpleName()));
        groupItems.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String type = entry.getKey();
                    long count = entry.getValue().size();
                    String items = entry.getValue().stream()
                            .map(Item::getName)
                            .sorted()
                            .collect(Collectors.joining(", "));
                    System.out.println("- " + type + " (" + count + ") " + items);
                });
    }

    private void TakeItem(GameState ctx, List<String> args) {
        if (args.isEmpty()) {
            throw new InvalidCommandException("Укажите название предмета");
        }

        String itemName = String.join(" ", args).trim();
        Room current = ctx.getCurrent();
        Player hero = ctx.getPlayer();


        Item foundItem = null;
        for (Item item: current.getItems()){
            if (item.getName().equalsIgnoreCase(itemName)) {
                foundItem = item;
                break;
            }
        }

        if (foundItem == null) {
            if (current.getItems().isEmpty()){
                throw new InvalidCommandException("В комнате нет предметов");
            } else {
                String availableItems = current.getItems().stream()
                        .map(Item::getName)
                        .collect(Collectors.joining(", "));
                throw new InvalidCommandException("Предмет '" + itemName +
                        "' не найден. Доступные предметы: "
                        + availableItems);
            }
        }

        current.getItems().remove(foundItem);
        hero.getInventory().add(foundItem);
        System.out.println("Взято: " + foundItem.getName());

    }

    private void UserInput(GameState ctx, List<String> args) {
        if (args.isEmpty()){
            throw new InvalidCommandException("Укажите направление: north, south, east, west, hangar");
        }

        String direction = args.getFirst().toLowerCase(Locale.ROOT);
        Room current = ctx.getCurrent();
        Room next = current.getNeighbors().get(direction);

        if (next == null) {
            throw new InvalidCommandException("Нет пути в направлении " + direction);
        }

        // Проверка для ангара - смотрим, есть ли ангар среди соседей
        if (direction.equalsIgnoreCase("hangar")) {
            boolean hasKey = false;
            for (Item item : ctx.getPlayer().getInventory()) {
                if (item.getName().equalsIgnoreCase("Поржавевший ключ от Ангара")) {
                    hasKey = true;
                    break;
                }
            }

            if (!hasKey) {
                throw new InvalidCommandException("Дверь в ангар заперта. Найдите ключ от ангара.");
            } else {
                System.out.println("Вы использовали ключ от ангара и открыли дверь.");
                ctx.getPlayer().getInventory().removeIf(item ->
                        item.getName().equalsIgnoreCase("Поржавевший ключ от Ангара"));
            }
        }

        ctx.setCurrent(next);
        System.out.println("Вы перешли в: " + next.getName());
        System.out.println(next.describe());
    }

    private void bootstrapWorld() {
        Player hero = new Player("Герой", 20, 5);
        Item item;
        hero.getInventory().add(new Key("Поржавевший ключ от Ангара"));
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        Room hangar = new Room("Ангар", "Старый обшарпанный ангар, давно оставленный людьми. Вы " +
                "замечаете в дальнем углу деревянный ящик. Из него торчит рукоятка");

        square.getNeighbors().put("north", forest);
        square.getNeighbors().put("hangar", hangar);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);

        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 1, 8));
        hangar.getItems().add(new Weapon("Блястящий Вострый меч", 10));


        state.setCurrent(square);

        Map<String, Room> gameRooms = new HashMap<>();
        gameRooms.put(square.getName(), square);
        gameRooms.put(forest.getName(), forest);
        gameRooms.put(cave.getName(), cave);
        state.updateRooms(gameRooms);
    }

    public void run() {
        System.out.println("DungeonMini (TEMPLATE). 'help' — команды.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> parts = Arrays.asList(line.split("\s+"));
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}
