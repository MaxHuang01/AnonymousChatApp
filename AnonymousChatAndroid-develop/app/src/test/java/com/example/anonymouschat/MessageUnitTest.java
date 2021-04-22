package com.example.anonymouschat;

import com.example.anonymouschat.models.Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageUnitTest {

    @Test
    public void check_sunnyCase_Message(){
        Message myMessage = new Message("hello sir",true);
        String expectedReturn = "hello sir";
        Boolean expectedReturnBool = true;
        assertEquals(expectedReturn, myMessage.getText());
        assertEquals(expectedReturnBool, myMessage.isBelongsToCurrentUser());
    }

    @Test
    public void check_falseCase_Message(){
        Message myMessage = new Message("hello sir",false);
        String expectedReturn = "hello sir";
        Boolean expectedReturnBool = false;
        assertEquals(expectedReturn, myMessage.getText());
        assertEquals(expectedReturnBool, myMessage.isBelongsToCurrentUser());
    }

    @Test
    public void check_empty_Message(){
        Message myMessage = new Message("",true);
        String expectedReturn = "";
        Boolean expectedReturnBool = true;
        assertEquals(expectedReturn, myMessage.getText());
        assertEquals(expectedReturnBool, myMessage.isBelongsToCurrentUser());
    }
}
