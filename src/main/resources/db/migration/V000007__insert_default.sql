INSERT INTO permissao (nome, descricao)
VALUES ('usuario:admin', 'Permissão para usuários administrador');

INSERT INTO permissao (nome, descricao)
VALUES ('usuario:sistema', 'Permissão para usuários do sistema');

INSERT INTO perfil (tipo, nome, descricao)
VALUES ('ROLE_ADMIN', 'Administrador', 'Perfil para usuários que realizam funções administrativas no aplicativo.');

INSERT INTO perfil (tipo, nome, descricao)
VALUES ('ROLE_USUARIO', 'Usuário', 'Perfil para usuários que realizam funções customizados no aplicativo.');

INSERT INTO permissao_perfil (perfil_id, permissao_id)
VALUES ((SELECT id FROM perfil WHERE nome = 'Administrador'), (SELECT id FROM permissao WHERE nome = 'usuario:admin'));
