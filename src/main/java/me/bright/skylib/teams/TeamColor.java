package me.bright.skylib.teams;

import me.bright.skylib.utils.Messenger;
import org.bukkit.DyeColor;

public enum TeamColor {

    BLUE('9',"Синие",11),
    RED('c',"Красные",14),
    GREEN('a',"Зеленые",13),
    YELLOW('e',"Желтые",4),
    WHITE('f',"Белые",0),
    PINK('d',"Розовые",6),
    PURPLE('5',"Пурпурные", DyeColor.PURPLE.getWoolData()),
    GRAY('7',"Серые", DyeColor.GRAY.getWoolData()),
    DARK_BLUE('1',"Темно-синие", DyeColor.BLUE.getWoolData()),
    CYAN('b',"Голубые", DyeColor.CYAN.getWoolData()),
    ORANGE('6',"Оранжевые", DyeColor.ORANGE.getWoolData()),
    BLACK('0',"Черные", DyeColor.BLACK.getWoolData());

    private char colorCode;
    private String name;
    private int durability;

    TeamColor(char colorCode, String name, int durability) {
        this.colorCode = colorCode;
        this.name = name;
        this.durability = durability;
    }

    public String getName() {
        return name;
    }

    public char getColorCode() {
        return colorCode;
    }

    public int getDurability() { return durability; }

    public String getColoredName() {
        return Messenger.color("&" + colorCode + name);
    }
}

