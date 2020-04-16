package com.initech.assessment;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class CustomerControllerTests {

    @Test
    void testContactExceeding80Chars() {
        String testString = "test".repeat(21);
        ResponseDto res = new CustomerController().contactValidator(testString);
        assertTrue(res.getMaskedContact().equals(""));
        assertTrue(res.getErrorMessage().equals("Contact is exceeding 80 characters."));
    }
    @Test
    void testValidEmail() {
        String testString = "abcd@gmail.com";
        ResponseDto res = new CustomerController().contactValidator(testString);
        assertTrue(res.getMaskedContact().equals("a**d@gmail.com"));
        assertTrue(res.getErrorMessage().equals(""));
    }
    @Test
    void testInvalidEmail() {
        String testString = "abcd@gmail@com";
        ResponseDto res = new CustomerController().contactValidator(testString);
        assertTrue(res.getMaskedContact().equals(""));
        assertTrue(res.getErrorMessage().equals("Invalid Email : Please enter valid email id."));
    }
    @Test
    void testValidPhone(){
        String valid1 = "1234567890";
        String valid2 = "123-456-7890";
        String valid3 = "(123)-456-7890";
        String valid4 = "123.456.7890";
        String valid5 = "123 456 7890";
        ResponseDto res = new CustomerController().contactValidator(valid1);
        assertTrue(res.getMaskedContact().equals("123***7890"));
        assertTrue(res.getErrorMessage().equals(""));
        res = new CustomerController().contactValidator(valid2);
        assertTrue(res.getMaskedContact().equals("123-***-7890"));
        assertTrue(res.getErrorMessage().equals(""));
        res = new CustomerController().contactValidator(valid3);
        assertTrue(res.getMaskedContact().equals("(123)-***-7890"));
        assertTrue(res.getErrorMessage().equals(""));
        res = new CustomerController().contactValidator(valid4);
        assertTrue(res.getMaskedContact().equals("123.***.7890"));
        assertTrue(res.getErrorMessage().equals(""));
        res = new CustomerController().contactValidator(valid5);
        assertTrue(res.getMaskedContact().equals("123 *** 7890"));
        assertTrue(res.getErrorMessage().equals(""));
    }
    @Test
    void testInvalidPhone(){
        String invalidPhone = "123 456 7890#";
        ResponseDto res = new CustomerController().contactValidator(invalidPhone);
        assertTrue(res.getMaskedContact().equals(""));
        assertTrue(res.getErrorMessage().equals("Invalid Phone Number : Please enter phone number."));
    }
}
