

DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS USER_ROLE;
DROP TABLE IF EXISTS SEC_ROLE;
DROP TABLE IF EXISTS SEC_USER;



create table SEC_USER
(
  userId           BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userName         VARCHAR(36) NOT NULL UNIQUE,
  encryptedPassword VARCHAR(128) NOT NULL,
  ENABLED           BIT NOT NULL 
) ;


insert into SEC_User (userName, encryptedPassword, ENABLED)
values 
('Jon', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1),
('Tony Stark', '$2a$10$yaC3cut1bq9YKZLZq7lDle.CPQeugNjmF86plTFqYKcK3OjtlPTk6', 1),
('Bruce Wayne', '$2a$10$tWyaxWVrVjbQ//E.SnwGDe8zIf6UMlpnkwwqPDqmBTYLA8qc3/LqS', 1),
('Peter Parker', '$2a$10$5po.eXuZlPq/IxZIaU/mWOSVpKdx5L.uThQ.XnqrREu3LQj/0M5Ly', 1),
('Bob Loren', '$2a$10$eineeEA.ncKi9K32oVMc5O609MhSGJh3OqGpEQXY//GHNNafibJuu', 1),
('Leron Mendal', '$2a$10$s/eLcleLu9glTp4nM.J3..JtybnuPHGNCF3Z9n.ouMbOZUySBfMja', 1),
('Modal Keylee', '$2a$10$C2VdCdmwOariCQFaN5zWseOmD6qut9pllu5H4ysEf6wn.wBBTQq36', 1),
('Kevin Brown', '$2a$10$tQ73L2E22Gy55RqisP5siewhFIPfe7oDxYqniM1TseJ1cp2RmHfFG', 1),
('John Limen', '$2a$10$njdXe/S28emND//SGLCJv.x73L6h5gkhLMxGUF3CTbbyTB2h4At1O', 1),
('Tom Hanker', '$2a$10$3fVCAlm6Br9t46YtOYubcOwh/OTCLGBLu/Bm9ax/wgdWCg1PPrmim', 1),
('Lio Gram', '$2a$10$lBrF6jL9154MrOb9lttCbuFu/bhqFKnCCtwyvCfHuMsYZT9Tf5tWa', 1);



create table SEC_ROLE
(
  roleId   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  roleName VARCHAR(30) NOT NULL UNIQUE
) ;


insert into sec_role (roleName)
values ('ROLE_PROFESSOR');

insert into sec_role (roleName)
values ('ROLE_STUDENT');


create table USER_ROLE
(
  ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  userId BIGINT NOT NULL,
  roleId BIGINT NOT NULL
);

alter table USER_ROLE
  add constraint USER_ROLE_UK unique (userId, roleId);

alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (userId)
  references SEC_USER (userId);
 
alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (roleId)
  references SEC_ROLE (roleId);


insert into user_role (userId, roleId)
values 
(1, 1),
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 2),
(10, 2),
(11, 2);


  
  
CREATE TABLE student 
(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	studentName VARCHAR(255), 
	studentId VARCHAR(255), 
	exercises DECIMAL(4,1), 
	assignment1 DECIMAL(4,1),
	assignment2 DECIMAL(4,1),
	assignment3 DECIMAL(4,1),
	midterm DECIMAL(4,1),
	finalExam DECIMAL(4,1)
);

INSERT INTO student (studentName, studentId, exercises, assignment1, assignment2, assignment3, midterm, finalExam)
VALUES 
('Tony Stark', 997265828, 85, 70, 60, 71, 80, 90),
('Bruce Wayne', 874536896, 69, 89, 86, 60, 90, 30),
('Peter Parker', 193772852, 87, 47, 86, 48, 63, 62),
('Bob Loren', 176039275, 72, 82, 72, 84, 91, 62),
('Leron Mendal', 173926394, 83, 73, 92, 85, 98, 86),
('Modal Keylee', 673629384, 98, 47, 36, 56, 75, 64),
('Kevin Brown', 329827394, 74, 76, 97, 37, 76, 48),
('John Limen', 293850293, 77, 97, 98, 99, 94, 97),
('Tom Hanker', 106748294, 90, 93, 95,91, 93, 83),
('Lio Gram', 671928375, 84, 74, 63, 86, 73, 98);


