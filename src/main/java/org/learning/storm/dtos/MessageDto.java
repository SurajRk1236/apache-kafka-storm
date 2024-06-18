package org.learning.storm.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    String data;
    Integer counter;

    public static List<MessageDto> getMessageData() {
        List<MessageDto> mutableList = new ArrayList<>();
        mutableList.add(new MessageDto("hi", 1));
        mutableList.add(new MessageDto("hello", 2));
        mutableList.add(new MessageDto("hi", 3));
        return mutableList;
    }
}