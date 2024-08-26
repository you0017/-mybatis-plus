package com.yc.pojo;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private Long id;
//	@TableField(condition = SqlCondition.LIKE)   // 配置该字段使用like进行拼接
	private String name;
	private Integer age;
	private String email;
	private Long managerId;
	//@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createTime;
}
