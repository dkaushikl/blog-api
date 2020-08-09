# Blog schema
 
# --- !Ups
CREATE TABLE IF NOT EXISTS `blogs-sb`.`blog` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(500) NOT NULL,
  `description` VARCHAR(2000) NOT NULL,
  `authorId` INT NOT NULL,
  `isShown` boolean NOT NULL,
  PRIMARY KEY (`id`))
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8
 
# --- !Downs
drop table 'blog'