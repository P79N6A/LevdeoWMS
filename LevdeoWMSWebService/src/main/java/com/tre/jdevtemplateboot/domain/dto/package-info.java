package com.tre.jdevtemplateboot.domain.dto;

/**
 * dto
 * data transfer object：数据传输对象
 * 是一种设计模式之间传输数据的软件应用系统，数据传输目标往往是数据访问对象从数据库中检索数据
 * 数据传输对象与数据交互对象或数据访问对象之间的差异是一个以不具任何行为除了存储和检索的数据（访问和存取器）
 * 简而言之，就是接口之间传递的数据封装
 * 表里面有十几个字段：id，name，gender（M/F)，age……
 * 页面需要展示三个字段：name，gender(男/女)，age
 * DTO由此产生，一是能提高数据传输的速度(减少了传输字段)，二能隐藏后端表结构
 */