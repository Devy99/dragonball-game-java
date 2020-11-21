package it.unimol.dragon_ball.app.logic;


import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CharacterTest {
    private Character character;

    CharacterTest() {
        character = null;

        String propertiesFilepath = "res/resources/file_properties/goku.properties";

        try (InputStream inputStream1 = new FileInputStream(propertiesFilepath)) {
            Properties characterProperties = new Properties();
            characterProperties.load(inputStream1);
            character = new Character(characterProperties);
        } catch (IOException ignored) {

        }
    }

    @Test
    void characterPropertiesTest() {
        assert character.getCanonicalName().equals("goku");
        assertEquals(5, character.getMAX_EVOLUTIONS_NUMBER());

        assertEquals(900, character.getActualHP());
        assertEquals(210, character.getHpIncreaseRate());
        assertEquals(800, character.getActualMaxMp());
        assertEquals(150, character.getMpIncreaseRate());
        assertEquals(4, character.getAtk());
        assertEquals(3, character.getAtkIncreaseRate());
        assertEquals(8, character.getSpeed());
    }

    @Test
    void skillDamageDealtTest(){
        character.addNewSkill(new Skill("TestSkill",character,50,100,20));
        assertEquals(54,character.getSkills().get(0).getDamageDealt());
    }

    @Test
    void resetAuraTest() {
        character.setAura(50);
        assertEquals(50, character.getAura());
        character.resetAura();
        assertEquals(1, character.getAura());
    }

    @Test
    void characterEvolutionTest() {
        character.evolve();
        assertEquals(1110, character.getActualHP());
        assertEquals(950, character.getActualMaxMp());
        assertEquals(7, character.getAtk());
    }


}