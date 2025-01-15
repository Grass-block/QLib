package org.atcraftmc.qlib.command.select;

import org.atcraftmc.qlib.command.execute.CommandExecution;
import org.atcraftmc.qlib.command.execute.CommandSuggestion;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public interface EntitySelector {
    List<String> SELECTOR_LIMITS = List.of("dx=", "dy=", "dz=", "world=", "level=", "type=", "level=", "gamemode=", "radius=");

    static Pair<String, Map<String, String>> extract(String exp) {
        if (!exp.matches("@[aeprs](\\[.*]*)?")) {
            return null;
        }

        var result = new Pair<String, Map<String, String>>(exp.substring(0, 2), new HashMap<>());

        if (exp.length() > 2) {
            Arrays.stream(exp.substring(3, exp.length() - 1).split(",")).forEach((s) -> {
                var spl = s.split("=");
                result.getRight().put(spl[0], spl[1]);
            });
        }

        return result;
    }

    static Collection<CommandSender> filter(Collection<CommandSender> input, Map<String, String> limits, Location position) {
        var stream = input.stream();

        if (limits.containsKey("name")) {
            stream = stream.filter((e) -> e.getName().equals(limits.get("name")));
        }

        if (limits.containsKey("tags")) {
            stream = stream.filter((e) -> ((Entity) e).getScoreboardTags().contains(limits.get("tags")));
        }

        if (limits.containsKey("gamemode")) {
            stream = stream.filter((e) -> e instanceof Player && ((Player) e).getGameMode() == GameMode.valueOf(limits.get("gamemode")
                                                                                                                        .toUpperCase()));
        }

        if (limits.containsKey("level")) {
            stream = stream.filter((e) -> e instanceof Player && ((Player) e).getExpToLevel() == Integer.parseInt(limits.get("level")));
        }

        if (limits.containsKey("dx")) {
            stream = stream.filter((e) -> {
                var v = ((Entity) e).getLocation().getX();
                var d = Float.parseFloat(limits.get("dx"));
                var cv = position.getX();

                return d > 0 ? v >= cv && v <= cv + d : v <= cv && v >= cv + d;
            });
        }
        if (limits.containsKey("dy")) {
            stream = stream.filter((e) -> {
                var v = ((Entity) e).getLocation().getY();
                var d = Float.parseFloat(limits.get("dy"));
                var cv = position.getY();

                return d > 0 ? v >= cv && v <= cv + d : v <= cv && v >= cv + d;
            });
        }

        if (limits.containsKey("dz")) {
            stream = stream.filter((e) -> {
                var v = ((Entity) e).getLocation().getZ();
                var d = Float.parseFloat(limits.get("dz"));
                var cv = position.getZ();

                return d > 0 ? v >= cv && v <= cv + d : v <= cv && v >= cv + d;
            });
        }

        return stream.collect(Collectors.toList());
    }

    static Collection<CommandSender> dispatchSelector(String exp, CommandSender caller, Location position) {
        var selector = extract(exp);

        if (selector == null) {//specific players
            return Set.of(Objects.requireNonNull(Bukkit.getPlayerExact(exp)));
        }

        var baseList = new ArrayList<CommandSender>();
        var range = selector.getLeft();
        var limits = selector.getRight();

        var worlds = new ArrayList<World>();

        var typeLimit = limits.containsKey("type") ? EntityType.valueOf(limits.get("type").toUpperCase()) : null;
        var distanceLimit = limits.containsKey("radius") ? Float.parseFloat(limits.get("radius")) : -1F;

        var isCallerPlayer = caller instanceof Player;
        var hasType = typeLimit != null;
        var hasDistance = distanceLimit != -1F;

        if (limits.containsKey("world")) {
            var world = Bukkit.getWorld(limits.get("world"));

            if (world != null) {
                worlds.add(world);
            }
        } else {
            worlds.addAll(Bukkit.getWorlds());
        }

        //pre-process limits
        if (Objects.equals(range, "@s")) {
            if (!isCallerPlayer) {
                throw new IllegalArgumentException("'@s' must be called by a player");
            }
            return filter(List.of(caller), limits, position);
        }

        if (limits.containsKey("uuid")) {
            var e = Bukkit.getEntity(UUID.fromString(limits.get("uuid")));

            if (e == null) {
                return List.of();
            }

            return filter(List.of(e), limits, position);
        }

        if (Objects.equals(range, "@r")) {
            var list = filter(baseList, limits, position);

            if (list.isEmpty()) {
                return list;
            }

            return List.of(new ArrayList<>(list).get(new Random().nextInt(0, list.size())));
        }

        if (Objects.equals(range, "@e")) {

            for (var world : worlds) {
                if (hasDistance && hasType) {
                    baseList.addAll(position.getNearbyEntitiesByType(typeLimit.getEntityClass(), distanceLimit));
                } else if (hasType) {
                    baseList.addAll(world.getEntitiesByClass(Objects.requireNonNull(typeLimit.getEntityClass())));
                } else if (hasDistance) {
                    baseList.addAll(position.getNearbyEntities(distanceLimit, distanceLimit, distanceLimit));
                } else {
                    baseList.addAll(world.getEntities());
                }
            }
        }

        if (Objects.equals(range, "@a")) {
            for (var world : worlds) {
                if (hasDistance) {
                    baseList.addAll(position.getNearbyPlayers(distanceLimit, distanceLimit, distanceLimit));
                } else {
                    baseList.addAll(world.getPlayers());
                }
            }
        }

        return filter(baseList, limits, position);
    }

    static Collection<CommandSender> selectEntity(CommandExecution execution, int position) {
        var sender = execution.getSender();
        var location =
                sender instanceof Player p ? p.getLocation()
                        : sender instanceof BlockCommandSender b ? b.getBlock().getLocation() :
                        new Location(Bukkit.getWorlds().get(0), 0, 0, 0);

        return dispatchSelector(execution.requireArgumentAt(position), sender, location);
    }

    static void tab(CommandSuggestion suggestion, int pos) {
        List<String> args = suggestion.getBuffer();

        if (pos >= args.size()) {
            suggestion.suggest(pos, "@a", "@e", "@p", "@r", "@s");
            suggestion.suggest(pos, Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
            return;
        }

        var arg = args.get(pos);

        if (arg.length() < 2) {
            suggestion.suggest(pos, "@a", "@e", "@p", "@r", "@s");
            suggestion.suggest(pos, Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet()));
            return;
        }

        var preFetch = arg.lastIndexOf("=");
        var lastSplit = arg.lastIndexOf(",");

        var prev = arg.substring(0, Math.max(preFetch, lastSplit) + 1);

        if (arg.endsWith("[") || lastSplit > preFetch) {
            suggestion.suggest(pos, SELECTOR_LIMITS.stream().map(m -> prev + m).collect(Collectors.toSet()));
            return;
        }

        if (arg.matches(".*world=[a-z]*")) {
            var vals = Bukkit.getWorlds().stream().map(w -> prev + w.getName()).collect(Collectors.toSet());
            suggestion.suggest(pos, vals);
            return;
        }

        if (arg.matches(".*type=[a-z]*")) {
            var vals = Arrays.stream(EntityType.values()).map((t) -> prev + t.name().toLowerCase()).collect(Collectors.toSet());
            suggestion.suggest(pos, vals);
            return;
        }

        if (arg.matches(".*gamemode=[a-z]*")) {
            var vals = Arrays.stream(GameMode.values()).map((t) -> prev + t.name().toLowerCase()).collect(Collectors.toSet());
            suggestion.suggest(pos, vals);
        }
    }

    record Pair<T1, T2>(T1 getLeft, T2 getRight) {
    }
}
