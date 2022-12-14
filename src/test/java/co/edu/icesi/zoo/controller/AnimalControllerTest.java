package co.edu.icesi.zoo.controller;

import co.edu.icesi.zoo.constant.AnimalGender;
import co.edu.icesi.zoo.dto.AnimalDTO;
import co.edu.icesi.zoo.error.exception.AnimalError;
import co.edu.icesi.zoo.error.exception.AnimalException;
import co.edu.icesi.zoo.model.Animal;
import co.edu.icesi.zoo.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalControllerTest {

    private static final String ANIMAL_NAME = "Luna";
    private static final AnimalGender ANIMAL_SEX = AnimalGender.F;
    private static final int ANIMAL_AGE = 10;
    private static final double ANIMAL_HEIGHT = 2;
    private static final double ANIMAL_WEIGHT = 150;
    private static final LocalDateTime ANIMAL_ARRIVAL_DATE = LocalDateTime.of(2002, 11, 13, 0, 0, 0);

    private AnimalController animalController;
    private AnimalService animalService;

    @BeforeEach
    public void init() {
        animalService = mock(AnimalService.class);
        animalController = new AnimalController(animalService);
    }

    @Test
    public void nameFieldNull() {
        try {
            AnimalDTO animalWithoutName = AnimalDTO.builder().sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithoutName);
            fail();
        } catch (AnimalException e) {
            nullFieldValidationException(e);
        }
    }

    @Test
    public void sexFieldNull() {
        try {
            AnimalDTO animalWithoutSex = AnimalDTO.builder().name(ANIMAL_NAME).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithoutSex);
            fail();
        } catch (AnimalException e) {
            nullFieldValidationException(e);
        }
    }

    @Test
    public void ageFieldNull() {
        try {
            AnimalDTO animalWithoutAge = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithoutAge);
            fail();
        } catch (AnimalException e) {
            nullFieldValidationException(e);
        }
    }

    @Test
    public void heightFieldNull() {
        try {
            AnimalDTO animalWithoutHeight = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithoutHeight);
            fail();
        } catch (AnimalException e) {
            nullFieldValidationException(e);
        }
    }

    @Test
    public void weightFieldNull() {
        try {
            AnimalDTO animalWithoutWeight = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithoutWeight);
            fail();
        } catch (AnimalException e) {
            nullFieldValidationException(e);
        }
    }

    @Test
    public void arrivalDateFieldNull() {
        try {
            AnimalDTO animalWithoutArrivalDate = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).build();
            animalController.createAnimal(animalWithoutArrivalDate);
            fail();
        } catch (AnimalException e) {
            nullFieldValidationException(e);
        }
    }

    private void nullFieldValidationException(AnimalException e) {
        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        assertNotNull(e.getError());
        AnimalError error = e.getError();
        assertEquals("All fields are required except father's and mother's animal", error.getMessage());
        assertEquals("CODE_01", error.getCode().name());
    }

    @Test
    public void fatherFieldNotExist() {
        try {
            String fatherToFail = "notExist";
            when(animalService.getAnimal(fatherToFail)).thenReturn(null);
            AnimalDTO animalWithNonExistenceFather = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).father(fatherToFail).build();
            animalController.createAnimal(animalWithNonExistenceFather);
            fail();
        } catch (AnimalException e) {
            parentsFieldValidationException(e);
        }
    }

    @Test
    public void motherFieldNotExist() {
        try {
            String motherToFail = "notExist";
            when(animalService.getAnimal(motherToFail)).thenReturn(null);
            AnimalDTO animalWithNonExistenceMother = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).mother(motherToFail).build();
            animalController.createAnimal(animalWithNonExistenceMother);
            fail();
        } catch (AnimalException e) {
            parentsFieldValidationException(e);
        }
    }

    private void parentsFieldValidationException(AnimalException e) {
        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        assertNotNull(e.getError());
        AnimalError error = e.getError();
        assertEquals("Animal father or mother does not exists", error.getMessage());
        assertEquals("CODE_05", error.getCode().name());
    }

    @Test
    public void createAnimalWithoutParents() {
        AnimalDTO animalWithoutParents = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        animalController.createAnimal(animalWithoutParents);
        verify(animalService, times(1)).createAnimal(any());
    }

    @Test
    public void createAnimalWithOnlyMother() {
        Animal mother = Animal.builder().name("Lupe").sex(AnimalGender.F).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        when(animalService.getAnimal(mother.getName())).thenReturn(mother);
        AnimalDTO animalWithOnlyMother = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).mother(mother.getName()).build();
        animalController.createAnimal(animalWithOnlyMother);
        verify(animalService, times(1)).createAnimal(any());
    }

    @Test
    public void createAnimalWithOnlyFather() {
        Animal father = Animal.builder().name("Oscar").sex(AnimalGender.M).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        when(animalService.getAnimal(father.getName())).thenReturn(father);
        AnimalDTO animalWithOnlyFather = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).father(father.getName()).build();
        animalController.createAnimal(animalWithOnlyFather);
        verify(animalService, times(1)).createAnimal(any());
    }

    @Test
    public void createAnimalWithParents() {
        Animal mother = Animal.builder().name("Lupe").sex(AnimalGender.F).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        Animal father = Animal.builder().name("Oscar").sex(AnimalGender.M).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        when(animalService.getAnimal(mother.getName())).thenReturn(mother);
        when(animalService.getAnimal(father.getName())).thenReturn(father);
        AnimalDTO animalWithBothParents = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).mother(mother.getName()).father(father.getName()).build();
        animalController.createAnimal(animalWithBothParents);
        verify(animalService, times(1)).createAnimal(any());
    }

    @Test
    public void fatherFieldFemaleSex() {
        Animal female = Animal.builder().name("Luna").sex(AnimalGender.F).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        when(animalService.getAnimal(female.getName())).thenReturn(female);
        try {
            String fatherToFail = female.getName();
            AnimalDTO animalWithFemaleFather = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).father(fatherToFail).build();
            animalController.createAnimal(animalWithFemaleFather);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The field father need to be a male", error.getMessage());
            assertEquals("CODE_02", error.getCode().name());
        }
    }

    @Test
    public void motherFieldMaleSex() {
        Animal male = Animal.builder().name("Oscar").sex(AnimalGender.M).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
        when(animalService.getAnimal(male.getName())).thenReturn(male);
        try {
            String motherToFail = male.getName();
            AnimalDTO animalWithFemaleFather = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).mother(motherToFail).build();
            animalController.createAnimal(animalWithFemaleFather);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The field mother need to be a female", error.getMessage());
            assertEquals("CODE_03", error.getCode().name());
        }
    }

    @Test
    public void nameFieldMoreThan120Characters() {
        try {
            String nameToFail = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            AnimalDTO animalWithBadName = AnimalDTO.builder().name(nameToFail).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithBadName);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The name cannot be greater than 120 characters", error.getMessage());
            assertEquals("CODE_04", error.getCode().name());
        }
    }

    @Test
    public void nameFieldWithSpecialCharacters() {
        try {
            String nameToFail = "@nameToFail&";
            AnimalDTO animalWithBadName = AnimalDTO.builder().name(nameToFail).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithBadName);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("Animal name should contain only letters and spaces", error.getMessage());
            assertEquals("CODE_06", error.getCode().name());
        }
    }

    @Test
    public void weightFieldMoreThanStandard() {
        try {
            double weightToFail = 201;
            AnimalDTO animalWithBadWeight = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(weightToFail).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithBadWeight);
            fail();
        } catch (AnimalException e) {
            weightFieldValidationException(e);
        }
    }

    @Test
    public void weightFieldLessThanStandard() {
        try {
            double weightToFail = 0.29;
            AnimalDTO animalWithBadWeight = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(weightToFail).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithBadWeight);
            fail();
        } catch (AnimalException e) {
            weightFieldValidationException(e);
        }
    }

    private void weightFieldValidationException(AnimalException e) {
        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        assertNotNull(e.getError());
        AnimalError error = e.getError();
        assertEquals("The weight of the animal is not realistic", error.getMessage());
        assertEquals("CODE_07", error.getCode().name());
    }

    @Test
    public void heightFieldMoreThanStandard() {
        try {
            double heightToFail = 2.01;
            AnimalDTO animalWithBadHeight = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(heightToFail).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithBadHeight);
            fail();
        } catch (AnimalException e) {
            heightFieldValidationException(e);
        }
    }

    @Test
    public void heightFieldLessThanStandard() {
        try {
            double heightToFail = 0.09;
            AnimalDTO animalWithBadHeight = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(heightToFail).weight(ANIMAL_WEIGHT).arrivalDate(ANIMAL_ARRIVAL_DATE).build();
            animalController.createAnimal(animalWithBadHeight);
            fail();
        } catch (AnimalException e) {
            heightFieldValidationException(e);
        }
    }

    private void heightFieldValidationException(AnimalException e) {
        assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        assertNotNull(e.getError());
        AnimalError error = e.getError();
        assertEquals("The height of the animal is not realistic", error.getMessage());
        assertEquals("CODE_08", error.getCode().name());
    }

    @Test
    public void arrivalDateFieldFromFuture() {
        try {
            LocalDateTime arrivalDateToFail = LocalDateTime.of(3000, 10, 10, 0, 0, 0);
            AnimalDTO animalWithBadArrivalDate = AnimalDTO.builder().name(ANIMAL_NAME).sex(ANIMAL_SEX).age(ANIMAL_AGE).height(ANIMAL_HEIGHT).weight(ANIMAL_WEIGHT).arrivalDate(arrivalDateToFail).build();
            animalController.createAnimal(animalWithBadArrivalDate);
            fail();
        } catch (AnimalException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
            assertNotNull(e.getError());
            AnimalError error = e.getError();
            assertEquals("The arrival date can't be a future date", error.getMessage());
            assertEquals("CODE_09", error.getCode().name());
        }
    }

    @Test
    public void getEmptyAnimals() {
        assertEquals(0, animalController.getAnimals().size());
    }

    @Test
    public void getNullAnimal() {
        assertNull(animalController.getAnimal(ANIMAL_NAME));
    }
}
