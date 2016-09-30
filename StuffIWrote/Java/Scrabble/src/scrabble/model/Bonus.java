package scrabble.model;

import javafx.scene.paint.Color;

/**
 * 9/29/16 10:25 PM
 */
class Bonus {

    final int factor;
    final boolean word;
    final Color color;

    Bonus(int factor, boolean word) {
        this.factor = factor;
        this.word = word;
        // @formatter:off
        this.color = word
                     ? factor == 2 ? Color.PINK : Color.SALMON
                     : factor == 2 ? Color.LIGHTBLUE : Color.BLUE;
        // @formatter:on
    }
}

