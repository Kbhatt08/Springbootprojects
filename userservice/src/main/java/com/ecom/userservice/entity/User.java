package com.ecom.userservice.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author admin
 */
@Getter
@Setter
@Builder
public class User {
    int id;
    String username;
    String email;
    String password;
    Timestamp creatdAt;
}
