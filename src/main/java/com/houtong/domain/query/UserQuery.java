package com.houtong.domain.query;

import com.houtong.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//用于User的查询
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery extends User {
    private LocalDate birthday2;
}