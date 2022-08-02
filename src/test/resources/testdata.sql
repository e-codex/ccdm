-- noinspection SqlNoDataSourceInspectionForFile

insert into cmt_config(config_id, environment, project, party, version, download_date, publish_date, zip) values (1, 'Oberunterdumpfing', 'X Files', 'SS Sinking Party', '13', TIMESTAMP '2022-02-03 14:23:11', TIMESTAMP '1999-05-15 13:54:23', 'avc');
insert into cmt_config(config_id, environment, project, party, version, download_date, publish_date, zip) values (2, 'Schland', 'Automaticus', 'Conformed Conforms', '101', TIMESTAMP '2022-02-03 14:23:11', TIMESTAMP '2021-07-16 05:46:11', 'pub');
insert into cmt_config(config_id, environment, project, party, version, download_date, publish_date, zip) values (3, 'Commonland', 'Unificando', 'Uniformia', '11', TIMESTAMP '2022-02-03 14:23:11', TIMESTAMP '1988-08-22 20:19:36', 'bvz');

insert into config_deploy(deploy_id, deployment_date, component, principal) values (1, TIMESTAMP '2022-05-02 15:25:11', 'GUI', 'Art Vandelay');
insert into config_deploy(deploy_id, deployment_date, component, principal) values (2, TIMESTAMP '1998-08-15 07:36:54', 'CLI', 'Susan Ross');
insert into config_deploy(deploy_id, deployment_date, component, principal) values (3, TIMESTAMP '1979-03-24 04:03:47', 'Computer', 'Mr vanDudenheim');

insert into cmt_project(id, name, description) values (1, 'Project Fuzzy Boots', 'Something with Wings');
insert into cmt_project(id, name, description) values (2, 'Project Vandeleigh', 'Something with Art');
insert into cmt_project(id, name, description) values (3, 'Project Thunderlight', 'Something with Bluetooth');

insert into cmt_use_case(id, name, type, fk_project_id) values (1, 'Project Vandeleigh', 'Musical', 1);
insert into cmt_use_case(id, name, type, fk_project_id) values (2, 'Project Thunderlight', 'Arts & Crafts', 1);
insert into cmt_use_case(id, name, type, fk_project_id) values (3, 'Project Fuzzy Boots', 'Tech', 3);