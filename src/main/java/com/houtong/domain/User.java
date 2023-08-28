package com.houtong.domain;

import com.baomidou.mybatisplus.annotation.*;
import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    //设置为ID自增策略（默认为随机生成ID即IdType.ASSIGN_ID）
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;

    //如下注解可以使得该字段无法被查询
     //@TableField(select = false)
    private String password;
    private LocalDate birthday;
    //逻辑删除的注解
    //@TableLogic(value = "0",delval = "1")
    private Integer deleted;
    //乐观锁注解
    @Version
    private Integer version;
}
