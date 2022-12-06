
INSERT INTO public.project
(created_at, updated_at, isactive, projectcode, projectdesc)
VALUES
('2022-11-29 16:40:40.202', '2022-11-29 16:40:40.202', true, 'TS-123', 'TeamSupport1');
INSERT INTO public.project
(created_at, updated_at, isactive, projectcode, projectdesc)
VALUES
('2022-11-29 16:40:40.202', '2022-11-29 16:40:40.202', true, 'TS-456', 'TeamSupport2');
INSERT INTO public.project
(created_at, updated_at, isactive, projectcode, projectdesc)
VALUES
('2022-11-29 16:40:40.202', '2022-11-29 16:40:40.202', false, 'TS-789', 'TeamSupport3');
INSERT INTO public.people
(cognizantid, projectid, communityname, firstname, middlename, lastname, isactive, jobleveldesc, projectdesc)
VALUES
(1, 1, 'CSV', 'John Raven', 'Tamang', 'Glomar', true, 'Level Desc Test', 'CommunityHelperSpringBoot');
INSERT INTO public.people
(cognizantid, projectid, communityname, firstname, middlename, lastname, isactive, jobleveldesc, projectdesc)
VALUES
(2, 1, 'CSV', 'Cris John', '', 'Baquiran', true, 'Level Desc Test', 'CommunityHelperSpringBoot');
INSERT INTO public.people
(cognizantid, projectid, communityname, firstname, middlename, lastname, isactive, jobleveldesc, projectdesc)
VALUES
(3, 1, 'CSV', 'Marvin', '', 'Hipolito', false, 'Level Desc Test', 'CommunityHelperSpringBoot');
INSERT INTO public.people
(cognizantid, projectid, communityname, firstname, middlename, lastname, isactive, jobleveldesc, projectdesc)
VALUES
(4, 2, 'CSV', 'Ryan', '', 'Aristosa', true, 'Level Desc Test', 'CommunityHelperSpringBoot');


