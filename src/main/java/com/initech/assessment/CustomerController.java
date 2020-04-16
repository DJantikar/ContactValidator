package com.initech.assessment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/")
public class CustomerController {
    @GetMapping("/validate")
    public ResponseDto contactValidator(@RequestParam String contact){
        ResponseDto responseDto = new ResponseDto();
        // Check if contact is exceeding 80 chars
        if(contact.length() > 80){
            responseDto.setMaskedContact("");
            responseDto.errorMessage="Contact is exceeding 80 characters.";
            return  responseDto;
        }
        if(contact.contains("@")){
            responseDto=checkForEmailValidity(contact);
        }
        else{
            responseDto = checkPhoneNumValidity(contact);
        }
        return responseDto;
    }

    private ResponseDto checkPhoneNumValidity(String contact) {
        ResponseDto responseDto = new ResponseDto();
        String regexForPhone = "\\(?\\d{3}\\)?[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{4}";
        String regexForDigit = "\\d{1}";
        boolean isContactValidPhoneNumber = contact.matches(regexForPhone);
        if(!isContactValidPhoneNumber){
            responseDto.setErrorMessage("Invalid Phone Number : Please enter phone number.");
            responseDto.setMaskedContact("");
            return responseDto;
        }
        int temp=0;
        StringBuffer midThree = new StringBuffer("");
        for(int i=0;i<contact.length();i++){
            temp = Character.isDigit(contact.charAt(i)) ? temp+1 : temp;
            if(temp>=4 && temp<=6){
                midThree.append(contact.charAt(i));
                if(midThree.length() == 3){
                    break;
                }
            }
        }
        StringBuffer maskedPhoneNumber = new StringBuffer(contact);
        int startIndex = contact.indexOf(midThree.toString());
        maskedPhoneNumber.replace(startIndex,startIndex+3,"***");
        responseDto.setMaskedContact(maskedPhoneNumber.toString());
        responseDto.setErrorMessage("");
        return responseDto;
    }

    private ResponseDto checkForEmailValidity(String contact) {
        ResponseDto responseDto = new ResponseDto();
        boolean isContactValidEmail = EmailValidator.getInstance().isValid(contact);
        if(!isContactValidEmail){
            responseDto.setErrorMessage("Invalid Email : Please enter valid email id.");
            responseDto.maskedContact="";
            return responseDto;
        }
        StringBuffer maskedEmail = new StringBuffer(contact);
        int lastIndex = contact.lastIndexOf("@");
        int len = lastIndex-2;
        String mask="*".repeat(len);
        maskedEmail.replace(1,lastIndex-1,mask);
        responseDto.setMaskedContact(maskedEmail.toString());
        responseDto.setErrorMessage("");
        return responseDto;
    }

    public static void main(String[] args){
        ResponseDto res = new CustomerController().contactValidator("1234567899e");
        System.out.println(res.toString());
    }
}
