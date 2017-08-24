-- create database mybatis;
-- use mybatis;
-- CREATE TABLE users(id INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(20), age INT);
-- INSERT INTO users(NAME, age) VALUES('孤傲苍狼', 27);
-- INSERT INTO users(NAME, age) VALUES('白虎神皇', 27);


CREATE  TABLE `blog_db`.`user` (
`id` INT NOT NULL AUTO_INCREMENT ,
`name` VARCHAR(45) NOT NULL ,
`email` VARCHAR(45) NOT NULL ,
`password` VARCHAR(45) NOT NULL ,
PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

ALTER TABLE `blog_db`.`user`
ADD UNIQUE INDEX `name_unique` (`name` ASC) ;

INSERT INTO `blog_db`.`user` (`name`, `email`, `password`) VALUES ('letian', 'letian@111.com', '123');

INSERT INTO `blog_db`.`user` ( `name`, `email`, `password`) VALUES ('xiaosi', 'xiaosi@111.com', '123');
