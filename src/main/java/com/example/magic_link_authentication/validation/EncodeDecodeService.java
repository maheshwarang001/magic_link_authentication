package com.example.magic_link_authentication.validation;


import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
public class EncodeDecodeService {

    public <T> byte[] encode(T data) {
        if (data instanceof UUID uuidData) {
            String str = uuidData.toString();
            return Base64.getEncoder().encode(str.getBytes());
        }

        return null;
    }


    public <T> T decode(byte[] encodedData, Class<T> targetType) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);

        if (String.class.equals(targetType)) {
            return targetType.cast(new String(decodedBytes));
        }
        else if (UUID.class.equals(targetType)) {
            String str = new String(decodedBytes);
            return targetType.cast(UUID.fromString(str));
        }



        return null;
    }



}


