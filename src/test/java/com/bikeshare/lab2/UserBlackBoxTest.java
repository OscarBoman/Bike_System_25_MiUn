package com.bikeshare.lab2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.MembershipType;
import com.bikeshare.model.User;

/**
 * Lab 2 Template: Black Box Testing for User class
 * 
 * 
 * 
 * TODO for students:
 * - Challenge 2.1: Add Equivalence Partitioning tests for email validation,
 * name, telephone number (With GenAI help), and fund addition
 * - Challenge 2.2: Add Boundary Value Analysis tests for fund addition
 * - Challenge 2.3: Add Decision Table tests for phone number validation
 * - Optional Challenge 2.4: Add error scenario tests
 */

// This test is just an example to get you started. You will need to add more
// tests as per the challenges.
@DisplayName("Verify name handling in User class")
class UserBlackBoxTest {

    @Test
    @DisplayName("Should store and retrieve user names correctly")
    void shouldStoreAndRetrieveUserNamesCorrectly() {
        // Arrange - Set up test data
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        // Act - Execute the method under test
        User user = new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName);
        String actualFirstName = user.getFirstName();
        String actualLastName = user.getLastName();
        String actualFullName = user.getFullName();

        // Assert - Verify the expected outcome
        assertNotNull(user, "User should be created successfully");
        assertEquals(expectedFirstName, actualFirstName, "First name should match");
        assertEquals(expectedLastName, actualLastName, "Last name should match");
        assertEquals("John Doe", actualFullName, "Full name should be formatted correctly");
    }

    // TODO: Challenge 2.1 - Add Equivalence Partitioning tests for email validation
    // Hint: Test valid emails (user@domain.com) and invalid emails (missing @,
    // empty, etc.)

    @Test
    @DisplayName("EP test for email")
    public void epTestEmail() {
        String validEmail = "name@hotmail.com";
        String validEmailWithNumbers = "name123@mail.com";
        String validEmailWithDot = "name.name@hotmail.com";

        String invalidEmailNoHost = "name@.com";
        String invalidEmailNoDomain = "name@hotmail";
        String invalidEmailTwoDots = "name..name@hotmail.com";
        String invalidEmailEmptyString = "";

        User user = new User("901101-1237", "john.doe@example.com", "John", "Doe");

        user.setEmail(validEmail);
        assertEquals(validEmail, user.getEmail());

        user.setEmail(validEmailWithNumbers);
        assertEquals(validEmailWithNumbers, user.getEmail());

        user.setEmail(validEmailWithDot);
        assertEquals(validEmailWithDot, user.getEmail());

        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail(invalidEmailNoHost);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail(invalidEmailNoDomain);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail(invalidEmailEmptyString);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail(invalidEmailTwoDots);
        });

        // Should not work to pass an Email with 2 consecutive dots per RFC 5322.
    }

    @Test
    @DisplayName("ep name")
    void epTestValidNames() {
        String[] names = {"name" , "name name", "name-name", "namé"};

        User user = new User("901101-1237", "john.doe@example.com", "John", "Doe");

        for(String name : names) {
            user.setFirstName(name);
            assertEquals(name, user.getFirstName());
        }

         for(String name : names) {
            user.setLastName(name);
            assertEquals(name, user.getLastName());
        }

        //Am i checking the wrong method? Cant find any method where name validation is performed. 
    }

     @Test
    @DisplayName("ep name")
    void epTestInvalidNames() {
        String[] names = {"123" , "s", "ddddddddddddddddddddddddddddddddddddddddddddddddssssssssddd"};

        User user = new User("901101-1237", "john.doe@example.com", "John", "Doe");

        for(String name : names) {
            user.setFirstName(name);
            assertEquals(name, user.getFirstName());
        }

         for(String name : names) {
            user.setLastName(name);
            assertEquals(name, user.getLastName());
        }
    }

     @Test
    @DisplayName("Decision-table test for phone number validation. Made with AI")
    void phoneNumberValidationTest() {
        User user = new User("901101-1237", "john.doe@example.com", "John", "Doe");

        // ✅ Pass cases
        assertDoesNotThrow(() -> user.setPhoneNumber("0701234567"));   // valid, Swedish mobile
        assertDoesNotThrow(() -> user.setPhoneNumber("+46723456789")); // valid, international format

        // ❌ Fail cases
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("1234567899"));  // no leading 0
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("+48523456789")); // wrong country code (+48)
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("+46123456789")); // wrong operator prefix (not 7)
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("07123456789"));  // 11 digits, not 10
        assertThrows(IllegalArgumentException.class, () -> user.setPhoneNumber("+7622522"));     // too short (not 12 digits)
    }

    // TODO: Challenge 2.2 - Add Boundary Value Analysis tests for fund addition

    @Test
    @DisplayName("Boundary test for found addition")
    void bvaTestValidNumbers() {
        double validMinFounds = 0.1;
        double validMinPlus1 = 0.11;
        double validNominal = 500;
        double validMaxMinus1 = 999;
        double validMax = 1000;

        User user = new User("901101-1237", "john.doe@example.com", "John", "Doe");

        user.addFunds(validMinFounds);
        assertEquals(validMinFounds, user.getAccountBalance());
        user.deductFunds(validMinFounds);

        user.addFunds(validMinPlus1);
        assertEquals(validMinPlus1, user.getAccountBalance());
        user.deductFunds(validMinPlus1);

        user.addFunds(validNominal);
        assertEquals(validNominal, user.getAccountBalance());
        user.deductFunds(validNominal);

        user.addFunds(validMaxMinus1);
        assertEquals(validMaxMinus1, user.getAccountBalance());
        user.deductFunds(validMaxMinus1);

        user.addFunds(validMax);
        assertEquals(validMax, user.getAccountBalance());
        user.deductFunds(validMax);

    }

    @Test
    @DisplayName("Boundary test for invalid found addition")
    void bvaTestInvalidNumbers() {

        double invalidMinMinus1 = 0.09;
        double invalidMaxPlus1 = 1000.1;

        User user = new User("901101-1237", "john.doe@example.com", "John", "Doe");

        assertThrows(IllegalArgumentException.class, () -> {
            user.addFunds(invalidMaxPlus1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user.addFunds(invalidMinMinus1);
        });

        //0.09 is under the limit, should pass
    }

    // TODO: Challenge 2.3 - Add Decision Table tests for phone number validation
    // Hint: Test Swedish phone formats (+46701234567, 0701234567) and invalid
    // formats
    @Test
    @DisplayName("Decision table test for membership discount")
    void memebershipDiscountTest () {
        User user1 = new User("901101-1237", "john.doe@example.com", "John", "Doe");
        user1.verifyEmail();
        user1.activate();
        user1.updateMembership(User.MembershipType.PREMIUM);    

        User user2 = new User("901101-1237", "john.doe@example.com", "John", "Doe");
        user2.verifyEmail();
        user2.activate();
        user2.updateMembership(User.MembershipType.STUDENT); 

        User user3 = new User("901101-1237", "john.doe@example.com", "John", "Doe");
        user3.verifyEmail();
        user3.activate();
        user3.updateMembership(User.MembershipType.CORPORATE); 

        assertEquals(0.15, user1.calculateDiscount());
        assertEquals(0.20, user2.calculateDiscount());
        assertEquals(0.10, user3.calculateDiscount());
    }

    // TODO: Challenge 2.4 - Add error scenario tests
    // Hint: Test insufficient balance, invalid inputs, state violations
}
